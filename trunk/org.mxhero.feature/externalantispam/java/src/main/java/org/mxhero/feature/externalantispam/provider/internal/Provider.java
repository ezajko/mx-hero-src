package org.mxhero.feature.externalantispam.provider.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_REJECT = "reject";
	private static final String ACTION_RECEIVE = "receive";
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
			if (property.getPropertyKey().equals(ACTION_SELECTION)) {
				action = property.getPropertyValue();
			} else if (property.getPropertyKey().equals(HEADER_VALUE)) {
				headerValue = property.getPropertyValue().trim();
			} else if (property.getPropertyKey().equals(HEADER_KEY)) {
				headerKey = property.getPropertyValue().trim();
			} else if (property.getPropertyKey().equals(HEADER_MANAGED)) {
				managed = Boolean.parseBoolean(property.getPropertyValue());
			} else if (property.getPropertyKey().equals(ADD_HEADER_VALUE)) {
				addHeaderValue = StringEscapeUtils.escapeJava(property
						.getPropertyValue().trim());
			} else if (property.getPropertyKey().equals(ADD_HEADER_KEY)) {
				addHeaderKey = StringEscapeUtils.escapeJava(property
						.getPropertyValue().trim());
			} else if (property.getPropertyKey().equals(EMAIL_LIST)) {
				String value = StringEscapeUtils.escapeJava(property
						.getPropertyValue().trim());
				if (value.startsWith("@")) {
					domains.add(value.replace("@", ""));
				} else {
					accounts.add(value);
				}
			} else if (property.getPropertyKey().equals(PREFIX_VALUE)) {
				prefix = StringEscapeUtils.escapeJava(property
						.getPropertyValue().trim());
			} else if (property.getPropertyKey().equals(REMOVE_HEADER)){
				removeHeader = StringEscapeUtils.escapeJava(property
						.getPropertyValue().trim());
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
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
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
			if(mail.getInitialData().getFromSender().getDomain().hasAlias(domains)
				|| mail.getInitialData().getSender().getDomain().hasAlias(domains)){
				isException = true;
			}
			//check account white list
			if(mail.getInitialData().getFromSender().hasAlias(accounts)
				|| mail.getInitialData().getSender().hasAlias(accounts)){
				isException = true;
			}
			//if in white list and remove header is check
			if(removeHeader!=null
				&& isException){
				mail.getHeaders().removeHeader(removeHeader);
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
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.externalantispam",Boolean.toString(isSpam) );
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","spam.detected",Boolean.toString(isSpam) );
			
			if (!isException && isSpam && action.equals(ACTION_REJECT)) {
				mail.drop("org.mxhero.feature.externalantispam");
			}else if(!isException && isSpam && action.equals(ACTION_RECEIVE)){
				if (addHeaderKey != null && !addHeaderKey.trim().isEmpty()
						&& addHeaderValue != null
						&& !addHeaderValue.trim().isEmpty()){
					mail.getHeaders().addHeader(addHeaderKey, addHeaderValue);
				}
				if (prefix != null && prefix.trim().length() > 0){
					mail.getSubject().setSubject(prefix+mail.getSubject().getSubject());
				}
			}
		}
		
	}
	
}
