package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.mail.business.Mail;

public interface Actionable {

	public void exec(Mail mail);
}
