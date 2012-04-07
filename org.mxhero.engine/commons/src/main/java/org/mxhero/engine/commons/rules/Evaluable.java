package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.mail.api.Mail;

/**
 * @author mxhero
 *
 */
public interface Evaluable {

	/**
	 * @param mail
	 * @return
	 */
	public boolean eval(Mail mail);
}
