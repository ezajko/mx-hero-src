package org.mxhero.engine.domain.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mxhero.engine.domain.feature.Feature;
import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RulesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourcesByDomain implements ResourcesProvider{

	private static Logger log = LoggerFactory
	.getLogger(ResourcesByDomain.class);
	
	private RulesFinder rulesFinder;
	
	private Feature feature;
	
	private String component;
	
	private Integer version;

	@Override
	public List<Resource> getResources() {

		List<Resource> resources = new ArrayList<Resource>();
		
		Map<String, Collection<Rule>> domainRules = new HashMap<String, Collection<Rule>>();
		
		if (getRulesFinder()==null){
			log.warn("rulesFinder was not found for component "+component+" and version "+version);
			return null;
		}
		feature = getRulesFinder().find(component, version);
		
		if(feature==null){
			log.warn("feature was not found for component "+component+" and version "+version);
			return null;
		}
		log.debug("feature:"+feature.getComponent()+" version:"+feature.getVersion());
		log.debug("processing "+feature.getRules().size()+" rules");
		for (Rule rule : feature.getRules()){
			if(!domainRules.containsKey(rule.getDomain())){
				domainRules.put(rule.getDomain(), new ArrayList<Rule>());
				
			}	
			domainRules.get(rule.getDomain()).add(rule);
			log.debug("added rule for domain "+rule.getDomain());
		}
		
		for (String domain : domainRules.keySet()){
			Resource resource = processByDomain(domain,domainRules.get(domain));
			log.debug("processed resource for domain "+domain);
			if(resource!=null){
				resources.add(resource);
				log.debug("resourced added "+resource.getName());
			}
		}
		return resources;
	}
	
	protected abstract Resource processByDomain(String domain, Collection<Rule> rules);
	
	public RulesFinder getRulesFinder() {
		return rulesFinder;
	}

	public void setRulesFinder(RulesFinder rulesFinder) {
		this.rulesFinder = rulesFinder;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
