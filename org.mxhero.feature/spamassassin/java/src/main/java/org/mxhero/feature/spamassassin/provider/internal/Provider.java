package org.mxhero.feature.spamassassin.provider.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.spamd.command.SpamScan;
import org.mxhero.engine.plugin.spamd.command.SpamScanParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_REJECT = "reject";
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
			if(property.getKey().equals(ACTION_SELECTION)){
				action=property.getValue();
			} else if(property.getKey().equals(HEADER_VALUE)){
				header=StringEscapeUtils.escapeJava(property.getValue().trim());
			} else if (property.getKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", ""));
				}else{
					accounts.add(value);
				}
			} else if (property.getKey().equals(PREFIX_VALUE)){
				prefix = StringEscapeUtils.escapeJava(property.getValue().trim());
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
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.spamassassin:"+group)
			&& (!mail.getSender().getDomain().hasAlias(domains))
			&& (!mail.getSender().hasAlias(accounts.toArray(new String[accounts.size()])));
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
			Result spamResult = mail.cmd(SpamScan.class.getName(),new SpamScanParameters(prefix, true));	 
			mail.getHeaders().addHeader("X-mxHero-SpamAssassin","rule="+ruleId+";result="+spamResult.getMessage());
			if(spamResult.isConditionTrue()){
				mail.getProperties().put("spam.detected","true");
				if(action.equals(ACTION_REJECT)){
					mail.drop("org.mxhero.feature.spamassassin");
				}
				if(header!=null && !header.trim().isEmpty()){
					mail.getHeaders().addHeaderLine(header);
				}
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.spamassassin", Boolean.toString(spamResult.isConditionTrue())));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("spam.detected", Boolean.toString(spamResult.isConditionTrue())));
		}
		
	}
	
}
