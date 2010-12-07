package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.EmailAccount;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface EmailAccountDao extends GenericDao<EmailAccount,Integer>{

	@Query("Select ea From EmailAccount ea WHERE ea.domain.id = :domainId")
	List<EmailAccount> finbAllByDomainId(@Param("domainId") Integer domainId);

	@Query("Select ea From EmailAccount ea WHERE ea.group.id = :groupId")
	List<EmailAccount> findAllByGroupId(@Param("groupId") Integer groupId);
	
	@Query("Select ea From EmailAccount ea WHERE ea.domain.id = :domainId AND ea.group IS NULL")
	List<EmailAccount> findAllByDomainIdWithoutGroup(@Param("domainId") Integer domainId);
}
