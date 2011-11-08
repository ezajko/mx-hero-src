package org.mxhero.feature.limituserdestinations.internal;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleProperty;
import org.mxhero.engine.domain.mail.business.Mail;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.rules.Actionable;
import org.mxhero.engine.domain.rules.CoreRule;
import org.mxhero.engine.domain.rules.Evaluable;
import org.mxhero.engine.domain.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{
	
	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_TEXT = "return.text";
	private static final String OWN_DOMAIN_SELECTED = "own.domain.selected";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		boolean ownDomain = false;
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
			} else if (property.getPropertyKey().equals(OWN_DOMAIN_SELECTED)){
				ownDomain = Boolean.parseBoolean(property.getPropertyValue());
			}
		}
		
		coreRule.addEvaluation(new LDEvaluate(ownDomain, accounts, domains));
		coreRule.addAction(new LDAction(coreRule.getId(), action, returnText, getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}

	private class LDEvaluate implements Evaluable{

		boolean ownDomain = false;
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		
		public LDEvaluate(boolean ownDomain, Set<String> accounts,
				Set<String> domains) {
			super();
			this.ownDomain = ownDomain;
			this.accounts = accounts;
			this.domains = domains;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& (!mail.getInitialData().getRecipient().getDomain().hasAlias(domains))
			&& (!mail.getInitialData().getRecipient().hasAlias(accounts))
			&& (!ownDomain || (mail.getInitialData().getFromSender().getDomain().getId().equals(mail.getInitialData().getRecipient().getDomain().getId()) == false && mail.getInitialData().getSender().getDomain().getId().equals(mail.getInitialData().getRecipient().getDomain().getId()) == false));
		}
		
	}
	
	private class LDAction implements Actionable{

		private Integer ruleId;
		private String action;
		private String returnText;
		private String replyMail;

		public LDAction(Integer ruleId, String action, String returnText,
				String replyMail) {
			super();
			this.ruleId = ruleId;
			this.action = action;
			this.returnText = returnText;
			this.replyMail = replyMail;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-LimitUserDestinations","rule="+ruleId);
			mail.drop("org.mxhero.feature.limituserdestinations");
			if(action.equalsIgnoreCase(ACTION_RETURN)){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Replay",replyMail,returnText,RulePhase.SEND,mail.getInitialData().getSender().getMail() );
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.limituserdestinations.recipient",mail.getInitialData().getRecipient().getMail());
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.limituserdestinations");
		}
		
	}
}
