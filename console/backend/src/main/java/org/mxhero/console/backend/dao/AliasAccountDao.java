package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.AliasAccount;
import org.mxhero.console.backend.entity.AliasAccountPk;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Modifying;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface AliasAccountDao extends GenericDao<AliasAccount, AliasAccountPk>{

	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from AliasAccount ac where ac.id.domainAlias = :alias ")
	void deleteAliasAccountByDomainAlias(@Param("alias") String alias);
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from AliasAccount ac where ac.account.id.domainId = :domain ")
	void deleteAliasAccountByDomain(@Param("domain") String domain);
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from AliasAccount ac where ac.id.domainAlias = :domain and ac.id.accountAlias = :alias")
	void deleteAliasAccountById(@Param("alias") String alias,@Param("domain") String domain);
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from AliasAccount ac where ac.account.id.domainId = :domain and ac.account.id.account = :account")
	void deleteAliasAccountByAccount(@Param("account") String account,@Param("domain") String domain);
}
