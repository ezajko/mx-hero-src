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

package org.mxhero.feature.attachmentlink.provider.internal;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.attachmentlink.alcommand.ALCommandParameters;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static DecimalFormat formatter = new DecimalFormat("#0.00#");
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";	
	private static final String RETURN_MESSAGE_PLAIN = "return.message.plain";
	private static final String MAX_SIZE_PROPERTY = "max.size";
	private static final String LOCALE = "locale";
	private static final String DEFAULT_LOCALE = "en_US";
	private static final Double CODING_FACTOR = 1.3;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		Number maxSizeValue = null;
		Integer effectiveMaxSize = null;
		boolean notify = false;
		String message = "";
		String messagePlain = "";
		String locale = DEFAULT_LOCALE;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equalsIgnoreCase(ACTION_SELECTION)){
				if(property.getValue().equalsIgnoreCase(ACTION_RETURN)){
					notify=true;
				}
			} else if(property.getKey().equalsIgnoreCase(RETURN_MESSAGE)){
				message = property.getValue();
			}else if(property.getKey().equalsIgnoreCase(RETURN_MESSAGE_PLAIN)){
				messagePlain = property.getValue();
			}else if(property.getKey().equalsIgnoreCase(LOCALE)){
				locale = property.getValue();
			} else if(property.getKey().equals(MAX_SIZE_PROPERTY)){
				try {
					maxSizeValue=formatter.parse(property.getValue());
					effectiveMaxSize = (int)(maxSizeValue.doubleValue()*1024*1024*CODING_FACTOR);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		coreRule.addEvaluation(new ALEvaluation(effectiveMaxSize));
		coreRule.addAction(new ALAction(rule.getId(), notify, message, messagePlain, locale));
		
		return coreRule;
	}

	public class ALEvaluation implements Evaluable{

		private Integer effectiveMaxSize = null;
	
		public ALEvaluation(Integer effectiveMaxSize) {
			this.effectiveMaxSize = effectiveMaxSize;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getInitialSize()> effectiveMaxSize
			&& mail.getAttachments().isAttached()
			&& !mail.getProperties().containsKey("org.mxhero.feature.attachmentlink")
			&& mail.getHeaders()!=null;
		}
		
	}
	
	public class ALAction implements Actionable{
		
		private Integer ruleId;
		private boolean notify = false;
		private String  message = null;
		private String  messagePlain = null;
		private String locale = null;
		
		
		public ALAction(Integer ruleId, boolean notify, String message, String messagePlain, String locale) {
			super();
			this.notify = notify;
			this.message = message;
			this.locale = locale;
			this.ruleId = ruleId;
			this.messagePlain = messagePlain;
		}

		@Override
		public void exec(Mail mail) {
			String files = Arrays.deepToString(mail.getAttachments().getFileNames().toArray());
			Result result = mail.cmd(AlCommand.class.getName(), new ALCommandParameters(locale, notify, messagePlain, message));
			if(!mail.getStatus().equals(Mail.Status.requeue)){
				mail.getHeaders().addHeader("X-mxHero-Attachmentlink","rule="+ruleId+";result="+result.isConditionTrue());
				mail.getProperties().put("org.mxhero.feature.attachmentlink", ruleId.toString());
				mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmentlink",Boolean.toString(result.isConditionTrue())));
				mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmentlink.files",files));
			}
		}
		
	}
}
