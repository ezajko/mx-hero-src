package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.Domain;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface DomainDao extends GenericDao<Domain, Integer> {

	@Query("From Domain d WHERE d.domain = :domain order by d.domain")
	Domain finbByDomain(@Param("domain") String domain);
	
}
