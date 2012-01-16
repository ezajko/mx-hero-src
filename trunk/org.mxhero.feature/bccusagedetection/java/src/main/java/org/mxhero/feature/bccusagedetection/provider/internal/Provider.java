package org.mxhero.feature.bccusagedetection.provider.internal;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	private static String EMAIL_VALUE = "email.value";
	private static String LIST_IGNORE = "lists.ignore";
	private static String BCC_HEADER = "bcc.header";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = new CoreRule(rule.getId(), this.getFeature().getBasePriority()+this.getPriority(rule.getFromDirection())+this.getPriority(rule.getToDirection()), (rule.getDomain()!=null)?rule.getDomain():rule.getAdminOrder());
		Boolean ignoreList = false;
		String email = null;
		String bccHeader = "BCC:";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(LIST_IGNORE)){
				ignoreList=Boolean.parseBoolean(property.getPropertyValue());
			}else if (property.getPropertyKey().equals(EMAIL_VALUE)){
				email = property.getPropertyValue();
			}else if (property.getPropertyKey().equals(BCC_HEADER)){
				bccHeader = property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new FromInHeaders(rule.getFromDirection(), rule.getToDirection(), rule.getTwoWays()));
		coreRule.addEvaluation(new BCCUDEval(ignoreList));
		coreRule.addAction(new BCCUDAction(email, rule.getId(), bccHeader));
		
		return coreRule;
	}

	private class BCCUDEval implements Evaluable{
		Boolean ignoreList = false;

		public BCCUDEval(Boolean ignoreList) {
			this.ignoreList = ignoreList;
		}

		@Override
		public boolean eval(Mail mail) {
			boolean result = mail.getState().equalsIgnoreCase(MailState.DELIVER)
					&& mail.getHeaders()!=null
					&& mail.getRecipients()!=null
					&& !mail.getProperties().containsKey("org.mxhero.feature.bccusagedetection")
					&& !mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply")
					&& ((ignoreList && !ignoreListCheck(mail) && !mail.getInitialData().getRecipient().hasAlias(mail.getRecipients().getAllRecipients()))
					|| (!ignoreList && !mail.getInitialData().getRecipient().hasAlias(mail.getRecipients().getAllRecipients())));
			log.debug("eva result:"+result);
			return result;
		}
		
		private boolean ignoreListCheck(Mail mail){
			String undisclosedPattern = "(?i).*undisclosed.*";
			log.debug("TO header:"+mail.getHeaders().getHeaderValue(Message.RecipientType.TO.toString()));
			if(mail.getHeaders().hasHeader(Message.RecipientType.TO.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.TO.toString()).matches(undisclosedPattern)){
				log.debug("TO undisclosed");
				return true;
			}
			log.debug("CC header:"+mail.getHeaders().getHeaderValue(Message.RecipientType.CC.toString()));
			if(mail.getHeaders().hasHeader(Message.RecipientType.CC.toString())
					&& mail.getHeaders().getHeaderValue(Message.RecipientType.CC.toString()).matches(undisclosedPattern)){
				log.debug("CC undisclosed");
				return true;
			}
			if(mail.getHeaders().hasHeader("List-Id")||
					mail.getHeaders().hasHeader("List-Help")||
					mail.getHeaders().hasHeader("List-Subscribe")||
					mail.getHeaders().hasHeader("List-Unsubscribe")||
					mail.getHeaders().hasHeader("List-Post")||
					mail.getHeaders().hasHeader("List-Owner")||
					mail.getHeaders().hasHeader("List-Archive")){
				log.debug("List");
				return true;
			}
			return false;
		}
	}
	
	
	private class BCCUDAction implements Actionable{
		String email = null;
		Integer ruleId = null;
		String bccHeader = null;

		public BCCUDAction(String email, Integer ruleId, String bccHeader) {
			this.email = email;
			this.ruleId = ruleId;
			this.bccHeader = bccHeader;
		}

		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.bccusagedetection", "true");
			mail.getHeaders().addHeader("X-mxHero-BCCUsageDetection", "rule="+ruleId+";blocked=true");
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.OUT,mail.getInitialData().getSender().getMail(),email,null,bccHeader+mail.getInitialData().getRecipient().getMail()+"\n\n","top","both");
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.bccusagedetection","true" );
		}
		
	}
}