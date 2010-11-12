package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.SystemProperty;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface SystemPropertyDao extends GenericDao<SystemProperty, Integer>{

	@Query("Select sp From SystemProperty sp WHERE sp.key = :key")
	SystemProperty findByKey(@Param("key") String key);
	
}
