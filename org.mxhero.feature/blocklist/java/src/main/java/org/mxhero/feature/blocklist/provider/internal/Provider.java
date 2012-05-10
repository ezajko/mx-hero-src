package org.mxhero.feature.blocklist.provider.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.FromToEval;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String RETURN_TEXT = "return.text";
	private static final String RETURN_TEXT_PLAIN = "return.text.plain";
	private static final String TWO_WAY_FLAG = "two.way.flag";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(), this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()), (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
		
		String action = "";
		String returnText = "";
		String returnTextPlain = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		boolean twoWayFlag = false;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ACTION_SELECTION)){
				action=property.getValue();
			} else if (property.getKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", "").toLowerCase());
				}else{
					accounts.add(value.toLowerCase());
				}
			} else if (property.getKey().equals(RETURN_TEXT)){
				returnText = property.getValue();
			} else if (property.getKey().equals(TWO_WAY_FLAG)){
				twoWayFlag = Boolean.parseBoolean(property.getValue());
			} if (property.getKey().equals(RETURN_TEXT_PLAIN)){
				returnTextPlain = property.getValue();
			}
		}
		coreRule.addEvaluation(new FromToEval(rule.getFromDirection(),rule.getToDirection(), twoWayFlag));
		coreRule.addEvaluation(new BLEvaluation(coreRule.getGroup(),accounts,domains,twoWayFlag));
		coreRule.addAction(new BLAction(rule.getId(), coreRule.getGroup(), returnTextPlain, returnText, getNoReplyEmail(rule.getDomain()), action));
		
		return coreRule;
	}
	
	private class BLEvaluation implements Evaluable{

		private String group;
		Collection<String> accounts;
		Collection<String> domains;
		boolean twoWayFlag = false;
		
		public BLEvaluation(String group, Collection<String> accounts,
				Collection<String> domains, boolean twoWayFlag) {
			this.group = group;
			this.accounts = accounts;
			this.domains = domains;
			this.twoWayFlag = twoWayFlag;
		}

		@Override
		public boolean eval(Mail mail) {
			boolean result = mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.blocklist:"+group)
			&& (mail.getFromSender().getDomain().hasAlias(domains)
				|| mail.getSender().getDomain().hasAlias(domains)
				|| mail.getFromSender().hasAlias(accounts.toArray(new String[accounts.size()]))
				|| mail.getSender().hasAlias(accounts.toArray(new String[accounts.size()])));
			
			if(twoWayFlag){
				if(!result){
					result = mail.getRecipient().getDomain().hasAlias(domains) || mail.getRecipient().hasAlias(accounts.toArray(new String[accounts.size()]));
					mail.getProperties().put("org.mxhero.feature.blocklist.recipient", mail.getRecipient().getMail());
				}else{
					mail.getProperties().put("org.mxhero.feature.blocklist.sender", mail.getSender().getMail());
				}
			}
			
			return result;
		}
	}
	
	private class BLAction implements Actionable{

		private static final String ACTION_RETURN = "return";
		
		private Integer ruleId;
		private String group;
		private String returnText;
		private String returnTextPlain;
		private String replyMail;
		private String action;
		
		public BLAction(Integer ruleId, String group, String returnTextPlain, String returnText,
				String replyMail, String action) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnText = returnText;
			this.returnTextPlain = returnTextPlain;
			this.replyMail = replyMail;
			this.action = action;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.blocklist:"+group,ruleId.toString());
			mail.getHeaders().addHeader("X-mxHero-BlockList", "rule="+ruleId.toString());
			mail.drop("org.mxhero.feature.blocklist");
			if(action.equalsIgnoreCase(ACTION_RETURN)){
				ReplyParameters replyParameter = new ReplyParameters(replyMail,returnTextPlain,returnText);
				replyParameter.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(),replyParameter);
			}
			if(mail.getProperties().containsKey("org.mxhero.feature.blocklist.sender")){
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.blocklist.email", mail.getSender().getMail()));
			}else{
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.blocklist.email", mail.getRecipient().getMail()));
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.blocklist"));			
		}
		
	}

}
