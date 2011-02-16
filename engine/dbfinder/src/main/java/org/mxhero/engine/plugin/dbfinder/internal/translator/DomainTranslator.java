package org.mxhero.engine.plugin.dbfinder.internal.translator;

import java.util.ArrayList;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.Group;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbAlias;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbDomain;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbGroup;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbUser;

public abstract class DomainTranslator {

	public static Domain translate(DbDomain entity){

		if(entity==null){
			return null;
		}

		Domain domain = new Domain();
		domain.setId(entity.getDomain());
		//ALIASES
		domain.setAliases(new ArrayList<String>());
		domain.setManaged(true);
		for(DbAlias dbAlias : entity.getAliases()){
			domain.getAliases().add(dbAlias.getAlias());
		}
		//GROUPS
		domain.setGroups(new ArrayList<Group>());
		for(DbGroup dbGroup : entity.getGroups()){
			Group group = new Group();
			group.setName(dbGroup.getName());
			group.setAliases(new ArrayList<String>());
			group.setMails(new ArrayList<String>());
			for(DbUser dbUser : dbGroup.getUsers()){
				group.getMails().add(dbUser.getAccount()+"@"+dbGroup.getDomain().getDomain());
				group.getAliases().add(dbUser.getAccount()+"@"+dbGroup.getDomain().getDomain());
				for(String alias : domain.getAliases()){
					group.getAliases().add(dbUser.getAccount()+"@"+alias);
				}
			}
			domain.getGroups().add(group);
		}

		// TODO TRANSLATE LIST
		
		return domain;
	}

}
