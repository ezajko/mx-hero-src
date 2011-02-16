package org.mxhero.engine.plugin.dbfinder.internal.translator;

import java.util.ArrayList;

import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbAlias;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbUser;

public abstract class UserTranslate {

	public static User translate(DbUser entity){
		
		if(entity==null){
			return null;
		}
		
		User user = new User();
		
		user.setMail(entity.getAccount()+"@"+entity.getDomain().getDomain());
		user.setAliases(new ArrayList<String>());
		user.getAliases().add(user.getMail());
		user.setManaged(true);
		for(DbAlias dbAlias : entity.getDomain().getAliases()){
			user.getAliases().add(entity.getAccount()+"@"+dbAlias.getAlias());
		}
		// TODO implement LISTS
		return user;
	}
	
}
