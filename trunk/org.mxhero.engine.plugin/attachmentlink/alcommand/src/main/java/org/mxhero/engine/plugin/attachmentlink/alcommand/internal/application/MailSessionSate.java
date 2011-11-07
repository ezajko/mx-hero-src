package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:54
 */
public interface MailSessionSate {

	/**
	 * 
	 * @param mail
	 */
	public void finishProcessing(Message mail);

	/**
	 * 
	 * @param mail
	 */
	public boolean isBeenProcessing(Message mail);

	/**
	 * 
	 * @param mail
	 */
	public void startProcessing(Message mail);

	/**
	 * 
	 * @param mail 
	 * @return
	 */
	public boolean hasBeenProcessed(Message mail);

}