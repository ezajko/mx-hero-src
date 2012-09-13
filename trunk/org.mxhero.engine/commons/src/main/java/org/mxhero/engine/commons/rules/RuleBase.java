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

package org.mxhero.engine.commons.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mxhero
 *
 */
public class RuleBase {

	private static Logger log = LoggerFactory.getLogger(RuleBase.class);
	
	private Map<String, Collection<CoreRule>> coreRules = new HashMap<String, Collection<CoreRule>>();
	
	private boolean isReady = false;
	
	/**
	 * @param group
	 * @param mail
	 */
	public void process(String group, Mail mail){
		if(!isReady){
			ready();
		}
		Collection<CoreRule> groupRules = coreRules.get(group);
		if(groupRules!=null){
			log.debug("found "+groupRules.size()+" rules for group "+group);
			for(CoreRule rule : groupRules){
				if(rule!=null){
					try{
						if(log.isDebugEnabled()){
							log.debug("mail "+mail.getId()+"running rule "+rule.toString());
						}
						rule.process(mail);
					}catch(Exception e){
						log.error("Error while processing rule"+rule,e);
					}
				}
			}
		}else{
			log.debug("found 0 rules for group "+group);
		}
	}
	
	/**
	 * @param rules
	 */
	public void addRules(Map<String, Set<CoreRule>> rules){
		if(rules!=null){
			for(String group : rules.keySet()){
				addRules(group,rules.get(group));
			}
		}
	}
	
	/**
	 * @param group
	 * @param rules
	 */
	public void addRules(String group, Collection<CoreRule> rules){
		if(rules!=null){
			for(CoreRule rule : rules){
				addRule(group,rule);
			}
		}
	}
	
	/**
	 * @param group
	 * @param rule
	 */
	public void addRule(String group, CoreRule rule){
		if(isReady){
			throw new RuntimeException("Data base closed, you can not add new rules");
		}
		if(!coreRules.containsKey(group)){
			coreRules.put(group, new HashSet<CoreRule>());
		}
		if(coreRules.get(group).contains(rule)){
			coreRules.get(group).remove(rule);
		}
		coreRules.get(group).add(rule);
	}
	
	/**
	 * 
	 */
	private synchronized void ready(){
		//prepare the rule base
		if(!isReady){
			for(String group : coreRules.keySet()){
				List<CoreRule> ordered = new ArrayList<CoreRule>(coreRules.get(group));
				Collections.sort(ordered);
				coreRules.put(group, ordered);
			}
			isReady=true;
		}
	}

	/**
	 * @return
	 */
	public int getGroups(){
		return coreRules.size();
	}
	
	/**
	 * @return
	 */
	public int getRules(){
		int size = 0;
		for(String group : coreRules.keySet()){
			if(coreRules.get(group)!=null){
				size=size+coreRules.get(group).size();
			}
		}
		return size;
	}
}
