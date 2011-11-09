package org.mxhero.engine.plugin.dbfinder.internal.service;

import java.util.HashSet;

import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;
import org.mxhero.engine.plugin.dbfinder.internal.repository.UserRepository;
import org.mxhero.engine.plugin.dbfinder.internal.util.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappedUserFinder implements UserFinder
{
	private static Logger log = LoggerFactory.getLogger(MappedUserFinder.class);
	private UserRepository repository;
	
	@Override
	public User getUser(String mailAdress) {
		String formatedMail = null;
		User user = null;
		Domain userDomain = null;
		if(mailAdress==null || mailAdress.trim().isEmpty() ||
				mailAdress.split("@").length!=2){
			return null;
		}
		formatedMail = mailAdress.trim().toLowerCase();
		String domain = formatedMail.split("@")[1];
		
		user=Cloner.clone(repository.getUsers().get(formatedMail));
		
		if(user==null){
			userDomain=Cloner.clone(repository.getDomains().get(domain));
			if(userDomain==null){
				userDomain = new Domain();
				userDomain.setId(domain);
				userDomain.setManaged(false);
				userDomain.setAliases(new HashSet<String>());
				userDomain.getAliases().add(domain);
			}
			user=new User();
			user.setMail(formatedMail);
			user.setManaged(false);
			user.setDomain(userDomain);
			user.setAliases(new HashSet<String>());
			user.getAliases().add(mailAdress);
		}
		if(log.isDebugEnabled()){
			log.debug("found "+user.toString()+" and "+user.getDomain().toString());
		}
		return user;
	}

	public UserRepository getRepository() {
		return repository;
	}

	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}

}
