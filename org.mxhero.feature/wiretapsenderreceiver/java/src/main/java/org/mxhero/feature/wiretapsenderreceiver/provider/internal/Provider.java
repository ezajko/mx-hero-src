package org.mxhero.feature.wiretapsenderreceiver.provider.internal;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String EMAIL_PROPERTY="email.value";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String backupRecipient = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(EMAIL_PROPERTY)){
				backupRecipient=property.getValue();
			} 
		}
		
		coreRule.addEvaluation(new WTEvaluate(coreRule.getGroup()));
		coreRule.addAction(new WTAction(coreRule.getId(), coreRule.getGroup(), backupRecipient));
		
		return coreRule;
	}

	private class WTEvaluate implements Evaluable{

		private String group;
		
		public WTEvaluate(String group) {
			super();
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
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
			mail.getProperties().put("org.mxhero.feature.wiretapsenderreceiver:"+group,ruleId.toString());
			for(String individualMail : backupRecipient.split(",")){
				try {
					InternetAddress emailAddress = new InternetAddress(individualMail,false);
					if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
						mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
						mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
					}
				} catch (AddressException e) {
					log.warn("wrong email address",e);
				}
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapsenderreceiver", mail.getSender().getMail()+";"+mail.getRecipient().getMail()));
		}
		
	}
	
}
