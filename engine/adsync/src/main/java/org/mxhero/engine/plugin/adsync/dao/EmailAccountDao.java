package org.mxhero.engine.plugin.adsync.dao;

import org.mxhero.engine.plugin.adsync.entity.EmailAccount;
import org.mxhero.engine.plugin.adsync.entity.EmailAccountPk;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Modifying;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface EmailAccountDao extends GenericDao<EmailAccount, EmailAccountPk> {


	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from AliasAccount ac where ac.account.id.domainId = :domain and ac.account.id.account = :account")
	void deleteAliasAccountByAccount(@Param("account") String account,@Param("domain") String domain);
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	@Modifying
	@Query("delete from EmailAccount ea where ea.id.account = :account and ea.id.domainId = :domain")
	void deleteAccount(@Param("account") String account,
							@Param("domain") String domain);
}
