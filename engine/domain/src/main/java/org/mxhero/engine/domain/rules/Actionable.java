package org.mxhero.engine.domain.rules;

import org.mxhero.engine.domain.mail.business.Mail;

public interface Actionable {

	public void exec(Mail mail);
}
