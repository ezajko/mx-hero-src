package org.mxhero.engine.plugin.dbfinder.internal.translator;

import java.util.ArrayList;

import org.mxhero.engine.domain.mail.business.Group;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbAlias;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbUser;

public abstract class UserTranslate {

	public static User translate(DbUser entity){
		
		if(entity==null){
			return null;
		}
		
		User user = internal(entity);
		
		user.setDomain(DomainTranslator.translate(entity.getDomain()));
		if(entity.getGroup()!=null){
			Group group = new Group();
			group.setName(entity.getGroup().getId()+entity.getGroup().getName());
			group.setAliases(new ArrayList<String>());
			group.setMails(new ArrayList<String>());
			for(DbUser dbUser : entity.getGroup().getUsers()){
				group.getMails().add(dbUser.getAccount()+"@"+entity.getGroup().getDomain().getDomain());
				group.getAliases().add(dbUser.getAccount()+"@"+entity.getGroup().getDomain().getDomain());
				for(String alias : user.getDomain().getAliases()){
					group.getAliases().add(dbUser.getAccount()+"@"+alias);
				}
			}
		}
		return user;
	}
	
	
	private static User internal(DbUser entity){

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
