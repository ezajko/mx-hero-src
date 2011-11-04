package org.mxhero.engine.plugin.dbfinder.internal.util;

import java.util.HashSet;

import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;

public abstract class Cloner {

	public static Domain clone(Domain domain){
		Domain clonedDomain = null;
		if(domain!=null){
			clonedDomain = new Domain();
			clonedDomain.setId(domain.getId());
			clonedDomain.setManaged(domain.getManaged());
			clonedDomain.setAliases(new HashSet<String>());
			if(domain.getAliases()!=null){
				clonedDomain.getAliases().addAll(domain.getAliases());
			}
		}
		return clonedDomain;
	}
	
	public static User clone(User user){
		User clonedUser = null;
			if(user!=null){
				clonedUser = new User();
				clonedUser.setMail(user.getMail());
				clonedUser.setManaged(user.getManaged());
				clonedUser.setGroup(user.getGroup());
				clonedUser.setDomain(clone(user.getDomain()));
				clonedUser.setAliases(new HashSet<String>());
				if(user.getAliases()!=null){
					clonedUser.getAliases().addAll(user.getAliases());
				}
			}
		return clonedUser;
	}
}
