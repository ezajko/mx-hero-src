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

package org.mxhero.console.backend.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.mxhero.console.backend.service.RuleService;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService
public class RuleEndPoint {

	private RuleService ruleService;
	
	@Autowired
	public RuleEndPoint(@Qualifier("jdbcRuleService")RuleService ruleService) {
		this.ruleService = ruleService;
	}

	@WebMethod(action="createRule")
	public Integer createRule(
			@WebParam(name="ruleVO")FeatureRuleVO ruleVO, 
			@WebParam(name="featureId")Integer featureId, 
			@WebParam(name="domainId")String domainId){
		return ruleService.createRule(ruleVO, featureId, domainId);
	}
	
	@WebMethod(action="removeRule")
	public void removeRule(
			@WebParam(name="ruleId")Integer ruleId){
		ruleService.remove(ruleId);
	}
	
	@WebMethod(action="toggleStatus")
	public void toggleStatus(
			@WebParam(name="ruleId")Integer ruleId){
		ruleService.toggleStatus(ruleId);
	}
	
}
