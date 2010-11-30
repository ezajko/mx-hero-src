package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.CategoryVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FeatureService {

	@Secured("ROLE_ADMIN")
	Collection<CategoryVO> findFeatures();
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<CategoryVO> findFeaturesByDomainId(Integer domainId);
}
