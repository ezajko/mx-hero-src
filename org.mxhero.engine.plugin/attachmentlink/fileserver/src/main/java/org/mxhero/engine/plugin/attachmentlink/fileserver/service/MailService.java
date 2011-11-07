package org.mxhero.engine.plugin.attachmentlink.fileserver.service;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;

public interface MailService {

	void sendMailToSender(ContentDTO content);

}
