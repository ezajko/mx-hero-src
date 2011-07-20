package org.mxhero.engine.plugin.dbfinder.internal.dao;

import org.mxhero.engine.plugin.dbfinder.internal.entity.DBDomain;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBEmailAccount;
import org.mxhero.engine.plugin.dbfinder.internal.entity.DBEmailAccountPk;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface EmailAccountDao extends GenericDao<DBEmailAccount, DBEmailAccountPk>{

	@Query("SELECT ac.account FROM DBAliasAccount ac WHERE ac.id.accountAlias=:account AND ac.id.domainAlias=:domain")
	DBEmailAccount findEmailAccount(@Param("account")String account, @Param("domain")String domain);
	
	@Query("SELECT da.domain FROM DBDomainAlias da WHERE da.alias=:domain")
	DBDomain findDomain(@Param("domain")String domain);
}
