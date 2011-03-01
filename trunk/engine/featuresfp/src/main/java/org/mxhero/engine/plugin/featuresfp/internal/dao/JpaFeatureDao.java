package org.mxhero.engine.plugin.featuresfp.internal.dao;

import org.mxhero.engine.plugin.featuresfp.internal.entity.JpaFeature;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;


public interface JpaFeatureDao extends GenericDao<Integer, JpaFeature>{

	@Query("Select f From JpaFeature f WHERE f.component = :component AND f.version = :version")
	JpaFeature findRulesByFeature(@Param("component") String component,
										@Param("version") Integer version);
}
