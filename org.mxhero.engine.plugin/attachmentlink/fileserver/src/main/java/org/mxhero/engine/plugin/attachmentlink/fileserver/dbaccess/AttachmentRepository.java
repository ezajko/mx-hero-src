package org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;

public interface AttachmentRepository {
	
	public ContentDTO getContent(Long idMessageAttachment);
	
	public void saveHistory(Long idMessageAttachment, boolean success);

	public boolean isAllowed(Long idMessageAttach);

	public void accessFirstTime(Long idMessageAttach);
	
}
