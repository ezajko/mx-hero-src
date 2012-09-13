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

package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.FeatureRuleVO;

public interface FeatureRuleRepository {

	void delete(Integer ruleId);
	
	void deleteByDomain(String domainId);
	
	void deleteByAccount(String domainId, String account);
	
	void deleteByGroup(String domainId, String group);
	
	boolean checkFromTo(String domainId, Integer featureId, String fromFreeValue, String toFreeValue, Integer ruleId);
	
	Integer insert(FeatureRuleVO rule);
	
	void update(FeatureRuleVO rule);
	
	void toggleStatus(Integer ruleId);
	
	List<FeatureRuleVO> findByDomainId(String domainId, Integer featureId);
	
	List<FeatureRuleVO> findWitNullDomain(Integer featureId);
	
	FeatureRuleVO findById(Integer ruleId);
}
