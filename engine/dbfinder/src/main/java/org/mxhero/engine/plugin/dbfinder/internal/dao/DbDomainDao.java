package org.mxhero.engine.plugin.dbfinder.internal.dao;

import org.mxhero.engine.plugin.dbfinder.internal.entity.DbDomain;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DbUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface DbDomainDao extends GenericDao<DbDomain, Integer>{

	@Transactional(readOnly=true,propagation=Propagation.REQUIRES_NEW)
	@Query("From DbDomain d WHERE d.domain = :domain")
	DbDomain finbByDomain(@Param("domain") String domain);
	
	@Query("Select a.domain From DbAlias a WHERE a.alias = :alias")
	DbDomain finbByAlias(@Param("alias") String alias);
	
	@Query("Select u From DbUser u WHERE u.account = :account AND u.domain.domain = :domain")
	DbUser findUserByDomainAndAccount(@Param("domain") String domain,
										@Param("account") String account);
}
