package org.mxhero.engine.plugin.dbfinder.internal.translator;

import java.util.HashSet;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBAliasAccount;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBDomain;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBDomainAlias;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBEmailAccount;

public abstract class Utils {

	public static User translate(DBEmailAccount entity){
		
		if(entity==null){
			return null;
		}
		
		User user = new User();
		user.setMail(entity.getId().getAccount()+"@"+entity.getId().getDomainId());
		user.setGroup(entity.getGroup());
		user.setManaged(true);
		user.setAliases(new HashSet<String>());
		if(entity.getAliases()!=null){
			for(DBAliasAccount alias : entity.getAliases()){
				user.getAliases().add(alias.getId().getAccountAlias()+"@"+alias.getId().getDomainAlias());
			}
		}
		user.getAliases().add(entity.getId().getAccount()+"@"+entity.getId().getDomainId());
		Domain domain = new Domain();
		domain.setId(entity.getDomain().getDomain());
		domain.setManaged(true);
		domain.setAliases(new HashSet<String>());
		if(entity.getDomain().getAliases()!=null){
			for(DBDomainAlias domainAlias : entity.getDomain().getAliases()){
				domain.getAliases().add(domainAlias.getAlias());
			}
		}
		user.setDomain(domain);
		return user;
	}
	
	public static Domain translate(DBDomain entity){
		
		if(entity==null){
			return null;
		}
		Domain domain= new Domain();
		domain.setId(entity.getDomain());
		domain.setManaged(true);
		domain.setAliases(new HashSet<String>());
		for(DBDomainAlias alias : entity.getAliases()){
			domain.getAliases().add(alias.getAlias());
		}
		return domain;
	}
}
