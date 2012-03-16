package org.mxhero.feature.backupcopy.provider.internal;

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
	
	private static final String EMAIL_PROPERTY="email.value";
	private static final String SPAM_CHECK_PROPERTY="spam.check";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String backupRecipient = null;
		boolean checkSpam = false;

		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(EMAIL_PROPERTY)){
				backupRecipient=property.getPropertyValue();
			} else if(property.getPropertyKey().equals(SPAM_CHECK_PROPERTY)){
				checkSpam = Boolean.parseBoolean(property.getPropertyValue());
			}
		}
		
		coreRule.addEvaluation(new BCEvaluation(coreRule.getGroup(), backupRecipient, checkSpam));
		coreRule.addAction(new BCAction(rule.getId(), coreRule.getGroup() ,backupRecipient));
		
		return coreRule;
	}
	
	private class BCEvaluation implements Evaluable{

		private String group;
		private String backupRecipient;
		private boolean checkSpam;

		public BCEvaluation(String group, String backupRecipient, boolean checkSpam) {
			this.group = group;
			this.backupRecipient = backupRecipient;
			this.checkSpam = checkSpam;
		}

		@Override
		public boolean eval(Mail mail) {
			
			if(log.isTraceEnabled()){
				log.trace("backupRecipient:"+backupRecipient
						+" group:"+group
						+" checkSpam:"+checkSpam);
				log.trace("(state=deliver):"+mail.getState().equalsIgnoreCase(MailState.DELIVER)
						+" (headers!=null)"+(mail.getHeaders()!=null)
						+" (!mail.getProperties().containsKey(\"org.mxhero.feature.backupcopy:"+group+"\"):"+!mail.getProperties().containsKey("org.mxhero.feature.backupcopy:"+group)
						+" (!checkSpam || (checkSpam && !mail.getProperties().containsKey(\"spam.detected\")):"+(!checkSpam || (checkSpam && !mail.getProperties().containsKey("spam.detected"))));
			}
			return 	mail.getState().equalsIgnoreCase(MailState.DELIVER) 
				&&	mail.getHeaders()!=null
				&&	!mail.getProperties().containsKey("redirected:"+backupRecipient)
				&& 	!mail.getProperties().containsKey("org.mxhero.feature.backupcopy:"+group)
				&& (!checkSpam || (checkSpam && !mail.getProperties().containsKey("spam.detected")));
		}
		
	}
	
	private class BCAction implements Actionable{

		private Integer ruleId;
		private String group;
		private String backupRecipient;
		
		public BCAction(Integer ruleId, String group, String backupRecipient) {
			this.ruleId = ruleId;
			this.group = group;
			this.backupRecipient = backupRecipient;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-BackupCopy", "rule="+ruleId+";redirected:"+backupRecipient);
			mail.getProperties().put("org.mxhero.feature.backupcopy:"+group,ruleId.toString());
			for(String individualMail : backupRecipient.split(",")){
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
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.backupcopy.recipient",backupRecipient);
		}	
	}

}
