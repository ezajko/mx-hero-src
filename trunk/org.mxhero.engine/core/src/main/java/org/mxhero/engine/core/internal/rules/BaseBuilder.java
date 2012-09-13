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

package org.mxhero.engine.core.internal.rules;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.RuleBase;
import org.mxhero.engine.commons.rules.provider.RulesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate a base from the different rules providers.
 * 
 * @author mmarmol
 */
public class BaseBuilder {

	private static Logger log = LoggerFactory
			.getLogger(BaseBuilder.class);

	@SuppressWarnings("rawtypes")
	private List rulesProviders;

	private RuleBase base = null;


	public void buildBase() {

		RuleBase newBase = new RuleBase();
		log.debug("total rulesProviders:"+rulesProviders.size());
		for(Object provider : rulesProviders){
			try{
				Map<String, Set<CoreRule>> rules =((RulesProvider)provider).getRules();
				if(rules!=null && rules.size()>0){
					newBase.addRules(rules);			
				}				
			}catch (Exception e) {
				log.error("error while trying to load rules from external provider",e);
			}
		}
		base=newBase;
	}

	/**
	 * @return the knowledgeBase
	 */
	public RuleBase getBase() {
		return base;
	}

	@SuppressWarnings("rawtypes")
	public List getRulesProviders() {
		return rulesProviders;
	}

	@SuppressWarnings("rawtypes")
	public void setRulesProviders(List rulesProviders) {
		this.rulesProviders = rulesProviders;
	}

}
