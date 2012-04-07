package org.mxhero.engine.commons.connector;

/**
 * @author mmarmol
 *
 */
public class QueueFullException extends Exception {

	private static final long serialVersionUID = 8681475537754396981L;

	/**
	 * 
	 */
	public QueueFullException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public QueueFullException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public QueueFullException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public QueueFullException(Throwable cause) {
		super(cause);
	}

	
}
