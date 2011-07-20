package org.mxhero.engine.plugin.dbfinder.internal.service;

import java.util.HashSet;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.plugin.dbfinder.internal.dao.EmailAccountDao;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBEmailAccount;

import static org.mxhero.engine.plugin.dbfinder.internal.translator.Utils.translate;

public class JpaUserFinder implements UserFinder{

	private EmailAccountDao dao;

	public User getUser(String mailAdress) {
		User user = null;
		Domain userDomain = null;
		if(mailAdress==null || mailAdress.trim().isEmpty() ||
				mailAdress.split("@").length!=2){
			return null;
		}
		String account = mailAdress.trim().toLowerCase().split("@")[0];
		String domain = mailAdress.trim().toLowerCase().split("@")[1];
		
		DBEmailAccount emailAccount = dao.findEmailAccount(account, domain);
		user = translate(emailAccount);
		
		if(user==null){
			userDomain=translate(dao.findDomain(domain));
			if(userDomain==null){
				userDomain = new Domain();
				userDomain.setId(domain);
				userDomain.setManaged(false);
				userDomain.setAliases(new HashSet<String>());
				userDomain.getAliases().add(domain);
			}
			user=new User();
			user.setMail(mailAdress);
			user.setManaged(false);
			user.setDomain(userDomain);
			user.setAliases(new HashSet<String>());
			user.getAliases().add(mailAdress);
		}
		return user;
	}

	public EmailAccountDao getDao() {
		return dao;
	}

	public void setDao(EmailAccountDao dao) {
		this.dao = dao;
	}

}
