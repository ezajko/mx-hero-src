package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.FeatureRule;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface FeatureRuleDao extends GenericDao<FeatureRule, Integer>{
	
	@Query("From FeatureRule fr WHERE fr.feature.id = :featureId AND fr.domain.id = :domainId")
	List<FeatureRule> findByFeatureIdAndDomainId(@Param("featureId") Integer featureId, 
										@Param("domainId") Integer domainId);
	
	@Query("From FeatureRule fr WHERE fr.feature.id = :featureId AND fr.domain is null")
	List<FeatureRule> findByFeatureIdAndNullDomain(@Param("featureId") Integer featureId);
	
	@Query("SELECT distinct frd.rule FROM FeatureRuleDirection frd WHERE frd.directionType = :directionType AND frd.valueId = :valueId")
	List<FeatureRule> findByDirectionTypeAndValueId(@Param("directionType") String directionType,
													@Param("valueId") Integer valueId);
}
