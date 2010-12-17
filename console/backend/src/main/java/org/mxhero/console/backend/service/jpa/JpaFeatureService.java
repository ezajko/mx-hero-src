package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.dao.CategoryDao;
import org.mxhero.console.backend.dao.FeatureDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.Category;
import org.mxhero.console.backend.entity.Feature;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.SystemProperty;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.translator.FeatureRuleTranslator;
import org.mxhero.console.backend.vo.CategoryVO;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.mxhero.console.backend.vo.FeatureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("featureService")
@RemotingDestination(channels={"flex-amf"})
public class JpaFeatureService implements FeatureService {

	private static final String UNCLASSIFIED_ID = "feature.category.unclassified.id";
	
	private static final String DEFAULT_LANGUAGE="default.user.language";
	
	private CategoryDao categoryDao;
	
	private FeatureDao featureDao;
	
	private FeatureRuleDao featureRuleDao;
	
	private LocalePropertyHelper helper;
	
	private SystemPropertyDao systemPropertyDao;
	
	private ApplicationUserDao userDao;
	
	private FeatureRuleTranslator ruleTranslator;
	
	@Autowired
	public JpaFeatureService(CategoryDao categoryDao, FeatureDao featureDao,
			FeatureRuleDao featureRuleDao, LocalePropertyHelper helper,
			SystemPropertyDao systemPropertyDao,
			ApplicationUserDao userDao,
			FeatureRuleTranslator ruleTranslator) {
		super();
		this.categoryDao = categoryDao;
		this.featureDao = featureDao;
		this.featureRuleDao = featureRuleDao;
		this.helper = helper;
		this.systemPropertyDao = systemPropertyDao;
		this.userDao = userDao;
		this.ruleTranslator = ruleTranslator;
	}

	@Override
	public Collection<CategoryVO> findFeatures() {
		SystemProperty property = systemPropertyDao.findByKey(UNCLASSIFIED_ID);
		Integer unclassifiedCategoryId = null;
		
		if(property!=null){
			unclassifiedCategoryId = Integer.parseInt(property.getPropertyValue());
		}
		List<Category> categories = categoryDao.readAll();
				
		for(Category category : categories){
			if(category.getId().equals(unclassifiedCategoryId)){
				category.setFeatures(new HashSet<Feature>());
				for(Feature feature : featureDao.findByNullCategory()){
					category.getFeatures().add(feature);
					feature.setRules(new HashSet<FeatureRule>(featureRuleDao.findByFeatureIdAndNullDomain(feature.getId())));
				}
			} else {
				for(Feature feature : category.getFeatures()){
					feature.setRules(new HashSet<FeatureRule>(featureRuleDao.findByFeatureIdAndNullDomain(feature.getId())));
				}
			}
		}
		
		return translate(categories);
	}

	@Override
	public Collection<CategoryVO> findFeaturesByDomainId(Integer domainId) {
		SystemProperty property = systemPropertyDao.findByKey(UNCLASSIFIED_ID);
		Integer unclassifiedCategoryId = null;
		
		if(property!=null){
			unclassifiedCategoryId = Integer.parseInt(property.getPropertyValue());
		}
		List<Category> categories = categoryDao.readAll();
				
		for(Category category : categories){
			if(category.getId().equals(unclassifiedCategoryId)){
				category.setFeatures(new HashSet<Feature>());
				for(Feature feature : featureDao.findByNullCategory()){
					category.getFeatures().add(feature);
					feature.setRules(new HashSet<FeatureRule>(featureRuleDao.findByFeatureIdAndDomainId(feature.getId(), domainId)));
				}
			} else {
				for(Feature feature : category.getFeatures()){
					feature.setRules(new HashSet<FeatureRule>(featureRuleDao.findByFeatureIdAndDomainId(feature.getId(), domainId)));
				}
			}
		}
		
		return translate(categories);
	}

	private Collection<CategoryVO> translate(Collection<Category> entities){
		Collection<CategoryVO> categoryVOs = new ArrayList<CategoryVO>();
		String userLocale = userDao.finbByUserName(SecurityContextHolder.getContext().getAuthentication().getName()).getLocale();
		String defaultLocale = systemPropertyDao.findByKey(DEFAULT_LANGUAGE).getPropertyValue();
		for (Category category : entities){
			if(category.getFeatures()!=null && category.getFeatures().size()>0){
				CategoryVO categoryVO = new CategoryVO();
				categoryVO.setIconsrc(category.getIconSource());
				categoryVO.setId(category.getId());
				categoryVO.setLabel(helper.getValue(Category.class.getName(),category.getLabelKey(),userLocale,defaultLocale));
				categoryVO.setChilds(new ArrayList<FeatureVO>());
				for(Feature feature : category.getFeatures()){
					FeatureVO featureVO = new FeatureVO();
					featureVO.setId(feature.getId());
					featureVO.setModuleUrl(feature.getModuleUrl());
					featureVO.setLabel(helper.getValue(feature.getComponent(),feature.getLabelKey(),userLocale,defaultLocale));
					featureVO.setDescription(helper.getValue(feature.getComponent(),feature.getDescriptionKey(),userLocale,defaultLocale));
					featureVO.setExplain(helper.getValue(feature.getComponent(),feature.getExplainKey(),userLocale,defaultLocale));
					featureVO.setRules(ruleTranslator.translate(feature.getRules()));
					categoryVO.getChilds().add(featureVO);
				}
				categoryVOs.add(categoryVO);
			}
		}
		 
		return categoryVOs;
	}

	@Override
	public void remove(Integer id) {
		FeatureRule rule = featureRuleDao.readByPrimaryKey(id);
		if(rule.getDomain()!=null){
			rule.getDomain().getRules().remove(rule);
		}
		rule.getFeature().getRules().remove(rule);
		featureRuleDao.delete(rule);
	}

	@Override
	public void toggleStatus(Integer id) {
		FeatureRule rule = featureRuleDao.readByPrimaryKey(id);
		rule.setEnabled(!rule.getEnabled());
		rule.setUpdated(Calendar.getInstance());
		featureRuleDao.save(rule);
	}

	@Override
	public Collection<FeatureRuleVO> getRulesByDomainId(Integer domainId,
			Integer featureId) {
		return ruleTranslator.translate(this.featureRuleDao.findByFeatureIdAndDomainId(featureId, domainId));
	}

	@Override
	public Collection<FeatureRuleVO> getRulesWithoutDomain(Integer featureId) {
		return ruleTranslator.translate(this.featureRuleDao.findByFeatureIdAndNullDomain(featureId));
	}

}
