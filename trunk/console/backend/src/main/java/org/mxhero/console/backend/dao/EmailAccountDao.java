package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.EmailAccountPk;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Modifying;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface EmailAccountDao extends GenericDao<EmailAccount,EmailAccountPk>{

	@Query("Select ea From EmailAccount ea WHERE ea.id.domainId = :domainId order by ea.id.account")
	List<EmailAccount> finbAllByDomainId(@Param("domainId") String domainId);

	
	@Query("Select ea From EmailAccount ea WHERE ea.id.domainId = :domainId AND ea.id.account like :accountLike order by ea.id.account")
	List<EmailAccount> finbAllByDomainIdAndAccoundLike(@Param("domainId") String domainId,
														@Param("accountLike") String accountLike);

	@Query("Select ea From EmailAccount ea WHERE ea.id.domainId = :domainId AND ea.id.account like :accountLike AND ea.group.id.name = :groupName order by ea.id.account")
	List<EmailAccount> finbAllByDomainIdAndAccoundLikeAndGroup(@Param("domainId") String domainId,
																@Param("accountLike") String accountLike,
																@Param("groupName") String groupName);

	
	@Query("Select ea From EmailAccount ea WHERE ea.group.id.name = :groupId AND ea.group.id.domainId = :domainId order by ea.id.account")
	List<EmailAccount> findAllByGroupId(@Param("groupId") String groupId,@Param("domainId") String domainId);
	
	@Query("Select ea From EmailAccount ea WHERE ea.id.domainId = :domainId AND ea.group IS NULL order by ea.id.account")
	List<EmailAccount> findAllByDomainIdWithoutGroup(@Param("domainId") String domainId);
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from EmailAccount ea where ea.id.account = :account and ea.id.domainId = :domain")
	void deleteAccount(@Param("account") String account,
							@Param("domain") String domain);
}
