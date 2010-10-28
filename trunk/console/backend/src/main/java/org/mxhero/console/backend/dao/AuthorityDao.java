package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.Authority;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface AuthorityDao extends GenericDao<Authority, Integer> {

	@Query("From Authority a WHERE a.authority = :authority")
	Authority finbByAuthority(@Param("authority") String authority);
	
}
