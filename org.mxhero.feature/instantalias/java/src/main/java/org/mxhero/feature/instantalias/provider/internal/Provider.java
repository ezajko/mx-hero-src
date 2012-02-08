package org.mxhero.feature.instantalias.provider.internal;

import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;

public class Provider extends RulesByFeatureWithFixed {

	private static final String SEPARATION_CHARACTER = "separation.charater";

	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String separationCharacter = null;
		for (RuleProperty property : rule.getProperties()) {
			if (property.getPropertyKey().equals(SEPARATION_CHARACTER)) {
				separationCharacter = property.getPropertyValue();
			}
		}
		coreRule.addEvaluation(new IAEvaluate(separationCharacter));
		coreRule.addAction(new IAAction(separationCharacter, rule.getId()));
		return coreRule;
	}

	private class IAEvaluate implements Evaluable {
		String separationCharacter = null;

		public IAEvaluate(String separationCharacter) {
			this.separationCharacter = separationCharacter;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
					&& (separationCharacter != null
							&& !separationCharacter.trim().isEmpty() 
							&& mail.getInitialData().getRecipient().getMail().contains(separationCharacter.trim())
							&& !mail.getInitialData().getRecipient().getMail().startsWith(REPLY_START));
		}
	}

	private class IAAction implements Actionable {
		String separationCharacter = null;
		private Integer ruleId;
		
		public IAAction(String separationCharacter, Integer ruleId) {
			this.separationCharacter = separationCharacter;
			this.ruleId=ruleId;
		}

		@Override
		public void exec(Mail mail) {
			int separatorInit=mail.getInitialData().getRecipient().getMail().indexOf(separationCharacter.trim());
			int aliasEnd = mail.getInitialData().getRecipient().getMail().indexOf("@");
			String account = mail.getInitialData().getRecipient().getMail().substring(0, separatorInit).toString();
			String domain = mail.getInitialData().getRecipient().getMail().substring(aliasEnd+1).toString();
			String alias = mail.getInitialData().getRecipient().getMail().substring(separatorInit+1, aliasEnd).toString();
			String realEmail= account+"@"+domain;
			String replyTo=null;
			try{replyTo = mail.getHeaders().getHeaderValue("Reply-To");
				replyTo = new InternetAddress(replyTo,false).getAddress();
			}catch (Exception e){replyTo = null;}
			if(replyTo==null || replyTo.trim().length()<3){
				try{replyTo = mail.getHeaders().getHeaderValue("From");
					replyTo = new InternetAddress(replyTo,false).getAddress();
				}catch (Exception e){replyTo = null;}
			}
			if(replyTo==null || replyTo.trim().length()<3){
				replyTo=mail.getInitialData().getSender().getMail();
			}
			replyTo = REPLY_START+REPLY_ALIAS+account+separationCharacter.trim()+alias+REPLY_ALIAS+domain+REPLY_ALIAS+replyTo;
			mail.getHeaders().removeHeader("Reply-To");
			mail.getHeaders().addHeader("Reply-To",replyTo);
			mail.getHeaders().addHeader("X-mxHero-InstantAlias","rule="+ruleId+";alias="+mail.getInitialData().getRecipient().getMail());
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),realEmail);
			mail.drop("org.mxhero.feature.instantalias");
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.instantalias.alias",mail.getInitialData().getRecipient().getMail());

		}

	}
}
