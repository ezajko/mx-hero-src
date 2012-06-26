package org.mxhero.engine.plugin.attachmentlink.fileserver.service;

import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.mxhero.engine.plugin.attachmentlink.fileserver.exceptions.NotAllowedToSeeContentException;

public interface ContentService {
	
	public ContentDTO getContent(Long idMessageAttach)throws NotAllowedToSeeContentException;
	
	public void successContent(Long idMessageAttach);

	public void failDownload(Long idToSearch);
	
	public void unsubscribe(Long messageId);
	
	public List<ContentDTO> getContentList(Long messageId);

}
