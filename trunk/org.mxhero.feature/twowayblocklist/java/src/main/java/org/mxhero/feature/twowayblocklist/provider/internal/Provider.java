package org.mxhero.feature.twowayblocklist.provider.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
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

	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String RETURN_TEXT = "return.text";
	private static final String BLOCK_GROUP = "block.group";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = "";
		String returnText = "";
		String blockgroup = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ACTION_SELECTION)){
				action=property.getPropertyValue();
			} else if (property.getPropertyKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", ""));
				}else{
					accounts.add(value);
				}
			} else if (property.getPropertyKey().equals(RETURN_TEXT)){
				returnText = property.getPropertyValue();
			} else if(property.getPropertyKey().equals(BLOCK_GROUP)){
				blockgroup=action=property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new TWBLEval(coreRule.getGroup(),blockgroup,accounts,domains));
		coreRule.addAction(new TWBLAction(rule.getId(), coreRule.getGroup(), returnText, getNoReplyEmail(rule.getDomain()), action));
		
		return coreRule;
	}

	private class TWBLEval implements Evaluable{

		private String group;
		Collection<String> accounts;
		Collection<String> domains;
		String blockgroup = "";

		public TWBLEval(String group, String blockgroup, Collection<String> accounts,
				Collection<String> domains) {
			this.group = group;
			this.accounts = accounts;
			this.domains = domains;
			this.blockgroup = blockgroup;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.twowayblocklist:"+group)
			&& ((blockgroup.equals("senders") && checkSender(mail,accounts,domains))||
				(blockgroup.equals("recipients") && checkRecipient(mail,accounts,domains))||
				(blockgroup.equals("both") && (checkRecipient(mail,accounts,domains)||checkSender(mail,accounts,domains)))
			);
		}
		
		private boolean checkSender(Mail mail, Collection<String> accounts,
				Collection<String> domains){
			boolean result = (mail.getInitialData().getFromSender().getDomain().hasAlias(domains)
			|| mail.getInitialData().getSender().getDomain().hasAlias(domains)
			|| mail.getInitialData().getFromSender().hasAlias(accounts)
			|| mail.getInitialData().getSender().hasAlias(accounts));
			if(result){
				mail.getProperties().put("org.mxhero.feature.twowayblocklist.match", "sender");
			}
			return result;
		}
		
		private boolean checkRecipient(Mail mail, Collection<String> accounts,
				Collection<String> domains){
			boolean result = (mail.getInitialData().getRecipient().getDomain().hasAlias(domains)
			|| mail.getInitialData().getRecipient().hasAlias(accounts));
			if(result){
				mail.getProperties().put("org.mxhero.feature.twowayblocklist.match", "recipient");
			}
			return result;
		}
		
	}
	
	private class TWBLAction implements Actionable{

		private static final String ACTION_RETURN = "return";
		
		private Integer ruleId;
		private String group;
		private String returnText;
		private String replyMail;
		private String action;
		
		public TWBLAction(Integer ruleId, String group, String returnText,
				String replyMail, String action) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnText = returnText;
			this.replyMail = replyMail;
			this.action = action;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.twowayblocklist:"+group,ruleId.toString());
			mail.getHeaders().addHeader("X-mxHero-TwoWayBlockList", "rule="+ruleId.toString());
			mail.drop("org.mxhero.feature.twowayblocklist");
			if(action.equalsIgnoreCase(ACTION_RETURN)){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",replyMail,returnText,RulePhase.SEND,mail.getInitialData().getSender().getMail() );
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.twowayblocklist",mail.getProperties().get("org.mxhero.feature.twowayblocklist.match"));
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.blocklist");			
		}
		
	}
	
}
