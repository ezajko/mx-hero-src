package org.mxhero.feature.instantalias.provider.internal;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature {

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
							&& !separationCharacter.trim().isEmpty() && mail
							.getInitialData().getRecipient().getMail()
							.contains(separationCharacter.trim()));
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
			String realEmail=mail.getInitialData().getRecipient().getMail().substring(0, separatorInit).toString()
					+mail.getInitialData().getRecipient().getMail().substring(aliasEnd).toString();
			mail.getHeaders().addHeader("X-mxHero-InstantAlias","rule="+ruleId+";alias="+mail.getInitialData().getRecipient().getMail());
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),realEmail);
			mail.drop("org.mxhero.feature.instantalias");
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.instantalias.alias",mail.getInitialData().getRecipient().getMail());

		}

	}
}
