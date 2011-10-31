package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.mail.business.Mail;

public interface Evaluable {

	public boolean eval(Mail mail);
}
