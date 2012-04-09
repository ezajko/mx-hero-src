package org.mxhero.feature.clamav.provider.internal;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.clamd.command.ClamavScan;
import org.mxhero.engine.plugin.clamd.command.ClamavScanParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";
	private static final String RETURN_MESSAGE_PLAIN = "return.message.plain";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = null;
		String message = "";
		String messagePlain = "";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ACTION_SELECTION)){
				action=property.getValue();
			} else if(property.getKey().equals(RETURN_MESSAGE)){
				message = property.getValue();
			} else if(property.getKey().equals(RETURN_MESSAGE_PLAIN)){
				messagePlain = property.getValue();
			}
		}
		
		coreRule.addEvaluation(new AVEvaluation());
		coreRule.addAction(new AVAction(rule.getId(), messagePlain, message, getNoReplyEmail(rule.getDomain()), action));
		
		return coreRule;
	}

	private class AVEvaluation implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& !mail.getProperties().containsKey("org.mxhero.feature.clamav")
			&& mail.getHeaders()!=null;
		}
		
	}
	
	private class AVAction implements Actionable{

		private Integer ruleId;
		private String returnText;
		private String replyMail;
		private String action;
		private String messagePlain;
		
		public AVAction(Integer ruleId, String messagePlain, String returnText, String replyMail,
				String action) {
			this.ruleId = ruleId;
			this.returnText = returnText;
			this.replyMail = replyMail;
			this.action = action;
			this.messagePlain = messagePlain;
		}

		@Override
		public void exec(Mail mail) {
			ClamavScanParameters cavsParameters = new ClamavScanParameters();
			cavsParameters.setAddHeader(true);
			cavsParameters.setRemoveInfected(false);
			Result clamavResult = mail.cmd(ClamavScan.class.getName(),cavsParameters);
			mail.getProperties().put("org.mxhero.feature.clamav",ruleId.toString());
			mail.getHeaders().addHeader("X-mxHero-ClamAV","rule="+ruleId.toString()+";result="+clamavResult.getMessage());
			if(action.equalsIgnoreCase(ACTION_RETURN) && clamavResult.isConditionTrue()){
				ReplyParameters replyParameter = new ReplyParameters(replyMail, messagePlain, returnText);
				replyParameter.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(), replyParameter);
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.clamav", Boolean.toString(clamavResult.isConditionTrue())));
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("virus.detected", Boolean.toString(clamavResult.isConditionTrue())));
		}
		
	}
}
