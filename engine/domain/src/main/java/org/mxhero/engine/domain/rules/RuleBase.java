package org.mxhero.engine.domain.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.domain.mail.business.Mail;

public class RuleBase {

	private Map<String, Collection<CoreRule>> coreRules = new HashMap<String, Collection<CoreRule>>();
	
	private boolean isReady = false;
	
	public void process(String group, Mail mail){
		if(!isReady){
			ready();
		}
		Collection<CoreRule> groupRules = coreRules.get(group);
		if(groupRules!=null){
			for(CoreRule rule : groupRules){
				if(rule!=null){
					rule.process(mail);
				}
			}
		}
	}
	
	public void addRules(Map<String, Set<CoreRule>> rules){
		if(rules!=null){
			for(String group : rules.keySet()){
				addRules(group,rules.get(group));
			}
		}
	}
	
	public void addRules(String group, Collection<CoreRule> rules){
		if(rules!=null){
			for(CoreRule rule : rules){
				addRule(group,rule);
			}
		}
	}
	
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

	public int getGroups(){
		return coreRules.size();
	}
	
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
