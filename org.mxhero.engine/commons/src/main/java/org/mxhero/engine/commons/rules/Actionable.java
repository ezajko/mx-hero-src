package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.mail.api.Mail;

public interface Actionable {

	public void exec(Mail mail);
}
