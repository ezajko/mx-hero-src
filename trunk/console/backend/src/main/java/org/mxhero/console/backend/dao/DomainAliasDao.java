package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.DomainAlias;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Modifying;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;


public interface DomainAliasDao extends GenericDao<DomainAlias, String>{

	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from DomainAlias da where da.alias = :alias and da.domain.domain = :domain")
	void deleteDomainAlias(@Param("alias") String alias,
							@Param("domain") String domain);
	
}
