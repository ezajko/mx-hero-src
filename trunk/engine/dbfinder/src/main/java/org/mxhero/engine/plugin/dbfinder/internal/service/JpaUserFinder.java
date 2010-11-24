package org.mxhero.engine.plugin.dbfinder.internal.service;

import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.plugin.dbfinder.internal.dao.DbDomainDao;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbUser;
import org.springframework.transaction.annotation.Transactional;

import static org.mxhero.engine.plugin.dbfinder.internal.translator.UserTranslate.translate;

@Transactional(readOnly=true)
public class JpaUserFinder implements UserFinder{

	private DbDomainDao dbDomainDao;
	
	public User getUser(String mailAdress, String domainId) {
		
		if(mailAdress==null || mailAdress.trim().isEmpty() ||
			domainId==null || domainId.trim().isEmpty()){
			return null;
		}
		DbUser user = dbDomainDao.findUserByDomainAndAccount(domainId, mailAdress.trim().toLowerCase().split("@")[0]);
		
		return translate(user);
	}

	/**
	 * @return the dbDomainDao
	 */
	public DbDomainDao getDbDomainDao() {
		return dbDomainDao;
	}

	/**
	 * @param dbDomainDao the dbDomainDao to set
	 */
	public void setDbDomainDao(DbDomainDao dbDomainDao) {
		this.dbDomainDao = dbDomainDao;
	}

}
