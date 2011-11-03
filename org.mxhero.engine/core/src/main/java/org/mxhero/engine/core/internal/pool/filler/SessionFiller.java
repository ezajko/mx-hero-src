package org.mxhero.engine.core.internal.pool.filler;

import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.core.internal.mail.MailVO;

/**
 * Used to set business objects to the KnowledgeSession.
 * @author mmarmol
 */
public interface SessionFiller {

	/**
	 * Actually do the work.
	 * @param session where to set the objects.
	 * @param userfinder used to find the user of the mail;
	 * @param domainFinder used to find the domain of the mail.
	 * @param mail the object is used to construct the actual business objects.
	 * @return returns the agenda for this mail.
	 */
	MailVO fill(UserFinder userfinder, MimeMail mail);
	
}