package org.mxhero.engine.core.mail.filter;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * Interface used to implement mail processing in the input and output of the core.
 * @author mmarmol
 */
public interface MailFilter {

	/**
	 * Do the actual job
	 * @param mail
	 */
	void process (MimeMail mail);
}
