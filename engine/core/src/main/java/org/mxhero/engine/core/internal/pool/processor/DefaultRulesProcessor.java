package org.mxhero.engine.core.internal.pool.processor;

import org.drools.runtime.StatefulKnowledgeSession;
import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.properties.PropertiesService;
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
	public void process(StatefulKnowledgeSession ksession, SessionFiller filler,
			UserFinder userfinder, DomainFinder domainFinder, MimeMail mail) {
		
		log.debug("Executing rules for "+mail);
		
		try{
		ksession.getAgenda().clear();

		//Getting domin group and adding objects
		String domainAgendaGroup = filler.fill(ksession, userfinder, domainFinder, mail);
		
		//Adding default rules to start first
		String bottomAgendaGroup = getProperties().getValue(Core.GROUP_ID_BOTTOM);
		if (bottomAgendaGroup!=null && !bottomAgendaGroup.isEmpty()){
			log.debug("Set focus on agenda "+bottomAgendaGroup);
			ksession.getAgenda().getAgendaGroup(bottomAgendaGroup).setFocus();
		}

		//Adding domain agenda group
		if(domainAgendaGroup!=null){
			ksession.getAgenda().getAgendaGroup(domainAgendaGroup).setFocus();
			log.debug("Set focus on agenda "+domainAgendaGroup);
		}
		
		//Adding default rules to start first
		String startAgendaGroup = getProperties().getValue(Core.GROUP_ID_TOP);
		if (startAgendaGroup!=null && !startAgendaGroup.isEmpty()){
			log.debug("Set focus on agenda "+startAgendaGroup);
			ksession.getAgenda().getAgendaGroup(startAgendaGroup).setFocus();
		}

		ksession.fireAllRules();
		log.debug("rules fired");
		} finally {
			ksession.dispose();
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
