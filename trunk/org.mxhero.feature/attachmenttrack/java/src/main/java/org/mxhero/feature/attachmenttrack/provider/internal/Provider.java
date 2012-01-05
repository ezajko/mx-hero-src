package org.mxhero.feature.attachmenttrack.provider.internal;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;

public class Provider extends RulesByFeature{

	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";	
	private static final String LOCALE = "locale";
	private static final String DEFAULT_LOCALE = "en_US";
	private static final String HEADER = "X-mxHero-Actions";
	private static final String HEADER_VALUE = "attachmmentTrack";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		boolean notify = false;
		String message = "";
		String locale = DEFAULT_LOCALE;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equalsIgnoreCase(ACTION_SELECTION)){
				if(property.getPropertyValue().equalsIgnoreCase(ACTION_RETURN)){
					notify=true;
				}
			} else if(property.getPropertyKey().equalsIgnoreCase(RETURN_MESSAGE)){
				message = property.getPropertyValue();
			} else if(property.getPropertyKey().equalsIgnoreCase(LOCALE)){
				locale = property.getPropertyValue();
			} 
		}
		
		coreRule.addEvaluation(new ATEvaluation());
		coreRule.addAction(new ATAction(rule.getId(), notify, message, locale));
		
		return coreRule;
	}

	public class ATEvaluation implements Evaluable{
		
		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& !mail.getProperties().containsKey("org.mxhero.feature.attachmentlink")
			&& !mail.getProperties().containsKey("org.mxhero.feature.attachmenttrack")
			&& mail.getHeaders()!=null
			&& mail.getAttachments()!=null
			&& mail.getAttachments().isAttached()
			&& (mail.getSubject().getSubject().matches("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*.*") ||
				HeaderUtils.parseParameters(HEADER, HEADER_VALUE)!=null);
		}
	}
	
	public class ATAction implements Actionable{
		
		private Integer ruleId;
		private boolean notify = false;
		private String  message = null;
		private String locale = null;
		
		
		public ATAction(Integer ruleId, boolean notify, String message, String locale) {
			this.notify = notify;
			this.message = message;
			this.locale = locale;
			this.ruleId = ruleId;
		}

		@Override
		public void exec(Mail mail) {
			Result result = mail.cmd("org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand",locale,Boolean.toString(notify),message);
			if(!mail.getState().equalsIgnoreCase(MailState.REQUEUE)){
				mail.getHeaders().addHeader("X-mxHero-AttachmentTrack","rule="+ruleId+";result="+result.isTrue());
				mail.getProperties().put("org.mxhero.feature.attachmentlink", ruleId.toString());
				mail.getProperties().put("org.mxhero.feature.attachmenttrack", ruleId.toString());
				mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.attachmentlink",Boolean.toString(result.isResult()) );
				mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.attachmenttrack",Boolean.toString(result.isResult()) );
			}
		}
	}
		
}
