/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.mailster.smtp.SMTPServer;
import org.mailster.smtp.api.listener.MessageListener;
import org.mxhero.engine.plugin.postfixconnector.internal.ConnectorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a listener in a port used to communicate with a MTA server. Each mail
 * received it will be added into the InputQueue so it can be processed.
 * 
 * @author mmarmol
 */
public final class SMTPListener implements Runnable {

	private static Logger log = LoggerFactory.getLogger(SMTPListener.class);

	private boolean running = false;

	private CustomSMTPServer server;

	private ConnectorProperties properties;

	private Thread thread;

	private static final long WAITTIME = 1000;
	private Charset charset = Charset.forName("UTF-8");


	/**
	 * Creates the object with the Message listener as a parameter.
	 * 
	 * @param listener
	 */
	public SMTPListener(MessageListener listener) {
		server = new CustomSMTPServer(listener);
		server.getDeliveryHandlerFactory().setDeliveryHandlerImplClass(
				CustomDeliveryHandler.class);
	}

	/**
	 * Create and start a thread with this class as Runnable.
	 */
	public void start() {
		log.debug("started");
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Implementation of run that call init() before, them loops work() while
	 * running is true and when exit the loop call clean() and ends.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		running = true;
		init();
		log.info("RUN");
		while (isRunning()) {
			work();
		}
		clean();
	}

	/**
	 * We are using apache mina, so threads are managed in that library. Here we
	 * will sleep so we can check after that if some one called us to stop.
	 */
	private void work() {
		try {
			Thread.sleep(WAITTIME);
		} catch (InterruptedException e) {
			log.warn("Interruped while waiting", e);
		}
	}

	/**
	 * starts the smpt server.
	 */
	private void init() {
		if (server != null && !server.isRunning()) {
			log.info("INIT");

			server.setPort(getProperties().getSmtpPort());
			server.getConfig().setHostName(getProperties().getSmtpHost());
			server.getConfig().setReceiveBufferSize(getProperties().getReceiveBufferSize());
			server.getConfig().setDataDeferredSize(getProperties().getDeferredSize());
			server.getConfig().setMaxConnections(getProperties().getMaxConnections());
			server.getConfig().setCharset(charset);
			server.getConfig().setConnectionTimeout(getProperties().getConnectionTimeout());
			server.getConfig().setHostName(getProperties().getHostName().trim());
			server.getConfig().setMaxRecipients(getProperties().getMaxRecipients());
			
			server.start();
		}
	}

	/**
	 * Shutdown the smtp server.
	 */
	private void clean() {
		if (server != null && server.isRunning()) {
			log.info("CLEAN");
			server.shutdown();
		}

	}

	/**
	 * Just puts the false value into running property.
	 */
	public void stop() {
		log.info("STOP");
		running = false;
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				log.warn("interrupted", e);
			}
			thread = null;
		}
	}

	/**
	 * @return the value of running property
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * We customized this class so the acceptor used by Apache Mina reuses
	 * closed ports, if we don't do this and immediate restart from this plugin
	 * it will fails.
	 * 
	 * @author mmarmol
	 */
	private static class CustomSMTPServer extends SMTPServer {

		/**
		 * @see org.mailster.smtp.SMTPServer
		 */
		public CustomSMTPServer(MessageListener listener) {
			super(listener);
			Field privateAcceptor;
			try {
				log
						.debug("Setting SocketAcceptor to public and to reuse port.");
				privateAcceptor = SMTPServer.class.getDeclaredField("acceptor");
				privateAcceptor.setAccessible(true);
				SocketAcceptor acceptor = (SocketAcceptor) privateAcceptor
						.get(this);
				acceptor.setReuseAddress(true);
			} catch (Exception e) {
				log.error("Error while setting SocketAcceptor.", e);
			}

		}

		/**
		 * @see org.mailster.smtp.SMTPServer#shutdown()
		 */
		@Override
		public synchronized void shutdown() {
			super.shutdown();

			Field privateAcceptor;
			try {
				log
						.debug("Setting SocketAcceptor to public and to dispose port.");
				privateAcceptor = SMTPServer.class.getDeclaredField("acceptor");
				privateAcceptor.setAccessible(true);
				SocketAcceptor acceptor = (SocketAcceptor) privateAcceptor
						.get(this);
				acceptor.dispose();
			} catch (Exception e) {
				log.error("Error while setting SocketAcceptor.", e);
			}
		}
	}

	/**
	 * @return the properties
	 */
	public ConnectorProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(ConnectorProperties properties) {
		this.properties = properties;
	}

}
