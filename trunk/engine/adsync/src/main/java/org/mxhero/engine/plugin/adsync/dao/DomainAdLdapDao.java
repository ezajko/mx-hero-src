package org.mxhero.engine.plugin.adsync.dao;

import java.util.Calendar;
import java.util.List;

import org.mxhero.engine.plugin.adsync.entity.DomainAdLdap;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface DomainAdLdapDao extends GenericDao<DomainAdLdap, String>{

	@Query("SELECT d.domain.domain FROM DomainAdLdap d WHERE d.nextUpdate<= :time")
	List<String> findDomainsToSync(@Param("time")Calendar time);
	
}
