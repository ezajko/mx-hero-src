package org.mxhero.feature.disclaimer.provider.internal;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Body;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;

public class Provider extends RulesByFeature{

	private final static String RETURN_MESSAGE = "return.message";
	private final static String RETURN_MESSAGE_PLAIN = "return.message.plain";
	
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String returnMessage = "";
		String returnMessagePlain = "";
		
		for(RuleProperty property : rule.getProperties()){
			if (property.getKey().equals(RETURN_MESSAGE)){
				returnMessage = property.getValue();
			}else if (property.getKey().equals(RETURN_MESSAGE_PLAIN)){
				returnMessagePlain=StringEscapeUtils.escapeJava(property.getValue());
			}
		}
		coreRule.addEvaluation(new DEvaluation(coreRule.getGroup()));
		coreRule.addAction(new DAction(rule.getId(),coreRule.getGroup(),returnMessage,returnMessagePlain));
		return coreRule;
	}

	private class DEvaluation implements Evaluable{

		private String group;

		public DEvaluation(String group) {
			this.group = group;
		}

		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver) 
					&& mail.getProperties().containsKey("org.mxhero.feature.disclaimer."+group);
		}
		
	}
	
	private class DAction implements Actionable{
		private Integer ruleId;
		private String group;
		private String returnMessage = "";
		private String returnMessagePlain = "";

		public DAction(Integer ruleId, String group, String returnMessage,
				String returnMessagePlain) {
			this.group = group;
			this.returnMessage = returnMessage;
			this.returnMessagePlain = returnMessagePlain;
		}

		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.disclaimer."+group, ruleId.toString());
			mail.getBody().addText(returnMessage, Body.AddTextPosition.botton, Body.AddTextPartType.html);
			mail.getBody().addText(returnMessagePlain, Body.AddTextPosition.botton, Body.AddTextPartType.plain);
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.disclaimer", Boolean.TRUE.toString()));
		}
		
	}
	
}
