package org.mxhero.feature.spamassassin.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleProperty;
import org.mxhero.engine.domain.mail.business.Mail;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.domain.rules.Actionable;
import org.mxhero.engine.domain.rules.CoreRule;
import org.mxhero.engine.domain.rules.Evaluable;
import org.mxhero.engine.domain.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_REJECT = "reject";
	private static final String ACTION_RECEIVE = "receive";
	private static final String HEADER_VALUE = "header.value";
	private static final String EMAIL_LIST = "email.list";
	private static final String PREFIX_VALUE = "prefix.value";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		String header = "";
		String prefix = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ACTION_SELECTION)){
				action=property.getPropertyValue();
			} else if(property.getPropertyKey().equals(HEADER_VALUE)){
				header=StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
			} else if (property.getPropertyKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", ""));
				}else{
					accounts.add(value);
				}
			} else if (property.getPropertyKey().equals(PREFIX_VALUE)){
				prefix = StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
			}
		}
		
		coreRule.addEvaluation(new SAEvaluate(coreRule.getGroup(), accounts, domains));
		coreRule.addAction(new SAAction(coreRule.getGroup(), coreRule.getId(), prefix, action, header));
		
		return coreRule;
	}

	private class SAEvaluate implements Evaluable{

		private String group;
		private Set<String> accounts = new HashSet<String>();
		private Set<String> domains = new HashSet<String>();

		public SAEvaluate(String group, Set<String> accounts,
				Set<String> domains) {
			super();
			this.group = group;
			this.accounts = accounts;
			this.domains = domains;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.spamassassin:"+group)
			&& (!mail.getInitialData().getSender().getDomain().hasAlias(domains))
			&& (!mail.getInitialData().getSender().hasAlias(accounts));
		}
		
	}
	
	private class SAAction implements Actionable{

		private String group;
		private Integer ruleId;
		private String prefix;
		private String action;
		private String header;
				
		public SAAction(String group, Integer ruleId, String prefix,
				String action, String header) {
			super();
			this.group = group;
			this.ruleId = ruleId;
			this.prefix = prefix;
			this.action = action;
			this.header = header;
		}

		@Override
		public void exec(Mail mail) {
			 
			mail.getProperties().put("org.mxhero.feature.spamassassin:"+group,ruleId.toString()); 
			Result spamResult = mail.cmd("org.mxhero.engine.plugin.spamd.command.SpamScan",prefix,"true");	 
			mail.getHeaders().addHeader("X-mxHero-SpamAssassin","rule="+ruleId+";result="+spamResult.getText());
			if(spamResult.isTrue()){
				mail.getProperties().put("spam.detected","true");
				if(action.equals(ACTION_REJECT)){
					mail.drop("org.mxhero.feature.spamassassin");
				}else if(action.equals(ACTION_RECEIVE)){
					if(header!=null && !header.trim().isEmpty()){
						mail.getHeaders().addHeaderLine(header);
					}
				}
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.spamassassin",Boolean.toString(spamResult.isTrue()) );
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","spam.detected",Boolean.toString(spamResult.isTrue()) );
		}
		
	}
	
}
