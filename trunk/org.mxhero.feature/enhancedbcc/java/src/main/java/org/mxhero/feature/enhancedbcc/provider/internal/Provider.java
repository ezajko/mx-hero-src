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

package org.mxhero.feature.enhancedbcc.provider.internal;

import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatchParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeatureWithFixed{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	public static final String FOLLOWER_ID = "org.mxhero.feature.enhancedbcc";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		coreRule.addEvaluation(new EBEvaluation());
		coreRule.addAction(new EBAction());
		return coreRule;
	}

	
	private class EBEvaluation implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			List<String> allRecipients = new ArrayList<String>();
			if(mail.getRecipients().getRecipients(RecipientType.cc)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.cc));
			}
			if(mail.getRecipients().getRecipients(RecipientType.to)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.to));
			}
			return mail.getStatus().equals(Mail.Status.deliver) 
					&& mail.getRecipients()!=null
					&& !mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply")
					&& !mail.getRecipient().hasAlias(allRecipients.toArray(new String[allRecipients.size()]));
		}
		
	}
	
	private class EBAction implements Actionable{

		@Override
		public void exec(Mail mail) {
			for(User user : mail.getRecipientsInHeaders()){
				AddThreadWatchParameters parameters = new AddThreadWatchParameters(FOLLOWER_ID+"."+mail.getRecipient().getMail(),"none");
				parameters.setSenderId(mail.getSender().getMail());
				parameters.setRecipientId(user.getMail());
				log.debug("wathc thread for sender:"+parameters.getSenderId()+" and recipient:"+parameters.getRecipientId());
				mail.cmd(AddThreadWatch.class.getName(), parameters);
			}
			
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.enhancedbcc", "true"));
		}
		
	}
}
