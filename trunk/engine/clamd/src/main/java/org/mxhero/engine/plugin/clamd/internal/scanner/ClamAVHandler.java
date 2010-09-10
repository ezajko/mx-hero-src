package org.mxhero.engine.plugin.clamd.internal.scanner;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler to manage the procol used by the scanAV demon.
 * @author mmarmol
 */
public class ClamAVHandler extends IoHandlerAdapter{

	private static Logger log = LoggerFactory.getLogger(ClamAVHandler.class);
	
	private static final String STREAM_PORT_STRING = "PORT ";
	
	private ClamAV scanner = null;
	
	/**
	 * Basic constructor
	 * @param scanner Need the scanner to call the scan method when STREAM is accepted and port received.
	 */
	public ClamAVHandler(ClamAV scanner){
		this.scanner=scanner;
	}
	
	/**
	 * Send the stream when the port is open, and takes the response to.
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message){
		log.debug("message received: "+message.toString());
		if (message.toString().startsWith(STREAM_PORT_STRING)){
			scanner.setStreamPort(decodePort(message.toString()));
			try {
				scanner.sendStream();
			} catch (Exception e) {
				scanner.getScanResponse().add(e.getMessage());
				log.warn("error",e);
			}
		} else {
			scanner.getScanResponse().add(message.toString());
		}
	}
	
	/**
	 * Just for logging.
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageSent(IoSession session, Object message){
		log.debug("message sent: "+message.toString());
	}
	
	/**
	 * Just for logging.
	 * @see org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.mina.core.session.IoSession, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause){
		log.warn("error",cause);
	}
	
	/**
	 * Used to decode the port.
	 * @param answer the string holding the port.
	 * @return the port number.
	 */
	private int decodePort(String answer) {
		int port = -1;
		if (answer != null && answer.startsWith(STREAM_PORT_STRING)) {
			try {
				port = Integer.parseInt(answer.substring(STREAM_PORT_STRING
						.length()));
			} catch (NumberFormatException nfe) {
				log.warn("wrong format for port:" + answer);
			}
		}
		log.debug("port parsed:" + port);
		return port;
	}
}
