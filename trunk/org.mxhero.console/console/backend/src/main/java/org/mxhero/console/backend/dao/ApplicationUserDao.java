package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface ApplicationUserDao extends GenericDao<ApplicationUser, Integer>{
	
	@Query("From ApplicationUser u WHERE u.userName = :userName")
	ApplicationUser finbByUserName(@Param("userName") String userName);
	
	@Query("From ApplicationUser u WHERE u.notifyEmail = :notifyEmail")
	ApplicationUser finbByNotifyEmail(@Param("notifyEmail") String notifyEmail);
}
