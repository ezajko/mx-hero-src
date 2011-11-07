package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.MessageAttachRecipient;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:54
 */
public interface LinkManager {

	/**
	 * 
	 * @param msg
	 */
	public String createLink(MessageAttachRecipient msg);

}