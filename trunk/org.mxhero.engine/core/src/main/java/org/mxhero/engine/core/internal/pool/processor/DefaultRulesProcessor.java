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
			//Adding default rules to start first
			String startAgendaGroup = getProperties().getTopGroupId();
			if (startAgendaGroup!=null && !startAgendaGroup.isEmpty()){
				log.debug("firing rules for top:"+startAgendaGroup);
				base.process(startAgendaGroup, fact);
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
			String bottomAgendaGroup = getProperties().getBottomGroupId();
			if (bottomAgendaGroup!=null && !bottomAgendaGroup.isEmpty()){
				log.debug("firing rules for bottom:"+bottomAgendaGroup);
				base.process(bottomAgendaGroup, fact);
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
