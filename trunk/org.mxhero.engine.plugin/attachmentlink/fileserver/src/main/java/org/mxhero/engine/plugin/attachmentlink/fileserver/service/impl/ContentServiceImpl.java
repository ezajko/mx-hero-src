package org.mxhero.engine.plugin.attachmentlink.fileserver.service.impl;

import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess.AttachmentRepository;
import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.mxhero.engine.plugin.attachmentlink.fileserver.exceptions.NotAllowedToSeeContentException;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.ContentService;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private AttachmentRepository repository;

	@Autowired
	private MailService mailer;
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public void failDownload(Long idToSearch) {
		repository.saveHistory(idToSearch, false);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public ContentDTO getContent(Long idMessageAttach, String email)
			throws NotAllowedToSeeContentException {
		if(!repository.isAllowed(idMessageAttach)){
			throw new NotAllowedToSeeContentException();
		}
		ContentDTO content = repository.getContent(idMessageAttach);
		if(!content.wasAccessed()){
			repository.accessFirstTime(idMessageAttach);
			if(content.isProcessMsg()){
				mailer.sendMailToSender(content);
			}
		}
		String url = repository.getPulicUrl(idMessageAttach, email);
		content.setPublicUrl(url);
		return content;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public void successContent(Long idMessageAttach) {
		repository.saveHistory(idMessageAttach,true);
	}

	@Override
	public void unsubscribe(Long messageId) {
		repository.unsubscribe(messageId);
	}

	@Override
	public List<ContentDTO> getContentList(Long messageId, String recipient) {
		List<ContentDTO> contents = repository.getContentList(messageId,recipient);
		boolean notifyAll = false;
		ContentDTO lastContent = null;
		if(contents!=null){
			for(ContentDTO content : contents){
				if(!content.wasAccessed()){
					repository.accessFirstTime(content.getIdMessageAttach());
					if(content.isProcessMsg()){
						notifyAll=true;
						lastContent = content;
					}
				}
			}
		}
		if(notifyAll){
			String lastContentFileName = lastContent.getFileName();
			lastContent.setFileName("allFiles.zip");
			mailer.sendMailToSender(lastContent);
			lastContent.setFileName(lastContentFileName);
		}
		return contents;
	}

}
