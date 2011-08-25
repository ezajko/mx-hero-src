package org.mxhero.engine.domain.rules;

import org.mxhero.engine.domain.mail.business.Mail;

public interface Evaluable {

	public boolean eval(Mail mail);
}
