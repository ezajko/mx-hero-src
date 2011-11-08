package org.mxhero.feature.clamav.provider.internal;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = null;
		String message = "";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ACTION_SELECTION)){
				action=property.getPropertyValue();
			} else if(property.getPropertyKey().equals(RETURN_MESSAGE)){
				message = property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new AVEvaluation());
		coreRule.addAction(new AVAction(rule.getId(), message, getNoReplyEmail(rule.getDomain()), action));
		
		return coreRule;
	}

	private class AVEvaluation implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& !mail.getProperties().containsKey("org.mxhero.feature.clamav")
			&& mail.getHeaders()!=null;
		}
		
	}
	
	private class AVAction implements Actionable{

		private Integer ruleId;
		private String returnText;
		private String replyMail;
		private String action;
		
		public AVAction(Integer ruleId, String returnText, String replyMail,
				String action) {
			super();
			this.ruleId = ruleId;
			this.returnText = returnText;
			this.replyMail = replyMail;
			this.action = action;
		}

		@Override
		public void exec(Mail mail) {
			Result clamavResult = mail.cmd("org.mxhero.engine.plugin.clamd.command.ClamavScan","false","true");
			mail.getProperties().put("org.mxhero.feature.clamav",ruleId.toString());
			mail.getHeaders().addHeader("X-mxHero-ClamAV","rule="+ruleId.toString()+";result="+clamavResult.getText());
			if(action.equalsIgnoreCase(ACTION_RETURN) && clamavResult.isTrue()){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",replyMail,returnText,RulePhase.SEND,mail.getInitialData().getSender().getMail() );
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.clamav",Boolean.toString(clamavResult.isTrue()) );
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","virus.detected",Boolean.toString(clamavResult.isTrue()) );
		}
		
	}
}
