package org.mxhero.feature.backupcopy.internal;

import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleProperty;
import org.mxhero.engine.domain.mail.business.Mail;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.rules.Actionable;
import org.mxhero.engine.domain.rules.CoreRule;
import org.mxhero.engine.domain.rules.Evaluable;
import org.mxhero.engine.domain.rules.provider.RulesByFeature;
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
						+" (!mail.getProperties().containsKey(\"redirected:"+backupRecipient+"\")):"+!mail.getProperties().containsKey("redirected:"+backupRecipient)
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
			mail.getProperties().put("redirected:"+backupRecipient,ruleId.toString());
			mail.getProperties().put("org.mxhero.feature.backupcopy:"+group,ruleId.toString());
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),backupRecipient);
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.backupcopy.recipient",backupRecipient);
		}	
	}

}
