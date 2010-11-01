package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.EmailAccount;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

public interface EmailAccountDao extends GenericDao<EmailAccount,Integer>{
	
	@Query("Select ea From EmailAccount ea WHERE ea.domain.id = :domainId")
	List<EmailAccount> finbAllByDomainId(@Param("domainId") Integer domainId);
	

	@Query("Select ea From EmailAccount ea WHERE ea.domain.id = :domainId")
	Page<EmailAccount> finbAllByDomainId(@Param("domainId") Integer domainId, Pageable pageable);

}
