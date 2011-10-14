package org.mxhero.engine.core.internal.pool.processor;

import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.core.mail.MailVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.rules.RuleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to process the mails with the given rules.
 * @author mmarmol
 */
public class DefaultRulesProcessor implements RulesProcessor{

	private static Logger log = LoggerFactory.getLogger(DefaultRulesProcessor.class);
	
	private PropertiesService properties;
	
	/**
	 * Set the top and bottom agenda and the domain agenda after that fire the rules.
	 * @see org.mxhero.engine.core.internal.pool.processor.RulesProcessor#process(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.core.internal.pool.filler.SessionFiller, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public void process(RuleBase base, SessionFiller filler,
			UserFinder userfinder, MimeMail mail) {

		//Getting domin group and adding objects
		String domainAgendaGroup = null;
		MailVO fact = filler.fill(userfinder, mail);
		if(mail.getPhase().equals(RulePhase.SEND)){
			domainAgendaGroup = mail.getSenderDomainId();
			//Adding default rules to start first
			String bottomAgendaGroup = getProperties().getValue(Core.GROUP_ID_BOTTOM);
			if (bottomAgendaGroup!=null && !bottomAgendaGroup.isEmpty()){
				log.debug("firing rules for bottom:"+bottomAgendaGroup);
				base.process(bottomAgendaGroup, fact);
			}
			//Adding domain agenda group
			if(domainAgendaGroup!=null){
				log.debug("firing rules for domain:"+domainAgendaGroup);
				base.process(domainAgendaGroup, fact);
			}
			
		} else if (mail.getPhase().equals(RulePhase.RECEIVE)){
			domainAgendaGroup = mail.getRecipientDomainId();
			//Adding domain agenda group
			if(domainAgendaGroup!=null){
				log.debug("firing rules for domain:"+domainAgendaGroup);
				base.process(domainAgendaGroup, fact);
			}
			//Adding default rules to start first
			String startAgendaGroup = getProperties().getValue(Core.GROUP_ID_TOP);
			if (startAgendaGroup!=null && !startAgendaGroup.isEmpty()){
				log.debug("firing rules for top:"+startAgendaGroup);
				base.process(startAgendaGroup, fact);
			}
		}
	}

	/**
	 * @return the properties
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}