package org.mxhero.engine.plugin.clamd.internal.scanner;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.clamd.internal.service.Clamd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class connect to spamd and uses a handler to manage all the message
 * between them.
 * 
 * @author mmarmol
 */
public class ClamAV {

	private static Logger log = LoggerFactory.getLogger(ClamAV.class);

	private static final String STREAM_COMMAND = "STREAM";

	public static final long DEFAULT_CONNECTION_TIMEOUT = 1000;
	public static final long DEFAULT_RESPONSE_TIMEOUT = 45000;
	public static final String DEFAULT_HOSTNAME = "localhost";
	public static final int DEFAULT_PORT = 6665;
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	public static final int WRONG_STREAM_PORT = -1;

	private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
	private long reponseTimeout = DEFAULT_RESPONSE_TIMEOUT;
	private String hostname = DEFAULT_HOSTNAME;
	private int port = DEFAULT_PORT;
	private int bufferSize = DEFAULT_BUFFER_SIZE;
	private int streamPort = WRONG_STREAM_PORT;
	private IoConnector connector = null;
	private IoSession session = null;
	private boolean open;

	private PropertiesService properties;

	private BlockingQueue<String> scanResponse = new LinkedBlockingQueue<String>();

	private Part part = null;

	/**
	 * Basic constructor
	 * 
	 * @param part
	 *            to be analyzed.
	 */
	public ClamAV(Part part) {
		this.part = part;
	}

	/**
	 * Open the connection with the spamd.
	 * 
	 * @return true if connection was establish and false in other case.
	 */
	protected boolean open() {
		if (!isOpen()) {
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(getConnectionTimeout());
			connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new TextLineCodecFactory()));
			connector.setHandler(new ClamAVHandler(this));
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(
						getHostname(), getPort()));
				future.awaitUninterruptibly();
				session = future.getSession();
				log.debug("connection open");
				return true;
			} catch (RuntimeIoException e) {
				log.debug("error while openning session", e);
			}
		}
		log.debug("failed to open connection");
		return false;
	}

	/**
	 * Close the connection with spamd
	 * 
	 * @return true is manages to close the connection, false if wasn't open or
	 *         had an error
	 */
	protected boolean close() {
		if (isOpen()) {
			session.getCloseFuture().awaitUninterruptibly();
			connector.dispose();
			return true;
		}
		return false;
	}

	/**
	 * Open the session and waits for response from spamd.
	 * 
	 * @return
	 */
	public String scan() {
		updateInitial();
		open();
		session.write(STREAM_COMMAND).awaitUninterruptibly();
		String response = null;
		try {
			response = scanResponse.poll(getReponseTimeout(),
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			log.warn("error:", e);
		}
		close();
		return response;
	}

	/**
	 * Called when connection is logically established to send a stream with the
	 * part to be analyzed.
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected void sendStream() throws IOException, MessagingException {
		log.debug("sending stream to port:" + getStreamPort());
		log.debug("type: " + getPart().getContentType() + " name: "
				+ getPart().getFileName());
		Socket streamSocket = new Socket(getHostname(), getStreamPort());
		BufferedOutputStream bos = new BufferedOutputStream(streamSocket
				.getOutputStream(), getBufferSize());
		if (getPart() instanceof Message) {
			getPart().writeTo(bos);
		} else {
			InputStream inputStream = part.getInputStream();
			int i;
			while ((i = inputStream.read()) != WRONG_STREAM_PORT) {
				bos.write(i);
			}
		}
		bos.flush();
		bos.close();
		streamSocket.close();
		log.debug("stream sent");
	}

	/**
	 * @return the true if the connection is open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * @return the connectionTimeout
	 */
	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout
	 *            the connectionTimeout to set
	 */
	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return the reponseTimeout
	 */
	public long getReponseTimeout() {
		return reponseTimeout;
	}

	/**
	 * @param reponseTimeout
	 *            the reponseTimeout to set
	 */
	public void setReponseTimeout(long reponseTimeout) {
		this.reponseTimeout = reponseTimeout;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the streamPort
	 */
	public int getStreamPort() {
		return streamPort;
	}

	/**
	 * @param streamPort
	 *            the streamPort to set
	 */
	public void setStreamPort(int streamPort) {
		this.streamPort = streamPort;
	}

	/**
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the part
	 */
	public Part getPart() {
		return part;
	}

	/**
	 * @param part
	 *            the part to set
	 */
	public void setPart(Part part) {
		this.part = part;
	}

	/**
	 * @return the scanResponse
	 */
	protected BlockingQueue<String> getScanResponse() {
		return scanResponse;
	}

	/**
	 * Updates from the property service of this module all the related values.
	 */
	protected void updateInitial() {
		String newHost = properties.getValue(Clamd.HOSTNAME);
		if (newHost != null) {
			this.setHostname(newHost);
		} else {
			log.warn("invalid hostname");
		}

		try {
			int newPort = Integer.parseInt(properties.getValue(Clamd.PORT));
			if (newPort > 0) {
				this.setPort(newPort);
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			log.warn("invalid port", e);
		}

		try {
			long newConnectionTimeout = Long.parseLong(properties
					.getValue(Clamd.CONNECTION_TIMEOUT));
			if (newConnectionTimeout > 0) {
				this.setConnectionTimeout(newConnectionTimeout);
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			log.warn("invalid connection timeout", e);
		}

		try {
			long newResponseTimeout = Long.parseLong(properties
					.getValue(Clamd.RESPONSE_TIMEOUT));
			if (newResponseTimeout > 0) {
				this.setReponseTimeout(newResponseTimeout);
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			log.warn("invalid response timeout", e);
		}

		try {
			int newBufferSize = Integer.parseInt(properties
					.getValue(Clamd.BUFFER_SIZE));
			if (newBufferSize > 0) {
				this.setBufferSize(newBufferSize);
			} else {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			log.warn("invalid buffer size", e);
		}

		log.debug(this.toString());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClamAV [bufferSize=" + bufferSize + ", connectionTimeout="
				+ connectionTimeout + ", hostname=" + hostname + ", port="
				+ port + ", reponseTimeout=" + reponseTimeout + "]";
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
