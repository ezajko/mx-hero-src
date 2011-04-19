package org.mxhero.engine.domain.filter;

import org.mxhero.engine.domain.mail.business.User;

public interface PreFilter {

	boolean filter(User sender, User recipient);

}
