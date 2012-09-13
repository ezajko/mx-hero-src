/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
