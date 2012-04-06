package org.mxhero.engine.core.internal.pool.processor;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.RuleBase;
import org.mxhero.engine.core.internal.CoreProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to process the mails with the given rules.
 * @author mmarmol
 */
public class DefaultRulesProcessor implements RulesProcessor{

	private static Logger log = LoggerFactory.getLogger(DefaultRulesProcessor.class);
	
	private CoreProperties properties;
	
	/**
	 * Set the top and bottom agenda and the domain agenda after that fire the rules.
	 * @see org.mxhero.engine.core.internal.pool.processor.RulesProcessor#process(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.core.internal.filler.SessionFiller, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public void process(RuleBase base, Mail fact) {

		//Getting domain group and adding objects
		String domainAgendaGroup = null;
		if(fact.getPhase().equals(Mail.Phase.send)){
			domainAgendaGroup = fact.getSender().getDomain().getId();
			//Adding default rules to start first
			String bottomAgendaGroup = getProperties().getBottomGroupId();
			if (bottomAgendaGroup!=null && !bottomAgendaGroup.isEmpty()){
				log.debug("firing rules for bottom:"+bottomAgendaGroup);
				base.process(bottomAgendaGroup, fact);
			}
			//Adding domain agenda group
			if(domainAgendaGroup!=null){
				log.debug("firing rules for domain:"+domainAgendaGroup);
				base.process(domainAgendaGroup, fact);
			}
			
		} else if (fact.getPhase().equals(Mail.Phase.receive)){
			domainAgendaGroup = fact.getRecipient().getDomain().getId();
			//Adding domain agenda group
			if(domainAgendaGroup!=null){
				log.debug("firing rules for domain:"+domainAgendaGroup);
				base.process(domainAgendaGroup, fact);
			}
			//Adding default rules to start first
			String startAgendaGroup = getProperties().getTopGroupId();
			if (startAgendaGroup!=null && !startAgendaGroup.isEmpty()){
				log.debug("firing rules for top:"+startAgendaGroup);
				base.process(startAgendaGroup, fact);
			}
		}
	}

	/**
	 * @return the properties
	 */
	public CoreProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(CoreProperties properties) {
		this.properties = properties;
	}

}
