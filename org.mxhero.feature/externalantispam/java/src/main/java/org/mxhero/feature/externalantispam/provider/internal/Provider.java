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

package org.mxhero.feature.externalantispam.provider.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_REJECT = "reject";
	private static final String HEADER_VALUE = "header.value";
	private static final String HEADER_KEY = "header.key";
	private static final String ADD_HEADER_VALUE = "add.header.value";
	private static final String ADD_HEADER_KEY = "add.header.key";
	private static final String EMAIL_LIST = "email.list";
	private static final String PREFIX_VALUE = "prefix.value";
	private static final String HEADER_MANAGED = "header.managed";
	private static final String REMOVE_HEADER = "remove.header";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		String headerKey = "";
		String headerValue = "";
		String addHeaderKey = "";
		String addHeaderValue = "";
		String removeHeader = null;
		String prefix = "";
		boolean managed = false;
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();

		for (RuleProperty property : rule.getProperties()) {
			if (property.getKey().equals(ACTION_SELECTION)) {
				action = property.getValue();
			} else if (property.getKey().equals(HEADER_VALUE)) {
				headerValue = property.getValue().trim();
			} else if (property.getKey().equals(HEADER_KEY)) {
				headerKey = property.getValue().trim();
			} else if (property.getKey().equals(HEADER_MANAGED)) {
				managed = Boolean.parseBoolean(property.getValue());
			} else if (property.getKey().equals(ADD_HEADER_VALUE)) {
				addHeaderValue = StringEscapeUtils.escapeJava(property
						.getValue().trim());
			} else if (property.getKey().equals(ADD_HEADER_KEY)) {
				addHeaderKey = StringEscapeUtils.escapeJava(property
						.getValue().trim());
			} else if (property.getKey().equals(EMAIL_LIST)) {
				String value = StringEscapeUtils.escapeJava(property
						.getValue().trim().toLowerCase());
				if (value.startsWith("@")) {
					domains.add(value.replace("@", "").toLowerCase());
				} else {
					accounts.add(value.toLowerCase());
				}
			} else if (property.getKey().equals(PREFIX_VALUE)) {
				prefix = StringEscapeUtils.escapeJava(property
						.getValue().trim());
			} else if (property.getKey().equals(REMOVE_HEADER)){
				removeHeader = StringEscapeUtils.escapeJava(property
						.getValue().trim());
			}
		}
		
		coreRule.addEvaluation(new ESEvaluate(coreRule.getGroup(), headerKey));
		coreRule.addAction(new ESAction(coreRule.getId(), coreRule.getGroup(), action, headerKey, headerValue, addHeaderKey, addHeaderValue, removeHeader, prefix, managed, accounts, domains));
		
		return coreRule;
	}

	private class ESEvaluate implements Evaluable{

		private String group;
		private String headerKey;

		public ESEvaluate(String group, String headerKey) {
			this.group = group;
			this.headerKey = headerKey;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& mail.getSubject()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.externalantispam:"+group)
			&& mail.getHeaders().hasHeader(headerKey);
		}

	}
	
	private class ESAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String action;
		private String headerKey;
		private String headerValue;
		private String addHeaderKey;
		private String addHeaderValue;
		private String removeHeader;
		private String prefix;
		private boolean managed;
		private Set<String> accounts;
		private Set<String> domains;
		
		public ESAction(Integer ruleId, String group, String action,
				String headerKey, String headerValue, String addHeaderKey,
				String addHeaderValue, String removeHeader, String prefix,
				boolean managed, Set<String> accounts, Set<String> domains) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.action = action;
			this.headerKey = headerKey;
			this.headerValue = headerValue;
			this.addHeaderKey = addHeaderKey;
			this.addHeaderValue = addHeaderValue;
			this.removeHeader = removeHeader;
			this.prefix = prefix;
			this.managed = managed;
			this.accounts = accounts;
			this.domains = domains;
		}

		@Override
		public void exec(Mail mail) {
			boolean isSpam = false;
			boolean isException = false;
			
			//check domain white list
			if(mail.getFromSender().getDomain().hasAlias(domains)
				|| mail.getSender().getDomain().hasAlias(domains)){
				isException = true;
			}
			//check account white list
			if(mail.getFromSender().hasAlias(accounts.toArray(new String[accounts.size()]))
				|| mail.getSender().hasAlias(accounts.toArray(new String[accounts.size()]))){
				isException = true;
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.externalantispam.exception", Boolean.toString(isException)));
			//if in white list and remove header is check
			if(removeHeader!=null
				&& isException){
				mail.getHeaders().removeHeader(removeHeader);
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.externalantispam.removed.header", removeHeader));
			}
			if(!isException){
				if(managed){
					isSpam=mail.getHeaders().getHeaderValue(headerKey).matches(headerValue);
				}else{
					isSpam=mail.getHeaders().getHeaderValue(headerKey).equalsIgnoreCase(headerValue);
				}
			}
			//add header so it wont be checked again if this is an spam or an exception
			if(isException || isSpam){
				mail.getProperties().put("org.mxhero.feature.externalantispam:"+group,ruleId.toString());
			}
			if(isSpam){
				mail.getProperties().put("spam.detected",Boolean.toString(isSpam));
			}
			mail.getHeaders().addHeader("X-mxHero-ExternalAntispam","rule="+ruleId+";spam="+Boolean.toString(isSpam));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.externalantispam", Boolean.toString(isSpam)));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("spam.detected", Boolean.toString(isSpam)));
			
			if (!isException && isSpam && action.equals(ACTION_REJECT)) {
				mail.drop("org.mxhero.feature.externalantispam");
			}
			
			if(!isException && isSpam ){
				if (addHeaderKey != null && !addHeaderKey.trim().isEmpty()
						&& addHeaderValue != null
						&& !addHeaderValue.trim().isEmpty()){
					mail.getHeaders().addHeader(addHeaderKey, addHeaderValue);
				}
				if (prefix != null && prefix.trim().length() > 0){
					mail.setSubject(prefix+mail.getSubject());
				}
			}
		}
		
	}
	
}
