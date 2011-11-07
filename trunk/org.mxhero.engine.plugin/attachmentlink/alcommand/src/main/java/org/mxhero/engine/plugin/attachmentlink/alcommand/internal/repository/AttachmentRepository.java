package org.mxhero.engine.plugin.attachmentlink.alcommand.internal.repository;

import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Attach;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.domain.Message;


/**
 * @author royojp
 * @version 1.0
 * @created 22-Aug-2011 12:52:54
 */
public interface AttachmentRepository {

	/**
	 * 
	 * @param attach
	 * 
	 */
	public Message save(Message attach);

	public List<Attach> getAttachmentsForEmail(Message mail);

	public boolean hasBeenProcessed(Message mail);

	public Attach getAttachForChecksum(Attach attach);

}