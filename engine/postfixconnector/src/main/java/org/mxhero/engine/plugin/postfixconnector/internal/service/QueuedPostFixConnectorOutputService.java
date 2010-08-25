package org.mxhero.engine.plugin.postfixconnector.internal.service;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.postfixconnector.queue.OutputQueue;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the OutputService that just adds the mail
 * to the output queue.
 * @author mmarmol
 */
public final class QueuedPostFixConnectorOutputService implements PostFixConnectorOutputService {


	private static Logger log = LoggerFactory.getLogger(QueuedPostFixConnectorOutputService.class);

	/**
	 * Implementation that adds mails to the OutputQueue.
	 * @see
	 * org.mxhero.engine.domain.connector.OutputService#addOutMail(org.mxhero
	 * .engine.domain.mail.Mail)
	 */
	public void addOutMail(MimeMail mail) {
		log.debug("Email received:" + mail);
		OutputQueue.getInstance().add(mail);
		log.debug("Email added to queue:" + mail);
	}

}
