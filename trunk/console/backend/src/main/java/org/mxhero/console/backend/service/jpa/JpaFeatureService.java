package org.mxhero.console.backend.service.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.mxhero.console.backend.dao.ApplicationUserDao;
import org.mxhero.console.backend.dao.CategoryDao;
import org.mxhero.console.backend.dao.FeatureDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.LocalePropertyDao;
import org.mxhero.console.backend.dao.SystemPropertyDao;
import org.mxhero.console.backend.entity.Category;
import org.mxhero.console.backend.entity.Feature;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.LocaleProperty;
import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.vo.CategoryVO;
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
	
	private LocalePropertyDao localePropertyDao;
	
	private SystemPropertyDao systemPropertyDao;
	
	private ApplicationUserDao userDao;
	
	@Autowired
	public JpaFeatureService(CategoryDao categoryDao, FeatureDao featureDao,
			FeatureRuleDao featureRuleDao, LocalePropertyDao localePropertyDao,
			SystemPropertyDao systemPropertyDao,
			ApplicationUserDao userDao) {
		super();
		this.categoryDao = categoryDao;
		this.featureDao = featureDao;
		this.featureRuleDao = featureRuleDao;
		this.localePropertyDao = localePropertyDao;
		this.systemPropertyDao = systemPropertyDao;
		this.userDao = userDao;
	}

	@Override
	public Collection<CategoryVO> findFeatures() {
		
		Integer unclassifiedCategoryId = Integer.parseInt(systemPropertyDao.findByKey(UNCLASSIFIED_ID).getPropertyValue());
		List<Category> categories = categoryDao.readAll();
				
		for(Category category : categories){
			if(category.getId()==unclassifiedCategoryId){
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
		Integer unclassifiedCategoryId = Integer.parseInt(systemPropertyDao.findByKey(UNCLASSIFIED_ID).getPropertyValue());
		List<Category> categories = categoryDao.readAll();
				
		for(Category category : categories){
			if(category.getId()==unclassifiedCategoryId){
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
			CategoryVO categoryVO = new CategoryVO();
			categoryVO.setIconsrc(category.getIconSource());
			categoryVO.setId(category.getId());
			categoryVO.setLabel(getValue(Category.class.getName(),category.getLabelKey(),userLocale,defaultLocale));
			
		}
		 
		return categoryVOs;
	}

	private String getValue(String component, String key, String userLocale, String defaultLocale ){
		LocaleProperty localeProperty = null;
		
		localeProperty = localePropertyDao.findByComponentAndLocaleAndPropertyKey(component, userLocale, key);
		if(localeProperty!=null){
			return localeProperty.getPropertyValue();
		}
		
		localeProperty = localePropertyDao.findByComponentAndLocaleAndPropertyKey(component, defaultLocale, key);
		if(localeProperty!=null){
			return localeProperty.getPropertyValue();
		}
		
		return key;
	}
}
