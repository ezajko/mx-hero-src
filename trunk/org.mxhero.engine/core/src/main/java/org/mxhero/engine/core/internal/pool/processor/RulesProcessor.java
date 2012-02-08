package org.mxhero.engine.core.internal.pool.processor;

import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.rules.RuleBase;

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
	 * @param base where the mail is going to be processed.
	 * @param filler who will add the business objects and return the mail agenda.
	 * @param userfinder passed to the filler
	 * @param domainFinder passed to the filler
	 * @param mail 
	 */
	void process(RuleBase base, Mail fact);
}
