/**
 * 
 */
package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.exception;

/**
 * @author royojp
 *
 */
public class RequeueingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 666051704864267925L;

	/**
	 * 
	 */
	public RequeueingException() {
	}

	/**
	 * @param message
	 */
	public RequeueingException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RequeueingException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RequeueingException(String message, Throwable cause) {
		super(message, cause);
	}

}
