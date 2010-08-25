package org.mxhero.engine.core.internal.pool.processor;

import org.drools.runtime.StatefulKnowledgeSession;
import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;

/**
 * Interface for the classes that implements the responsibility of processing
 * mails based in the base.
 * 
 * @author mmarmol
 * 
 */
public interface RulesProcessor {

	/**
	 * Actually do the job
	 * @param session where the mail is going to be processed.
	 * @param filler who will add the business objects and return the mail agenda.
	 * @param userfinder passed to the filler
	 * @param domainFinder passed to the filler
	 * @param mail 
	 */
	void process(StatefulKnowledgeSession session, SessionFiller filler, UserFinder userfinder, DomainFinder domainFinder, MimeMail mail);
}
