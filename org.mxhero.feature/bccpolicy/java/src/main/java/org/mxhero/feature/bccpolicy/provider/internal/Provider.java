package org.mxhero.feature.bccpolicy.provider.internal;

import javax.mail.Message;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.FromInHeaders;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{
	
	private static final String ACTION_SELECTION = "action.selection";
	private static final String RETURN_MESSAGE = "return.message";
	private static final String LIST_IGNORE = "lists.ignore";
	private static final String RETURN_ACTION = "return";
			
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(), this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()), (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
	
		String action = null;
		String returnMessage = "";
		Boolean ignoreList = false;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ACTION_SELECTION)){
				action=property.getPropertyValue();
			}else if (property.getPropertyKey().equals(RETURN_MESSAGE)){
				returnMessage = property.getPropertyValue();
			}else if (property.getPropertyKey().equals(LIST_IGNORE)){
				ignoreList = Boolean.parseBoolean(property.getPropertyValue());
			}
		}
		
		coreRule.addEvaluation(new FromInHeaders(rule.getFromDirection(), rule.getToDirection(), rule.getTwoWays()));
		coreRule.addEvaluation(new BCCPEvaluation(ignoreList));
		coreRule.addAction(new BCCPEAction(action,returnMessage,rule.getId(),this.getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}

	private class BCCPEvaluation implements Evaluable{

		Boolean ignoreList = false;
		
		public BCCPEvaluation(Boolean ignoreList) {
			this.ignoreList = ignoreList;
		}

		@Override
		public boolean eval(Mail mail) {
			
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
					&& mail.getHeaders()!=null
					&& mail.getRecipients()!=null
					&& ((ignoreList && !ignoreListCheck(mail) && !mail.getInitialData().getRecipient().hasAlias(mail.getRecipients().getAllRecipients()))
					|| (!ignoreList && !mail.getInitialData().getRecipient().hasAlias(mail.getRecipients().getAllRecipients())));
		}
		
		private boolean ignoreListCheck(Mail mail){
			String undisclosedPattern = ".*undisclosed-recipients.*";
			if(mail.getHeaders().hasHeader(Message.RecipientType.TO.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.TO.toString()).matches(undisclosedPattern)){
				return true;
			}
			if(mail.getHeaders().hasHeader(Message.RecipientType.CC.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.CC.toString()).matches(undisclosedPattern)){
				return true;
			}
			if(mail.getHeaders().hasHeader("List-Id")||
					mail.getHeaders().hasHeader("List-Help")||
					mail.getHeaders().hasHeader("List-Subscribe")||
					mail.getHeaders().hasHeader("List-Unsubscribe")||
					mail.getHeaders().hasHeader("List-Post")||
					mail.getHeaders().hasHeader("List-Owner")||
					mail.getHeaders().hasHeader("List-Archive")){
				return true;
			}
			return false;
		}
		
	}
	
	
	private class BCCPEAction implements Actionable {

		String action = null;
		String returnMessage = "";
		Integer ruleId = null;
		String noreplyMail = null;

		public BCCPEAction(String action, String returnMessage, Integer ruleId, String noreplyMail) {
			this.action = action;
			this.returnMessage = returnMessage;
			this.ruleId = ruleId;
			this.noreplyMail = noreplyMail;
		}

		@Override
		public void exec(Mail mail) {
			mail.drop("org.mxhero.feature.bccpolicy");
			mail.getHeaders().addHeader("X-mxHero-BCCPolicy", "rule="+ruleId+";blocked=true");
			if(action!=null && action.equals(RETURN_ACTION)){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",new String[]{noreplyMail,returnMessage,RulePhase.SEND,mail.getInitialData().getSender().getMail()} );
			}			
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.bccpolicy","true" );
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.bccpolicy");
		}

	}
}
