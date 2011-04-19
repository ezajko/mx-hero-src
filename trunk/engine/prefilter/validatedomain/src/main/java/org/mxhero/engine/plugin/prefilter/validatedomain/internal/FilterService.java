package org.mxhero.engine.plugin.prefilter.validatedomain.internal;

import org.mxhero.engine.domain.filter.PreFilter;
import org.mxhero.engine.domain.mail.business.User;

public class FilterService implements PreFilter{

	@Override
	public boolean filter(User sender, User recipient) {
		if(sender.getManaged() || recipient.getManaged()){
			//if the sender or recipient are accounts inside mxHero, do not filter the email.
			return false;
		}
		return true;
	}

}
