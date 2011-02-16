package org.mxhero.engine.plugin.featuresfp.internal.service;

import org.mxhero.engine.domain.feature.Feature;
import org.mxhero.engine.domain.feature.RulesFinder;
import org.mxhero.engine.plugin.featuresfp.internal.dao.JpaFeatureDao;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class JpaRulesFinder implements RulesFinder{

	
	private JpaFeatureDao featureDao;
	
	@Override
	public Feature find(String component, Integer version) {
		return featureDao.findRulesByFeature(component, version);
	}

	public JpaFeatureDao getFeatureDao() {
		return featureDao;
	}

	public void setFeatureDao(JpaFeatureDao featureDao) {
		this.featureDao = featureDao;
	}

}
