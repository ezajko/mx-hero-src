package org.mxhero.feature.blocklist.provider.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String RETURN_TEXT = "return.text";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		String returnText = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ACTION_SELECTION)){
				action=property.getPropertyValue();
			} else if (property.getPropertyKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", ""));
				}else{
					accounts.add(value);
				}
			} else if (property.getPropertyKey().equals(RETURN_TEXT)){
				returnText = property.getPropertyValue();
			} 
		}
		
		coreRule.addEvaluation(new BLEvaluation(coreRule.getGroup(),accounts,domains));
		coreRule.addAction(new BLAction(rule.getId(), coreRule.getGroup(), returnText, getNoReplyEmail(rule.getDomain()), action));
		
		return coreRule;
	}
	
	private class BLEvaluation implements Evaluable{

		private String group;
		Collection<String> accounts;
		Collection<String> domains;

		public BLEvaluation(String group, Collection<String> accounts,
				Collection<String> domains) {
			this.group = group;
			this.accounts = accounts;
			this.domains = domains;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.blocklist:"+group)
			&& (mail.getInitialData().getFromSender().getDomain().hasAlias(domains)
				|| mail.getInitialData().getSender().getDomain().hasAlias(domains)
				|| mail.getInitialData().getFromSender().hasAlias(accounts)
				|| mail.getInitialData().getSender().hasAlias(accounts));
		}
		
	}
	
	private class BLAction implements Actionable{

		private static final String ACTION_RETURN = "return";
		
		private Integer ruleId;
		private String group;
		private String returnText;
		private String replyMail;
		private String action;
		
		public BLAction(Integer ruleId, String group, String returnText,
				String replyMail, String action) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnText = returnText;
			this.replyMail = replyMail;
			this.action = action;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.blocklist:"+group,ruleId.toString());
			mail.getHeaders().addHeader("X-mxHero-BlockList", "rule="+ruleId.toString());
			mail.drop("org.mxhero.feature.blocklist");
			if(action.equalsIgnoreCase(ACTION_RETURN)){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Replay",replyMail,returnText,RulePhase.SEND,mail.getInitialData().getSender().getMail() );
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.blocklist.sender",mail.getInitialData().getSender().getMail());
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.blocklist");			
		}
		
	}

}
