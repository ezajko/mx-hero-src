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
