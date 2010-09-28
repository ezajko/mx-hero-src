package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface ApplicationUserDao extends GenericDao<ApplicationUser, Integer>{
	
	@Query("From User u WHERE u.userName = :userName")
	ApplicationUser finbByUserName(@Param("userName") String userName);
	
}
