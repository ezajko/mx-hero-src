package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.lang.reflect.Field;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.mailster.smtp.SMTPServer;
import org.mailster.smtp.api.listener.MessageListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
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

	private PropertiesService properties;

	private Thread thread;

	private static final long WAITTIME = 1000;

	/**
	 * Creates the object with the Message listener as a parameter.
	 * 
	 * @param listener
	 */
	public SMTPListener(SMTPMessageListener listener) {
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
			server.setPort(Integer.parseInt(properties
					.getValue(PostfixConnector.SMTP_PORT)));
			server.getConfig().setHostName(
					properties.getValue(PostfixConnector.SMTP_HOST));
			server.getConfig().setReceiveBufferSize(Integer.parseInt(properties
					.getValue(PostfixConnector.RECEIVE_BUFFER_SIZE)));
			server.getConfig().setDataDeferredSize(Integer.parseInt(properties
					.getValue(PostfixConnector.DEFERRED_SIZE)));
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
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}
