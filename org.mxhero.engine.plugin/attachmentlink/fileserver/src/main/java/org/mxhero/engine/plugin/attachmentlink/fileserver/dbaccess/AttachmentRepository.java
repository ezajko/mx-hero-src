package org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess;

import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;

public interface AttachmentRepository {
	
	public ContentDTO getContent(Long idMessageAttachment);
	
	public void saveHistory(Long idMessageAttachment, boolean success);

	public boolean isAllowed(Long idMessageAttach);

	public void accessFirstTime(Long idMessageAttach);
	
	public void unsubscribe(Long messageId);
	
	public List<ContentDTO> getContentList(Long messageId, String recipient);
	
}
