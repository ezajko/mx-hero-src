package org.mxhero.engine.plugin.adsync.dao;

import java.util.List;

import org.mxhero.engine.plugin.adsync.entity.FeatureRule;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface FeatureRuleDao extends GenericDao<FeatureRule, Integer>{

	@Query("SELECT DISTINCT fr FROM FeatureRule fr WHERE (fr.fromDirection.domain = :domain AND fr.fromDirection.account = :account AND fr.fromDirection.directionType = 'individual') OR (fr.toDirection.domain = :domain AND fr.toDirection.account = :account AND fr.toDirection.directionType = 'individual')")
	List<FeatureRule> findRulesByAccount(@Param("account") String account,@Param("domain") String domain);
	
}
