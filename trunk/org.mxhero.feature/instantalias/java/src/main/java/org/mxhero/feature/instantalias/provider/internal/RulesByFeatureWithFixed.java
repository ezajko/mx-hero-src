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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
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

public abstract class RulesByFeatureWithFixed extends RulesByFeature {

	protected static final String REPLY_ALIAS="--";
	protected static final String REPLY_START="mxhero-instantalias";
	
	private static Logger log = LoggerFactory.getLogger(RulesByFeatureWithFixed.class);
	private String group;
	
	@Override
	public Map<String, Set<CoreRule>> getRules() {
		Map<String, Set<CoreRule>> rules = super.getRules();
		if(rules==null){
			rules = new HashMap<String, Set<CoreRule>>();
		}
		if(rules.get(group)!=null){
			rules.get(group).add(getFixedRule());
		}else{
			HashSet<CoreRule> groupRules = new HashSet<CoreRule>();
			groupRules.add(getFixedRule());
			rules.put(group,groupRules);
		}
		return rules;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	private CoreRule getFixedRule(){
		CoreRule coreRule = new CoreRule(getFeature().getId()*(-1), getFeature().getBasePriority(), group);
		coreRule.addEvaluation(new IAFixedEval());
		coreRule.addAction(new IAFixedAction());
		return coreRule;
	}

	private class IAFixedEval implements Evaluable{
		@Override
		public boolean eval(Mail mail) {
			log.debug("analize recipient:"+mail.getRecipient().getMail()+", TO:"+Arrays.deepToString(mail.getRecipients().getRecipients(RecipientType.to).toArray()));
			boolean result = mail.getStatus().equals(Mail.Status.deliver)
					&& mail.getSender().getDomain().getManaged();
			
			if(result){
				if(mail.getRecipient().getMail().startsWith(REPLY_START)){
					log.debug("is recipient");
					return true;
				}
				
				for(String recipient : mail.getRecipients().getRecipients(RecipientType.to)){
					log.debug("cheking TO:"+recipient);
					if(recipient.startsWith(REPLY_START)){
						log.debug("process");
						return true;
					}
				}
			}
			
			return result;
		}
	}
	
	private class IAFixedAction implements Actionable{
		@Override
		public void exec(Mail mail) {
			
			if(mail.getRecipient().getMail().startsWith(REPLY_START)){
				String[] emailComposition = mail.getRecipient().getMail().split(REPLY_ALIAS);
				String senderDomain=emailComposition[2];
				String senderAccount=emailComposition[1];
				log.debug("senderDomain:"+senderDomain);
				log.debug("senderAccount:"+senderAccount);
				//if sender is from the domain of the alias and the account is in the alias
				if(mail.getSender().getDomain().hasAlias(senderDomain)
						&& senderAccount.startsWith(mail.getSender().getMail().split("@")[0])){
						String sender = senderAccount+"@"+senderDomain;
						String recipient = mail.getRecipient().getMail().substring(emailComposition[0].length()+emailComposition[1].length()+emailComposition[2].length()+(REPLY_ALIAS.length()*3));
						log.debug("sender:"+sender+", recipient:"+recipient);
						CloneParameters cloneParameters = new CloneParameters(sender, recipient);
						cloneParameters.setGenerateId(false);
						cloneParameters.setOverride("both");
						mail.getHeaders().removeHeader("Reply-To");
						mail.cmd(Clone.class.getName(), cloneParameters);
						mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.instantalias.redirected", recipient));
						mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.instantalias.redirected.sender", sender));
						mail.redirect("org.mxhero.feature.redirect");
				} else{
					log.debug("no match");
				}
			}else{
				for(String recipient : mail.getRecipients().getRecipients(RecipientType.to)){
					log.debug("cheking="+recipient);
					if(recipient.startsWith(REPLY_START)){
						log.debug("fixing:"+recipient);
						mail.getRecipients().removeRecipient(RecipientType.to, recipient);
						String[] emailComposition = recipient.split(REPLY_ALIAS);
						String recipientOriginal = recipient.substring(emailComposition[0].length()+emailComposition[1].length()+emailComposition[2].length()+(REPLY_ALIAS.length()*3));
						mail.getRecipients().addRecipient(RecipientType.to, recipientOriginal);
					}
				}
			}
		}
	}
}
