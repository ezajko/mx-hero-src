package org.mxhero.feature.addressprotection.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatchParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeatureWithFixed{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	public static final String EMAIL_LIST = "email.list";
	public static final String PROTECTED_SELECTION = "protected.selection";
	public static final String CC_VALUE = "cc";
	public static final String CCTO_VALUE = "ccto";
	public static final String FOLLOWER_ID = "org.mxhero.feature.addressprotection";
	
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
		coreRule.addAction(new APAction(rule.getId(),protectedSelection,accounts,coreRule.getGroup()));
		
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
			if(log.isDebugEnabled()){
				log.debug("protectedSelection:"+protectedSelection
						+" accounts:"+Arrays.deepToString(accounts.toArray())
						+" hasEmailInHeader:"+hasEmailInHeader(mail,accounts)
						+" don't have property:"+!mail.getProperties().containsKey(FOLLOWER_ID));
			}
			return mail.getStatus().equals(Mail.Status.deliver) 
					&& !mail.getFromSender().hasAlias(accounts.toArray(new String[accounts.size()]))
					&& hasEmailInHeader(mail,accounts)
					&& !mail.getProperties().containsKey(FOLLOWER_ID);
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
		private String group;

		public APAction(Integer ruleId, String protectedSelection, List<String> accounts,String group) {
			this.protectedSelection = protectedSelection;
			this.accounts = accounts;
			this.ruleId = ruleId;
			this.group = group;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-AddressProtection", "rule="+ruleId);
			mail.getProperties().put(FOLLOWER_ID, protectedSelection);
			Map<RecipientType, Collection<String>> removed = removeEmailInHeader(mail);
			String removedCcStr = Arrays.deepToString(removed.get(RecipientType.cc).toArray()).replace("[","").replace("]","");
			String removedToStr = Arrays.deepToString(removed.get(RecipientType.to).toArray()).replace("[","").replace("]","");
			String removedAllStr = Arrays.deepToString(removed.get(RecipientType.all).toArray()).replace("[","").replace("]","");
			if(mail.getRecipients().getRecipients(RecipientType.to)==null || mail.getRecipients().getRecipients(RecipientType.to).size()<1){
				mail.getHeaders().removeHeader("To");
				mail.getHeaders().addHeader("To", "undisclosed-recipients:;");
			}
			mail.cmd(AddThreadWatch.class.getName(), new AddThreadWatchParameters(FOLLOWER_ID,removedToStr+";"+removedCcStr+";"+group));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.addressprotection", removedAllStr));
		}
		
		private Map<RecipientType, Collection<String>> removeEmailInHeader(Mail mail){
			Collection<String> mailCC = mail.getRecipients().getRecipients(RecipientType.cc);
			Collection<String> mailTO = mail.getRecipients().getRecipients(RecipientType.to);
			Collection<String> removedTo = new ArrayList<String>();
			Collection<String> removedCc = new ArrayList<String>();
			Collection<String> removedAll = new ArrayList<String>();
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
										removedCc.add(alias);
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
										removedTo.add(alias);
									}
								}
							}
						}
					}
				}
			}
			removedAll.addAll(removedTo);
			removedAll.addAll(removedCc);
			Map<RecipientType, Collection<String>> response = new HashMap<RecipientType, Collection<String>>();
			response.put(RecipientType.to, removedTo);
			response.put(RecipientType.cc, removedCc);
			response.put(RecipientType.all, removedAll);
			return response;
		}
	}
	
}
