package org.mxhero.feature.addressprotection.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	public static final String EMAIL_LIST = "email.list";
	public static final String PROTECTED_SELECTION = "protected.selection";
	public static final String CC_VALUE = "cc";
	public static final String CCTO_VALUE = "ccto";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String protectedSelection = null;
		List<String> accounts = new ArrayList<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(PROTECTED_SELECTION)){
				protectedSelection=property.getValue();
			} else if (property.getKey().equals(EMAIL_LIST)){
				accounts.add(StringEscapeUtils.escapeJava(property.getValue().trim()));
			}
		}
		
		coreRule.addEvaluation(new APEvaluation(protectedSelection,accounts));
		coreRule.addAction(new APAction(rule.getId(),protectedSelection,accounts));
		
		return coreRule;
	}

	private class APEvaluation implements Evaluable{

		private String protectedSelection = null;
		private List<String> accounts;

		public APEvaluation(String protectedSelection,List<String> accounts) {
			this.accounts = accounts;
			this.protectedSelection = protectedSelection;
		}

		@Override
		public boolean eval(Mail mail) {
			log.debug("protectedSelection:"+protectedSelection+" accounts:"+Arrays.deepToString(accounts.toArray()));
			return mail.getStatus().equals(Mail.Status.deliver) 
					&& hasEmailInHeader(mail,accounts);
		}
		
		private boolean hasEmailInHeader(Mail mail, List<String> accounts){
			Collection<String> mailCC = mail.getRecipients().getRecipients(RecipientType.cc);
			Collection<String> mailTO = mail.getRecipients().getRecipients(RecipientType.to);
			
			for(User user : mail.getRecipientsInHeaders()){
				if(accounts!=null){
					if(user.hasAlias(accounts.toArray(new String[accounts.size()]))){
						if(mailCC!=null){
							if(user.hasAlias(mailCC.toArray(new String[mailCC.size()]))){
								log.debug("alias found in CC for user "+user.getMail());
								return true;
							}
						}
						if(CCTO_VALUE.equals(protectedSelection) && mailTO!=null){
							if(user.hasAlias(mailTO.toArray(new String[mailTO.size()]))){
								log.debug("alias found in TO for user "+user.getMail());
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		
	}
	
	private class APAction implements Actionable{

		private Integer ruleId;
		private String protectedSelection;
		private List<String> accounts;

		public APAction(Integer ruleId, String protectedSelection, List<String> accounts) {
			this.protectedSelection = protectedSelection;
			this.accounts = accounts;
			this.ruleId = ruleId;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-AddressProtection", "rule="+ruleId);
			String removedStr = Arrays.deepToString(removeEmailInHeader(mail).toArray()).replace("[","").replace("]","");
			if(mail.getRecipients().getRecipients(RecipientType.to)==null || mail.getRecipients().getRecipients(RecipientType.to).size()<1){
				mail.getHeaders().removeHeader("To");
				mail.getHeaders().addHeader("To", "undisclosed-recipients:;");
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.addressprotection", removedStr));
		}
		
		private Collection<String> removeEmailInHeader(Mail mail){
			Collection<String> mailCC = mail.getRecipients().getRecipients(RecipientType.cc);
			Collection<String> mailTO = mail.getRecipients().getRecipients(RecipientType.to);
			Collection<String> removed = new ArrayList<String>();
			for(User user : mail.getRecipientsInHeaders()){
				log.debug("checking user:"+user.getMail());
				if(accounts!=null){
					log.debug("checking accounts:"+Arrays.deepToString(accounts.toArray()));
					if(user.hasAlias(accounts.toArray(new String[accounts.size()]))){
						if(mailCC!=null){
							log.debug("cheking mailCC:"+Arrays.deepToString(mailCC.toArray()));
							if(user.hasAlias(mailCC.toArray(new String[mailCC.size()]))){
								log.debug("user has alias in mailCC");
								for(String alias : user.getAliases()){
									log.debug("trying to remove alias "+alias);
									if(mail.getRecipients().removeRecipient(RecipientType.cc, alias)){
										log.debug("alias removed from CC "+alias);
										removed.add(alias);
									}
								}
							}
						}
						if(CCTO_VALUE.equals(protectedSelection) && mailTO!=null){
							log.debug("cheking mailTO:"+Arrays.deepToString(mailTO.toArray()));
							if(user.hasAlias(mailTO.toArray(new String[mailTO.size()]))){
								log.debug("user has alias in TO");
								for(String alias : user.getAliases()){
									log.debug("trying to remove alias "+alias);
									if(mail.getRecipients().removeRecipient(RecipientType.to, alias)){
										log.debug("alias removed from TO "+alias);
										removed.add(alias);
									}
								}
							}
						}
					}
				}
			}
			return removed;
		}
	}
	
}
