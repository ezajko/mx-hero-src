package org.mxhero.console.backend.service.jdbc;

import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.repository.FeatureRepository;
import org.mxhero.console.backend.repository.FeatureRuleRepository;
import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.vo.CategoryVO;
import org.mxhero.console.backend.vo.FeatureVO;
import org.mxhero.console.backend.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("jdbcFeatureService")
public class JdbcFeatureService implements FeatureService {

	private static final String UNCLASSIFIED_ID = "feature.category.unclassified.id";
		
	private SystemPropertyRepository propertyRepository;
	
	private FeatureRepository featureRepository;
	
	private FeatureRuleRepository ruleRepository;

	@Autowired(required=true)
	public JdbcFeatureService(SystemPropertyRepository propertyRepository,
			FeatureRepository featureRepository,
			FeatureRuleRepository ruleRepository) {
		super();
		this.propertyRepository = propertyRepository;
		this.featureRepository = featureRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public Collection<CategoryVO> findFeatures() {
		SystemPropertyVO property = propertyRepository.findById(UNCLASSIFIED_ID);
		Integer unclassifiedCategoryId = null;
		
		if(property!=null){
			unclassifiedCategoryId = Integer.parseInt(property.getPropertyValue());
		}
		List<CategoryVO> categories =  featureRepository.findAll();
				
		CategoryVO unclassifiedCategory = null;
		for(CategoryVO category : categories){
			if(category.getId().equals(unclassifiedCategoryId)){
				unclassifiedCategory = category;
				category.setChilds(featureRepository.findFeatures(null));
				for(FeatureVO feature : category.getChilds()){
					feature.setRules( ruleRepository.findWitNullDomain(feature.getId()));
				}
			} else {
				category.setChilds(featureRepository.findFeatures(category.getId()));
				for(FeatureVO feature : category.getChilds()){
					feature.setRules( ruleRepository.findWitNullDomain(feature.getId()));
				}
			}
		}

		if(unclassifiedCategory!=null && (unclassifiedCategory.getChilds()==null || unclassifiedCategory.getChilds().size()<1)){
			categories.remove(unclassifiedCategory);
		}
		return categories;
	}

	@Override
	public Collection<CategoryVO> findFeaturesByDomainId(String domainId) {
		SystemPropertyVO property = propertyRepository.findById(UNCLASSIFIED_ID);
		Integer unclassifiedCategoryId = null;
		
		if(property!=null){
			unclassifiedCategoryId = Integer.parseInt(property.getPropertyValue());
		}
		List<CategoryVO> categories =  featureRepository.findAll();
		
		CategoryVO unclassifiedCategory = null;	
		for(CategoryVO category : categories){
			if(category.getId().equals(unclassifiedCategoryId)){
				unclassifiedCategory = category;
				category.setChilds(featureRepository.findFeatures(null));
				for(FeatureVO feature : category.getChilds()){
					feature.setRules( ruleRepository.findByDomainId(domainId, feature.getId()));
				}
			} else {
				category.setChilds(featureRepository.findFeatures(category.getId()));
				for(FeatureVO feature : category.getChilds()){
					feature.setRules( ruleRepository.findByDomainId(domainId, feature.getId()));
				}
			}
		}
		if(unclassifiedCategory!=null && (unclassifiedCategory.getChilds()==null || unclassifiedCategory.getChilds().size()<1)){
			categories.remove(unclassifiedCategory);
		}
		return categories;
	}

}
