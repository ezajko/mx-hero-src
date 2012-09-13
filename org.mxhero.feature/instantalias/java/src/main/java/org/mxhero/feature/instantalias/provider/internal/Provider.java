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

package org.mxhero.feature.instantalias.provider.internal;

import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeatureWithFixed {

	private static final String SEPARATION_CHARACTER = "separation.charater";
	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String separationCharacter = null;
		for (RuleProperty property : rule.getProperties()) {
			if (property.getKey().equals(SEPARATION_CHARACTER)) {
				separationCharacter = property.getValue();
			}
		}
		coreRule.addEvaluation(new IAEvaluate(separationCharacter));
		coreRule.addAction(new IAAction(separationCharacter, rule.getId()));
		return coreRule;
	}

	private class IAEvaluate implements Evaluable {
		String separationCharacter = null;

		public IAEvaluate(String separationCharacter) {
			this.separationCharacter = separationCharacter;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
					&& (separationCharacter != null
							&& !separationCharacter.trim().isEmpty() 
							&& mail.getRecipient().getMail().contains("@")
							&& mail.getRecipient().getMail().substring(0,mail.getRecipient().getMail().indexOf('@')).contains(separationCharacter.trim())
							&& !mail.getRecipient().getMail().startsWith(REPLY_START));
		}
	}

	private class IAAction implements Actionable {
		String separationCharacter = null;
		private Integer ruleId;
		
		public IAAction(String separationCharacter, Integer ruleId) {
			this.separationCharacter = separationCharacter;
			this.ruleId=ruleId;
		}

		@Override
		public void exec(Mail mail) {
			String realEmail=null;
			String account=null;
			String domain=null;
			String alias=null;

			try{
				int separatorInit=mail.getRecipient().getMail().indexOf(separationCharacter.trim());
				int aliasEnd = mail.getRecipient().getMail().indexOf("@");
				account = mail.getRecipient().getMail().substring(0, separatorInit).toString();
				domain = mail.getRecipient().getMail().substring(aliasEnd+1).toString();
				alias = mail.getRecipient().getMail().substring(separatorInit+1, aliasEnd).toString();
				realEmail= account+"@"+domain;
			}catch(RuntimeException e){
				log.warn("error while dealing with alias="+mail.getRecipient().getMail());
				throw(e);
			}
			
			String replyTo=null;
			try{replyTo = mail.getHeaders().getHeaderValue("Reply-To");
				replyTo = new InternetAddress(replyTo,false).getAddress();
			}catch (Exception e){replyTo = null;}
			if(replyTo==null || replyTo.trim().length()<3){
				try{replyTo = mail.getHeaders().getHeaderValue("From");
					replyTo = new InternetAddress(replyTo,false).getAddress();
				}catch (Exception e){replyTo = null;}
			}
			if(replyTo==null || replyTo.trim().length()<3){
				replyTo=mail.getSender().getMail();
			}
			replyTo = REPLY_START+REPLY_ALIAS+account+separationCharacter.trim()+alias+REPLY_ALIAS+domain+REPLY_ALIAS+replyTo;
			mail.getHeaders().removeHeader("Reply-To");
			mail.getHeaders().addHeader("Reply-To",replyTo);
			mail.getHeaders().addHeader("X-mxHero-InstantAlias","rule="+ruleId+";alias="+mail.getRecipient().getMail());
			CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(),realEmail);
			mail.cmd(Clone.class.getName(),cloneParameters);
			mail.redirect("org.mxhero.feature.instantalias");
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.instantalias.redirected", realEmail));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.instantalias.alias", mail.getRecipient().getMail()));

		}

	}
}
