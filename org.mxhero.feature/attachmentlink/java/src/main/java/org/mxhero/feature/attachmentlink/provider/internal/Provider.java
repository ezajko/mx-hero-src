package org.mxhero.feature.attachmentlink.provider.internal;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static DecimalFormat formatter = new DecimalFormat("#0.00#");
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";	
	private static final String MAX_SIZE_PROPERTY = "max.size";
	private static final String LOCALE = "locale";
	private static final String DEFAULT_LOCALE = "en_US";
	private static final Double CODING_FACTOR = 1.3;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		Number maxSizeValue = null;
		Integer effectiveMaxSize = null;
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
			} else if(property.getPropertyKey().equals(MAX_SIZE_PROPERTY)){
				try {
					maxSizeValue=formatter.parse(property.getPropertyValue());
					effectiveMaxSize = (int)(maxSizeValue.doubleValue()*1024*1024*CODING_FACTOR);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
		coreRule.addEvaluation(new ALEvaluation(effectiveMaxSize));
		coreRule.addAction(new ALAction(rule.getId(), notify, message, locale));
		
		return coreRule;
	}

	public class ALEvaluation implements Evaluable{

		private Integer effectiveMaxSize = null;
	
		public ALEvaluation(Integer effectiveMaxSize) {
			this.effectiveMaxSize = effectiveMaxSize;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getInitialData().getInitialSize()> effectiveMaxSize
			&& !mail.getProperties().containsKey("org.mxhero.feature.attachmentlink")
			&& mail.getHeaders()!=null;
		}
		
	}
	
	public class ALAction implements Actionable{
		
		private Integer ruleId;
		private boolean notify = false;
		private String  message = null;
		private String locale = null;
		
		
		public ALAction(Integer ruleId, boolean notify, String message, String locale) {
			super();
			this.notify = notify;
			this.message = message;
			this.locale = locale;
			this.ruleId = ruleId;
		}

		@Override
		public void exec(Mail mail) {
			Result result = mail.cmd("org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand",locale,Boolean.toString(notify),message);
			if(!mail.getState().equalsIgnoreCase(MailState.REQUEUE)){
				mail.getHeaders().addHeader("X-mxHero-Attachmentlink","rule="+ruleId+";result="+result.isTrue());
				mail.getProperties().put("org.mxhero.feature.attachmentlink", ruleId.toString());
				mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.attachmentlink",Boolean.toString(result.isResult()) );
			}
		}
		
	}
}
