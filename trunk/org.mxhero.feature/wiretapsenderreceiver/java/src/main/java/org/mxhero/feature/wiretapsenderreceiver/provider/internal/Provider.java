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

package org.mxhero.feature.wiretapsenderreceiver.provider.internal;

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
	private static final String EMAIL_PROPERTY="email.value";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String backupRecipient = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(EMAIL_PROPERTY)){
				backupRecipient=property.getValue();
			} 
		}
		
		coreRule.addEvaluation(new WTEvaluate(coreRule.getGroup()));
		coreRule.addAction(new WTAction(coreRule.getId(), coreRule.getGroup(), backupRecipient));
		
		return coreRule;
	}

	private class WTEvaluate implements Evaluable{

		private String group;
		
		public WTEvaluate(String group) {
			super();
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.wiretapsenderreceiver:"+group);
		}
		
	}
	
	private class WTAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String backupRecipient;
		
		public WTAction(Integer ruleId, String group, String backupRecipient) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.backupRecipient = backupRecipient;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.wiretapsenderreceiver:"+group,ruleId.toString());
			for(String individualMail : backupRecipient.split(",")){
				try {
					InternetAddress emailAddress = new InternetAddress(individualMail,false);
					if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
						mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
						mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
						mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapsenderreceiver.copy."+emailAddress.getAddress(), Boolean.TRUE.toString()));
					}
				} catch (AddressException e) {
					log.warn("wrong email address",e);
				}
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapsenderreceiver", mail.getSender().getMail()+";"+mail.getRecipient().getMail()));
		}
		
	}
	
}
