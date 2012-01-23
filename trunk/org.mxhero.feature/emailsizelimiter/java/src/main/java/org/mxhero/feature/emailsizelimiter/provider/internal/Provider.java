package org.mxhero.feature.emailsizelimiter.provider.internal;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static DecimalFormat formatter = new DecimalFormat("#0.00#");
	private static final String MAX_SIZE_PROPERTY = "max.size";
	private static final String RETURN_MESSAGE_PROPERTY = "return.message";
	private static final String ACTION_SELECTION_PROPERTY = "action.selection";
	private static final Double CODING_FACTOR = 1.3;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		Number maxSizeValue = null;
		Integer effectiveMaxSize = null;
		String returnMessage = null;
		String action = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(MAX_SIZE_PROPERTY)){
				try {
					maxSizeValue=formatter.parse(property.getPropertyValue());
					effectiveMaxSize = (int)(maxSizeValue.doubleValue()*1024*1024*CODING_FACTOR);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			} else if(property.getPropertyKey().equals(RETURN_MESSAGE_PROPERTY)){
				returnMessage = property.getPropertyValue();
			} else if(property.getPropertyKey().equals(ACTION_SELECTION_PROPERTY)){
				action = property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new EsEvaluate(coreRule.getGroup()));
		coreRule.addAction(new ESAction(rule.getId(), coreRule.getGroup(), effectiveMaxSize, returnMessage, action, getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}
	
	private class EsEvaluate implements Evaluable{

		private String group;

		public EsEvaluate(String group) {
			super();
			this.group = group;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.initialsizelimiter:"+group);
		}
		
	}
	
	private class ESAction implements Actionable{

		private static final String ACTION_RETURN = "return";
		
		private Integer ruleId;
		private String group;
		private Integer effectiveMaxSize;
		private String returnMessage;
		private String action;
		private String replyMail;

		public ESAction(Integer ruleId, String group, Integer effectiveMaxSize,
				String returnMessage, String action, String replyMail) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.effectiveMaxSize = effectiveMaxSize;
			this.returnMessage = returnMessage;
			this.action = action;
			this.replyMail = replyMail;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-InitialSizeLimiter","rule="+ruleId);
			mail.getProperties().put("org.mxhero.feature.initialsizelimiter:"+group,ruleId.toString());
			boolean isDropped = false;
			if(mail.getInitialData().getInitialSize()>effectiveMaxSize){
				mail.drop("org.mxhero.feature.initialsizelimiter");
				isDropped=true;
				mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.initialsizelimiter");
			}
			if(action.equalsIgnoreCase(ACTION_RETURN) && isDropped){
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",replyMail,mail.getInitialData().getSender().getMail(),returnMessage,returnMessage );
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.initialsizelimiter",Boolean.toString(isDropped) );
		}
		
	}

}
