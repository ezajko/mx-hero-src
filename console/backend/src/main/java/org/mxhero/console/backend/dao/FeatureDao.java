package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.Feature;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Query;

public interface FeatureDao extends GenericDao<Feature, Integer>{

	@Query("From Feature f WHERE f.category is null")
	List<Feature> findByNullCategory();
	
}
