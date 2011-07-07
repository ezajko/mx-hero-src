package org.mxhero.engine.plugin.dbfinder.internal.service;

import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.plugin.dbfinder.internal.dao.EmailAccountDao;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBEmailAccount;

import static org.mxhero.engine.plugin.dbfinder.internal.translator.UserTranslate.translate;

public class JpaUserFinder implements UserFinder{

	private EmailAccountDao dao;

	public User getUser(String mailAdress) {
		
		if(mailAdress==null || mailAdress.trim().isEmpty() ||
				mailAdress.split("@").length!=2){
			return null;
		}
		String account = mailAdress.trim().toLowerCase().split("@")[0];
		String domain = mailAdress.trim().toLowerCase().split("@")[1];
		
		DBEmailAccount user = dao.findEmailAccount(account, domain);
		
		return translate(user);
	}

	public EmailAccountDao getDao() {
		return dao;
	}

	public void setDao(EmailAccountDao dao) {
		this.dao = dao;
	}

}
