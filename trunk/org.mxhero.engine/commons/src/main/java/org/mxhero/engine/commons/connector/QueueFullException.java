package org.mxhero.engine.commons.connector;

public class QueueFullException extends Exception {

	private static final long serialVersionUID = 8681475537754396981L;

	public QueueFullException() {
		super();
	}

	public QueueFullException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueueFullException(String message) {
		super(message);
	}

	public QueueFullException(Throwable cause) {
		super(cause);
	}

	
}
