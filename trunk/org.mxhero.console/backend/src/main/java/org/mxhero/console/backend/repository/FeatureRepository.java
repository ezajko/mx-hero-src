package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.CategoryVO;
import org.mxhero.console.backend.vo.FeatureVO;

public interface FeatureRepository {

	FeatureVO findById(Integer featureId);
	
	List<FeatureVO> findFeatures(Integer categoryId);
	
	List<CategoryVO> findAll();
}
