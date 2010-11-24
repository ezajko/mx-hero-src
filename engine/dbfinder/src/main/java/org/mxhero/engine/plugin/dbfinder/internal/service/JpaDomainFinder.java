package org.mxhero.engine.plugin.dbfinder.internal.service;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.plugin.dbfinder.internal.dao.DbDomainDao;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbDomain;
import org.springframework.transaction.annotation.Transactional;

import static org.mxhero.engine.plugin.dbfinder.internal.translator.DomainTranslator.translate;

@Transactional(readOnly=true)
public class JpaDomainFinder implements DomainFinder{

	private DbDomainDao dbDomainDao;
	
	public Domain getDomain(String domainId) {
		if(domainId==null || domainId.trim().isEmpty()){
			return null;
		}
		
		DbDomain domain = dbDomainDao.finbByDomain(domainId.trim().toLowerCase());
		if(domain==null){
			domain = dbDomainDao.finbByAlias(domainId.trim().toLowerCase());
		}
		
		return translate(domain);
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
