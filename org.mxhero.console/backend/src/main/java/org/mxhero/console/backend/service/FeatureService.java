package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.CategoryVO;

public interface FeatureService {

	Collection<CategoryVO> findFeatures();
	
	Collection<CategoryVO> findFeaturesByDomainId(String domainId);
	
}
