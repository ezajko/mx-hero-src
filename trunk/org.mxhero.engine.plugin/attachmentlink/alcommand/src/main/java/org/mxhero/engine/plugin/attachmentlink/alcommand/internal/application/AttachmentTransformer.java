package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.application;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;

/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:54
 */
public interface AttachmentTransformer {

	/**
	 * 
	 * @param mail
	 */
	public void transformAttachments(Message mail);

}