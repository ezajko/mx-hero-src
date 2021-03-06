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

package org.mxhero.feature.bccusagedetection.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
	
	private static String EMAIL_VALUE = "email.value";
	private static String LIST_IGNORE = "lists.ignore";
	private static String BCC_HEADER = "bcc.header";
	private static String BCC_HEADER_PLAIN = "bcc.header.plain";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(), this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()), (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
		Boolean ignoreList = false;
		String email = null;
		String bccHeader = "BCC:";
		String bccHeaderPlain = "BCC:";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(LIST_IGNORE)){
				ignoreList=Boolean.parseBoolean(property.getValue());
			}else if (property.getKey().equals(EMAIL_VALUE)){
				email = property.getValue().toLowerCase();
			}else if (property.getKey().equals(BCC_HEADER)){
				bccHeader = property.getValue();
			}else if (property.getKey().equals(BCC_HEADER_PLAIN)){
				bccHeaderPlain = property.getValue();
			}
		}
		
		coreRule.addEvaluation(new FromInHeaders(rule.getFromDirection(), rule.getToDirection(), rule.getTwoWays()));
		coreRule.addEvaluation(new BCCUDEval(ignoreList));
		coreRule.addAction(new BCCUDAction(email, rule.getId(), bccHeader, bccHeaderPlain));
		
		return coreRule;
	}

	private class BCCUDEval implements Evaluable{
		Boolean ignoreList = false;

		public BCCUDEval(Boolean ignoreList) {
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
					&& mail.getHeaders()!=null
					&& mail.getRecipients()!=null
					&& !mail.getProperties().containsKey("org.mxhero.feature.bccusagedetection")
					&& !mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply")
					&& ((ignoreList && !ignoreListCheck(mail) && !mail.getRecipient().hasAlias(allRecipients.toArray(new String[allRecipients.size()])))
					|| (!ignoreList && !mail.getRecipient().hasAlias(allRecipients.toArray(new String[allRecipients.size()]))));
			
			boolean isForwarded = mail.getHeaders().hasHeader("X-Forwarded-To")
								&& mail.getRecipient().hasAlias(mail.getHeaders().getHeaderValue("X-Forwarded-To"));
			
			if(log.isDebugEnabled()){
				log.debug("ignoreList:"+ignoreList);
				log.debug("isForwarded:"+isForwarded);
				log.debug("ignoreListCheck:"+ignoreListCheck(mail));
				log.debug("allRecipients:"+Arrays.deepToString(allRecipients.toArray()));
				log.debug("eva result:"+result);
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
	
	
	private class BCCUDAction implements Actionable{
		private String email = null;
		private Integer ruleId = null;
		private String bccHeader = null;
		private String bccHeaderPlain=null;

		public BCCUDAction(String email, Integer ruleId, String bccHeader, String bccHeaderPlain) {
			this.email = email;
			this.ruleId = ruleId;
			this.bccHeader = bccHeader;
			this.bccHeaderPlain = bccHeaderPlain;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.bccusagedetection", "true");
			mail.getHeaders().addHeader("X-mxHero-BCCUsageDetection", "rule="+ruleId);
			for(String individualMail : email.split(",")){
				try {
					InternetAddress emailAddress = new InternetAddress(individualMail,false);
					ReplyParameters replyParameters = new ReplyParameters(mail.getSender().getMail(),bccHeaderPlain,bccHeader);
					replyParameters.setRecipient(emailAddress.getAddress());
					replyParameters.setIncludeMessage(true);
					mail.cmd(Reply.class.getName(), replyParameters);
				} catch (AddressException e) {
					log.warn("wrong email address",e);
				}
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.bccusagedetection",Boolean.TRUE.toString()));
		}
		
	}
}
