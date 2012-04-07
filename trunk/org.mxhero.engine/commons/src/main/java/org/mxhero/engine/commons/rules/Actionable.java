package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.mail.api.Mail;

/**
 * 
 * @author mmarmol
 */
public interface Actionable {

	/**
	 * @param mail
	 */
	public void exec(Mail mail);
}
