package org.mxhero.engine.plugin.attachmentlink.fileserver.service;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.mxhero.engine.plugin.attachmentlink.fileserver.exceptions.NotAllowedToSeeContentException;

public interface ContentService {
	
	public ContentDTO getContent(Long idMessageAttach)throws NotAllowedToSeeContentException;
	
	public void successContent(Long idMessageAttach);

	public void failDownload(Long idToSearch);

}
