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

package org.mxhero.feature.bccpolicy.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.FromInHeaders;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{
	
	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	private static final String ACTION_SELECTION = "action.selection";
	private static final String RETURN_MESSAGE = "return.message";
	private static final String RETURN_MESSAGE_PLAIN = "return.message.plain";
	private static String BCC_HEADER = "bcc.header";
	private static String BCC_HEADER_PLAIN = "bcc.header.plain";
	private static final String LIST_IGNORE = "lists.ignore";
	private static final String RETURN_ACTION = "return";
			
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(), this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()), (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
	
		String action = null;
		String returnMessage = "";
		String returnMessagePlain = "";
		Boolean ignoreList = false;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ACTION_SELECTION)){
				action=property.getValue();
			}else if (property.getKey().equals(RETURN_MESSAGE) || property.getKey().equals(BCC_HEADER)){
				returnMessage = property.getValue();
			}else if (property.getKey().equals(LIST_IGNORE)){
				ignoreList = Boolean.parseBoolean(property.getValue());
			}else if (property.getKey().equals(RETURN_MESSAGE_PLAIN) || property.getKey().equals(BCC_HEADER_PLAIN)){
				returnMessagePlain = property.getValue();
			}
		}
		
		coreRule.addEvaluation(new FromInHeaders(rule.getFromDirection(), rule.getToDirection(), rule.getTwoWays()));
		coreRule.addEvaluation(new BCCPEvaluation(ignoreList));
		coreRule.addAction(new BCCPEAction(action,returnMessage,returnMessagePlain,rule.getId(),this.getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}

	private class BCCPEvaluation implements Evaluable{

		Boolean ignoreList = false;
		
		public BCCPEvaluation(Boolean ignoreList) {
			this.ignoreList = ignoreList;
		}

		@Override
		public boolean eval(Mail mail) {
			
			List<String> allRecipients = new ArrayList<String>();
			if(mail.getRecipients().getRecipients(RecipientType.cc)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.cc));
			}
			if(mail.getRecipients().getRecipients(RecipientType.to)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.to));
			}
			
			boolean result = mail.getStatus().equals(Mail.Status.deliver)
					&& !mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply")
					&& ((ignoreList && !ignoreListCheck(mail) && !mail.getRecipient().hasAlias(allRecipients.toArray(new String[0])))
					|| (!ignoreList && !mail.getRecipient().hasAlias(allRecipients.toArray(new String[0]))));
			
			boolean isForwarded = mail.getHeaders().hasHeader("X-Forwarded-To")
					&& mail.getRecipient().hasAlias(mail.getHeaders().getHeaderValue("X-Forwarded-To"));
			
			if(log.isDebugEnabled()){
				log.debug("ignoreList="+ignoreList);
				log.debug("status="+mail.getStatus());
				log.debug("isForwarded:"+isForwarded);
				log.debug("replyproperty="+mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply"));
				log.debug("ignoreListCheck="+ignoreListCheck(mail));
				log.debug("recipientAliases="+Arrays.deepToString(mail.getRecipient().getAliases().toArray()));
				log.debug("allEmailRecipients="+Arrays.deepToString(allRecipients.toArray(new String[0])));
				log.debug("result="+result);
			}
			return result && !isForwarded;
		}
		
		private boolean ignoreListCheck(Mail mail){
			String undisclosedPattern = "(?i).*undisclosed.*";
			log.debug("TO header:"+mail.getHeaders().getHeaderValue(Message.RecipientType.TO.toString()));
			if(mail.getHeaders().hasHeader(Message.RecipientType.TO.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.TO.toString()).matches(undisclosedPattern)){
				log.debug("TO undisclosed");
				return true;
			}
			log.debug("CC header:"+mail.getHeaders().getHeaderValue(Message.RecipientType.CC.toString()));
			if(mail.getHeaders().hasHeader(Message.RecipientType.CC.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.CC.toString()).matches(undisclosedPattern)){
				log.debug("CC undisclosed");
				return true;
			}
			if(mail.getHeaders().hasHeader("List-Id")||
					mail.getHeaders().hasHeader("List-Help")||
					mail.getHeaders().hasHeader("List-Subscribe")||
					mail.getHeaders().hasHeader("List-Unsubscribe")||
					mail.getHeaders().hasHeader("List-Post")||
					mail.getHeaders().hasHeader("List-Owner")||
					mail.getHeaders().hasHeader("List-Archive")){
				log.debug("List");
				return true;
			}
			return false;
		}
		
	}
	
	
	private class BCCPEAction implements Actionable {

		String action = null;
		String returnMessage = "";
		String returnMessagePlain = "";
		Integer ruleId = null;
		String noreplyMail = null;

		public BCCPEAction(String action, String returnMessage, String returnMessagePlain, Integer ruleId, String noreplyMail) {
			this.action = action;
			this.returnMessage = returnMessage;
			this.returnMessagePlain = returnMessagePlain;
			this.ruleId = ruleId;
			this.noreplyMail = noreplyMail;
		}

		@Override
		public void exec(Mail mail) {
			mail.drop("org.mxhero.feature.bccpolicy");
			mail.getHeaders().addHeader("X-mxHero-BCCPolicy", "rule="+ruleId+";blocked=true");
			log.debug("returnMessagePlain="+returnMessagePlain);
			log.debug("returnMessage="+returnMessage);
			if(action!=null && action.equals(RETURN_ACTION)){
				ReplyParameters replyParameters = new ReplyParameters(noreplyMail, returnMessagePlain, returnMessage);
				replyParameters.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(), replyParameters);
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.bccpolicy", "true"));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.bccpolicy"));
		}

	}
}
