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

package org.mxhero.feature.emailsizelimiter.provider.internal;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static DecimalFormat formatter = new DecimalFormat("#0.00#");
	private static final String MAX_SIZE_PROPERTY = "max.size";
	private static final String RETURN_MESSAGE_PROPERTY = "return.message";
	private static final String RETURN_MESSAGE_PLAIN_PROPERTY = "return.message.plain";
	private static final String ACTION_SELECTION_PROPERTY = "action.selection";
	private static final Double CODING_FACTOR = 1.3;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		Number maxSizeValue = null;
		Integer effectiveMaxSize = null;
		String returnMessage = null;
		String returnMessagePlain = null;
		String action = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(MAX_SIZE_PROPERTY)){
				try {
					maxSizeValue=formatter.parse(property.getValue());
					effectiveMaxSize = (int)(maxSizeValue.doubleValue()*1024*1024*CODING_FACTOR);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			} else if(property.getKey().equals(RETURN_MESSAGE_PROPERTY)){
				returnMessage = property.getValue();
			} else if(property.getKey().equals(ACTION_SELECTION_PROPERTY)){
				action = property.getValue();
			} else if(property.getKey().equals(RETURN_MESSAGE_PLAIN_PROPERTY)){
				returnMessagePlain = property.getValue();
			}
		}
		
		coreRule.addEvaluation(new EsEvaluate(coreRule.getGroup()));
		coreRule.addAction(new ESAction(rule.getId(), coreRule.getGroup(), effectiveMaxSize, returnMessagePlain, returnMessage, action, getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}
	
	private class EsEvaluate implements Evaluable{

		private String group;

		public EsEvaluate(String group) {
			super();
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.initialsizelimiter:"+group);
		}
		
	}
	
	private class ESAction implements Actionable{

		private static final String ACTION_RETURN = "return";
		
		private Integer ruleId;
		private String group;
		private Integer effectiveMaxSize;
		private String returnMessage;
		private String returnMessagePlain;
		private String action;
		private String replyMail;

		public ESAction(Integer ruleId, String group, Integer effectiveMaxSize,
				String returnMessagePlain, String returnMessage, String action, String replyMail) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.effectiveMaxSize = effectiveMaxSize;
			this.returnMessage = returnMessage;
			this.action = action;
			this.replyMail = replyMail;
			this.returnMessagePlain = returnMessagePlain;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-InitialSizeLimiter","rule="+ruleId);
			mail.getProperties().put("org.mxhero.feature.initialsizelimiter:"+group,ruleId.toString());
			boolean isDropped = false;
			if(mail.getInitialSize()>effectiveMaxSize){
				mail.drop("org.mxhero.feature.initialsizelimiter");
				isDropped=true;
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.initialsizelimiter"));
			}
			if(action.equalsIgnoreCase(ACTION_RETURN) && isDropped){
				ReplyParameters replyParameters = new ReplyParameters(replyMail, returnMessagePlain, returnMessage);
				replyParameters.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(),replyParameters);
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.initialsizelimiter", Boolean.toString(isDropped)));
		}
		
	}

}
