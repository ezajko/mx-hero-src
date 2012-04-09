package org.mxhero.feature.instantalias.provider.internal;

import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeatureWithFixed {

	private static final String SEPARATION_CHARACTER = "separation.charater";

	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String separationCharacter = null;
		for (RuleProperty property : rule.getProperties()) {
			if (property.getKey().equals(SEPARATION_CHARACTER)) {
				separationCharacter = property.getValue();
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
			return mail.getStatus().equals(Mail.Status.deliver)
					&& (separationCharacter != null
							&& !separationCharacter.trim().isEmpty() 
							&& mail.getRecipient().getMail().contains(separationCharacter.trim())
							&& !mail.getRecipient().getMail().startsWith(REPLY_START));
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
			int separatorInit=mail.getRecipient().getMail().indexOf(separationCharacter.trim());
			int aliasEnd = mail.getRecipient().getMail().indexOf("@");
			String account = mail.getRecipient().getMail().substring(0, separatorInit).toString();
			String domain = mail.getRecipient().getMail().substring(aliasEnd+1).toString();
			String alias = mail.getRecipient().getMail().substring(separatorInit+1, aliasEnd).toString();
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
				replyTo=mail.getSender().getMail();
			}
			replyTo = REPLY_START+REPLY_ALIAS+account+separationCharacter.trim()+alias+REPLY_ALIAS+domain+REPLY_ALIAS+replyTo;
			mail.getHeaders().removeHeader("Reply-To");
			mail.getHeaders().addHeader("Reply-To",replyTo);
			mail.getHeaders().addHeader("X-mxHero-InstantAlias","rule="+ruleId+";alias="+mail.getRecipient().getMail());
			CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(),realEmail);
			mail.cmd(Clone.class.getName(),cloneParameters);
			mail.drop("org.mxhero.feature.instantalias");
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.instantalias.alias", mail.getRecipient().getMail()));

		}

	}
}
