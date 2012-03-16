package org.mxhero.feature.redirect.provider.internal;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
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
		
		coreRule.addEvaluation(new REvaluate(coreRule.getGroup()));
		coreRule.addAction(new RAction(coreRule.getId(), coreRule.getGroup(), redirectRecipient));
		
		return coreRule;
	}

	private class REvaluate implements Evaluable{

		private String group;
	
		public REvaluate(String group) {
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
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
			mail.getProperties().put("org.mxhero.feature.redirect:"+group, ruleId.toString());
			for(String individualMail : redirectRecipient.split(",")){
				try {
					InternetAddress emailAddress = new InternetAddress(individualMail,false);
					if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
						mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
						mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),emailAddress.getAddress());					
					}
				} catch (AddressException e) {
					log.warn("wrong email address",e);
				}
			}
			mail.drop("org.mxhero.feature.redirect");
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.redirect.recipient",redirectRecipient);
		}
		
	}
	
}
