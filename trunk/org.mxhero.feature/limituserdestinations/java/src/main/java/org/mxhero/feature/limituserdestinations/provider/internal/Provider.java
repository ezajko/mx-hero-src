package org.mxhero.feature.limituserdestinations.provider.internal;

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
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{
	
	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_TEXT = "return.text";
	private static final String RETURN_TEXT_PLAIN = "return.text.plain";
	private static final String OWN_DOMAIN_SELECTED = "own.domain.selected";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		boolean ownDomain = false;
		String returnText = "";
		String returnTextPlain = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ACTION_SELECTION)){
				action=property.getValue();
			} else if (property.getKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", "").toLowerCase());
				}else{
					accounts.add(value.toLowerCase());
				}
			} else if (property.getKey().equals(RETURN_TEXT)){
				returnText = property.getValue();
			} else if (property.getKey().equals(RETURN_TEXT_PLAIN)){
				returnTextPlain = property.getValue();
			} else if (property.getKey().equals(OWN_DOMAIN_SELECTED)){
				ownDomain = Boolean.parseBoolean(property.getValue());
			}
		}
		
		coreRule.addEvaluation(new LDEvaluate(ownDomain, accounts, domains));
		coreRule.addAction(new LDAction(coreRule.getId(), action, returnTextPlain, returnText, getNoReplyEmail(rule.getDomain())));
		
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
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& (!mail.getRecipient().getDomain().hasAlias(domains))
			&& (!mail.getRecipient().hasAlias(accounts.toArray(new String[accounts.size()])))
			&& (!ownDomain || (mail.getFromSender().getDomain().getId().equals(mail.getRecipient().getDomain().getId()) == false && mail.getSender().getDomain().getId().equals(mail.getRecipient().getDomain().getId()) == false));
		}
		
	}
	
	private class LDAction implements Actionable{

		private Integer ruleId;
		private String action;
		private String returnText;
		private String returnTextPlain;
		private String replyMail;

		public LDAction(Integer ruleId, String action, String returnTextPlain, String returnText,
				String replyMail) {
			super();
			this.ruleId = ruleId;
			this.action = action;
			this.returnText = returnText;
			this.replyMail = replyMail;
			this.returnTextPlain = returnTextPlain;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-LimitUserDestinations","rule="+ruleId);
			mail.drop("org.mxhero.feature.limituserdestinations");
			if(action.equalsIgnoreCase(ACTION_RETURN)){
				ReplyParameters replyParameters = new ReplyParameters(replyMail, returnTextPlain, returnText);
				replyParameters.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(),replyParameters);
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.limituserdestinations.recipient",mail.getRecipient().getMail()));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.limituserdestinations"));
		}
		
	}
}
