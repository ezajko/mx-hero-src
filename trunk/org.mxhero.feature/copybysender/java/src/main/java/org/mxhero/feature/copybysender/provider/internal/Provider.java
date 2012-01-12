package org.mxhero.feature.copybysender.provider.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.User;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String EMAIL_LIST = "email.list";
	private static final String SUBJECT_PATTERN = "subject.pattern";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		Set<String> accounts = new HashSet<String>();
		String subjectPattern = "";
		
		for(RuleProperty property : rule.getProperties()){
			if (property.getPropertyKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
				accounts.add(value);
			} else if (property.getPropertyKey().equals(SUBJECT_PATTERN)){
				subjectPattern = property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new CBSEval(accounts,subjectPattern));
		
		return coreRule;
	}

	private class CBSEval implements Evaluable{
		Set<String> accounts = new HashSet<String>();
		String subjectPattern = "";
		
		public CBSEval(Set<String> accounts, String subjectPattern) {
			this.accounts = accounts;
			this.subjectPattern = subjectPattern;
		}

		@Override
		public boolean eval(Mail mail) {
			 
			return (mail.getState().equalsIgnoreCase(MailState.DELIVER)
					&& !mail.getProperties().containsKey("org.mxhero.feature.copybysender")
					&& !mail.getInitialData().getRecipient().hasAlias(accounts) && !anyEmailInRecipients(mail.getInitialData().getRecipientsInHeaders(),accounts)
					&& (subjectPattern==null || subjectPattern.trim().isEmpty()|| mail.getSubject().getSubject().matches(".*"+subjectPattern+".*")));
		}
	
		private boolean anyEmailInRecipients(List<User> recipientsInHeaders, Collection<String> accounts){
			for(User user : recipientsInHeaders){
				if(user.hasAlias(accounts)){
					return true;
				}
			}
			return false;
		}
		
	}
	
	private class CBSAction implements Actionable{

		@Override
		public void exec(Mail arg0) {
			// TODO Auto-generated method stub
			
		}
		
		private boolean hasBeenSent(String email){
			return false;
		}
		
	}
}
