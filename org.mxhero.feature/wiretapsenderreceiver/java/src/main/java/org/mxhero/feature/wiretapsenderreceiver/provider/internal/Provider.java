package org.mxhero.feature.wiretapsenderreceiver.provider.internal;

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

	private static final String EMAIL_PROPERTY="email.value";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String backupRecipient = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(EMAIL_PROPERTY)){
				backupRecipient=property.getPropertyValue();
			} 
		}
		
		coreRule.addEvaluation(new WTEvaluate(coreRule.getGroup(), backupRecipient));
		coreRule.addAction(new WTAction(coreRule.getId(), coreRule.getGroup(), backupRecipient));
		
		return coreRule;
	}

	private class WTEvaluate implements Evaluable{

		private String group;
		private String backupRecipient;
		
		public WTEvaluate(String group, String backupRecipient) {
			super();
			this.group = group;
			this.backupRecipient = backupRecipient;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("redirected:"+backupRecipient)
			&& !mail.getProperties().containsKey("org.mxhero.feature.wiretapsenderreceiver:"+group);
		}
		
	}
	
	private class WTAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String backupRecipient;
		
		public WTAction(Integer ruleId, String group, String backupRecipient) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.backupRecipient = backupRecipient;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-WiretapSenderReceiver","rule:"+ruleId+";hidden_copied:"+backupRecipient);
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),backupRecipient);
			mail.getProperties().put("redirected:"+backupRecipient,ruleId.toString());
			mail.getProperties().put("org.mxhero.feature.wiretapsenderreceiver:"+group,ruleId.toString());
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.wiretapsenderreceiver",mail.getInitialData().getSender().getMail()+";"+mail.getInitialData().getRecipient().getMail() );
		}
		
	}
	
}
