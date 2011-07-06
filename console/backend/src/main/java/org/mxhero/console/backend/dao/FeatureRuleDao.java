package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.FeatureRule;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface FeatureRuleDao extends GenericDao<FeatureRule, Integer>{
	
	@Query("From FeatureRule fr WHERE fr.feature.id = :featureId AND fr.domain.domain = :domainId")
	List<FeatureRule> findByFeatureIdAndDomainId(@Param("featureId") Integer featureId, 
										@Param("domainId") String domainId);
	
	@Query("From FeatureRule fr WHERE fr.feature.id = :featureId AND fr.domain is null")
	List<FeatureRule> findByFeatureIdAndNullDomain(@Param("featureId") Integer featureId);
	
	@Query("SELECT distinct frd.rule FROM FeatureRuleDirection frd WHERE frd.directionType = :directionType AND frd.domain = :domainId")
	List<FeatureRule> findByDirectionTypeDomainId(@Param("directionType") String directionType,
													@Param("domainId") String domainId);


	@Query("SELECT distinct frd.rule FROM FeatureRuleDirection frd WHERE frd.directionType = :directionType AND frd.domain = :domainId AND frd.account = :accountId")
	List<FeatureRule> findByDirectionTypeDomainIdAccountId(@Param("directionType") String directionType,
													@Param("domainId") String domainId,
													@Param("accountId") String accountId);


	@Query("SELECT distinct frd.rule FROM FeatureRuleDirection frd WHERE frd.directionType = :directionType AND frd.domain = :domainId AND frd.group = :groupId")
	List<FeatureRule> findByDirectionTypeAndDomainIdAndGroupId(@Param("directionType") String directionType,
													@Param("domainId") String domainId,
													@Param("groupId") String groupId);

	
	
	@Query("SELECT r FROM FeatureRule r " +
			" WHERE (r.fromDirection.freeValue = :fromFreeValue OR (r.fromDirection.freeValue = 'anyoneelse' AND :fromFreeValue = 'anyone') OR (r.fromDirection.freeValue = 'anyone' AND :fromFreeValue = 'anyoneelse') )" +
			" AND (r.toDirection.freeValue = :toFreeValue OR (r.toDirection.freeValue = 'anyoneelse' AND :toFreeValue = 'anyone') OR (r.toDirection.freeValue = 'anyone' AND :toFreeValue = 'anyoneelse') ) " +
			" AND r.feature.id = :featureId " +
			" AND r.domain.domain = :domainId ")
	List<FeatureRule> findCheckCreation(@Param("fromFreeValue") String fromFreeValue,
										@Param("toFreeValue") String toFreeValue,
										@Param("featureId") Integer featureId,
										@Param("domainId") String domainId);
	
	@Query("SELECT r FROM FeatureRule r " +
			" WHERE (r.fromDirection.freeValue = :fromFreeValue OR (r.fromDirection.freeValue = 'anyoneelse' AND :fromFreeValue = 'anyone') OR (r.fromDirection.freeValue = 'anyone' AND :fromFreeValue = 'anyoneelse') )" +
			" AND (r.toDirection.freeValue = :toFreeValue OR (r.toDirection.freeValue = 'anyoneelse' AND :toFreeValue = 'anyone') OR (r.toDirection.freeValue = 'anyone' AND :toFreeValue = 'anyoneelse') ) " +
			" AND r.feature.id = :featureId " +
			" AND r.domain IS NULL ")
	List<FeatureRule> findCheckCreationAdmin(@Param("fromFreeValue") String fromFreeValue,
										@Param("toFreeValue") String toFreeValue,
										@Param("featureId") Integer featureId);
	
}
