package org.mxhero.feature.redirect.internal;

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

	private static final String REDIRECT_PROPERTY="redirect.email";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String redirectRecipient = null;

		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(REDIRECT_PROPERTY)){
				redirectRecipient=property.getPropertyValue();
			} 
		}
		
		coreRule.addEvaluation(new REvaluate(coreRule.getGroup(), redirectRecipient));
		coreRule.addAction(new RAction(coreRule.getId(), coreRule.getGroup(), redirectRecipient));
		
		return coreRule;
	}

	private class REvaluate implements Evaluable{

		private String group;
		private String redirectRecipient;
	
		public REvaluate(String group, String redirectRecipient) {
			super();
			this.group = group;
			this.redirectRecipient = redirectRecipient;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("redirected:"+redirectRecipient)
			&& !mail.getProperties().containsKey("org.mxhero.feature.redirect:"+group);

		}
		
	}
	
	private class RAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String redirectRecipient;

		public RAction(Integer ruleId, String group, String redirectRecipient) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.redirectRecipient = redirectRecipient;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-Redirect","rule="+ruleId+";recipient="+mail.getInitialData().getRecipient().getMail()+";redirected="+redirectRecipient);
			mail.getProperties().put("redirected:"+redirectRecipient,ruleId.toString());
			mail.getProperties().put("org.mxhero.feature.redirect:"+group, ruleId.toString());
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),redirectRecipient);
			mail.drop("org.mxhero.feature.redirect");
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.redirect.recipient",redirectRecipient);
		}
		
	}
	
}
