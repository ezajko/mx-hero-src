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

package org.mxhero.feature.redirect.provider.internal;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String REDIRECT_PROPERTY="redirect.email";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String redirectRecipient = null;

		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(REDIRECT_PROPERTY)){
				redirectRecipient=property.getValue();
			} 
		}
		
		coreRule.addEvaluation(new REvaluate(coreRule.getGroup()));
		coreRule.addAction(new RAction(coreRule.getId(), coreRule.getGroup(), redirectRecipient));
		
		return coreRule;
	}

	private class REvaluate implements Evaluable{

		private String group;
	
		public REvaluate(String group) {
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.redirect:"+group);
		}
		
	}
	
	private class RAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String redirectRecipient;

		public RAction(Integer ruleId, String group, String redirectRecipient) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.redirectRecipient = redirectRecipient;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-Redirect","rule="+ruleId+";recipient="+mail.getRecipient().getMail()+";redirected="+redirectRecipient);
			mail.getProperties().put("org.mxhero.feature.redirect:"+group, ruleId.toString());
			for(String individualMail : redirectRecipient.split(",")){
				try {
					InternetAddress emailAddress = new InternetAddress(individualMail,false);
					if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
						mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
						CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress());
						mail.cmd(Clone.class.getName(), cloneParameters);					
					}
				} catch (AddressException e) {
					log.warn("wrong email address",e);
				}
			}
			mail.redirect("org.mxhero.feature.redirect");
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.redirect.recipient", redirectRecipient));
		}
		
	}
	
}
