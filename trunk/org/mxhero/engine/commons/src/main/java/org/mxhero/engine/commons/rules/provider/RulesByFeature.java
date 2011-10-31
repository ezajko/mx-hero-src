package org.mxhero.engine.commons.rules.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Feature;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.feature.RulesFinder;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.FromToEval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RulesByFeature implements RulesProvider{
	
	private static final String MXHERO_NOREPLY_NAME="mxhero.noreply.name";
	private static final String MXHERO_ADMIN_NAME="mxhero.admin.name";
	private static final String MXHERO_DOMAIN_NAME="mxhero.domain.name";
	
	private static final String DEFAULT_MXHERO_NOREPLY_NAME="noreply";
	private static final String DEFAULT_MXHERO_ADMIN_NAME="admin";
	private static final String DEFAULT_MXHERO_DOMAIN_NAME="mxhero.com";
	
	private static final String DOMAIN = "domain";
	private static final String GROUP = "group";
	private static final String INDIVIDUAL = "individual";
	private static final String ANYONE = "anyone";
	private static final String ANYONEELSE = "anyoneelse";
	private static final String ALLDOMAINS = "alldomains";
	
	private static Logger log = LoggerFactory
	.getLogger(RulesByFeature.class);
	
	private RulesFinder rulesFinder;
	
	private Feature feature;
	
	private String component;
	
	private Integer version;

	@Override
	public Map<String, Set<CoreRule>> getRules() {

		Map<String, Set<CoreRule>> domainRules = new HashMap<String, Set<CoreRule>>();
		
		if (getRulesFinder()==null){
			log.warn("rulesFinder was not found for component "+component+" and version "+version);
			return null;
		}
		feature = getRulesFinder().find(component, version);
		
		if(feature==null){
			log.warn("feature was not found for component "+component+" and version "+version);
			return null;
		}

		for (Rule rule : feature.getRules()){
			if(rule.getEnabled()==true){
				//if domain is null them is an admin rule and we should use the admin order.
				String group = (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder();
				if(!domainRules.containsKey(group)){
					domainRules.put(group, new HashSet<CoreRule>());
				}	
				domainRules.get(group).add(createRule(rule));
			}
		}

		return domainRules;
	}
	
	protected abstract CoreRule createRule(Rule rule);
	
	
	protected CoreRule getDefault(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(),this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()),(rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
		coreRule.addEvaluation(new FromToEval(rule.getFromDirection(), rule.getToDirection(), rule.getTwoWays()));	
		return coreRule;
	}
	
	public String getNoReplyEmail(String domain){
		String name = null;
		String noreplyDomain = null;
		String email = null;
		if(System.getProperty(MXHERO_NOREPLY_NAME)!=null && 
				System.getProperty(MXHERO_NOREPLY_NAME).trim().length()>0){
			name=System.getProperty(MXHERO_NOREPLY_NAME);
		}else{
			name=DEFAULT_MXHERO_NOREPLY_NAME;
		}
		if(domain!=null && domain.trim().length()>0){
			noreplyDomain=domain;
		}else{
			if(System.getProperty(MXHERO_DOMAIN_NAME)!=null && 
					System.getProperty(MXHERO_DOMAIN_NAME).trim().length()>0){
				noreplyDomain=System.getProperty(MXHERO_DOMAIN_NAME);
			}else{
				noreplyDomain=DEFAULT_MXHERO_DOMAIN_NAME;
			}
		}
		
		try {
			email=new InternetAddress(name+"@"+noreplyDomain).getAddress();
		} catch (AddressException e) {
			email=DEFAULT_MXHERO_NOREPLY_NAME+"@"+DEFAULT_MXHERO_DOMAIN_NAME;
		}
		
		return email;
	}
	
	public String getAdminEmail(String domain){
		String name = null;
		String adminDomain = null;
		String email = null;
		if(System.getProperty(MXHERO_ADMIN_NAME)!=null && 
				System.getProperty(MXHERO_ADMIN_NAME).trim().length()>0){
			name=System.getProperty(MXHERO_ADMIN_NAME);
		}else{
			name=DEFAULT_MXHERO_ADMIN_NAME;
		}
		if(domain!=null && domain.trim().length()>0){
			adminDomain=domain;
		}else{
			if(System.getProperty(MXHERO_DOMAIN_NAME)!=null && 
					System.getProperty(MXHERO_DOMAIN_NAME).trim().length()>0){
				domain=System.getProperty(MXHERO_DOMAIN_NAME);
			}else{
				name=DEFAULT_MXHERO_DOMAIN_NAME;
			}
		}
		
		try {
			email=new InternetAddress(name+"@"+adminDomain).getAddress();
		} catch (AddressException e) {
			email=DEFAULT_MXHERO_ADMIN_NAME+"@"+DEFAULT_MXHERO_DOMAIN_NAME;
		}
		
		return email;
	}
	
	public int getPriority(RuleDirection direction){
		if(direction.getDirectionType().equals(ANYONE)){
			return 1;
		} else if(direction.getDirectionType().equals(ANYONEELSE)){
			return 2;
		}else if(direction.getDirectionType().equals(ALLDOMAINS)){
			return 4;
		}else if(direction.getDirectionType().equals(DOMAIN)){
			return 8;
		}else if(direction.getDirectionType().equals(GROUP)){
			return 16;
		}else if(direction.getDirectionType().equals(INDIVIDUAL)){
			return 32;
		}else{
			return 0;
		}
	}
	
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
