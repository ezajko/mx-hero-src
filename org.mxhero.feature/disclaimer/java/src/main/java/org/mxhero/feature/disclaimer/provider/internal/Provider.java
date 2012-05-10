package org.mxhero.feature.disclaimer.provider.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Provider extends RulesByFeature{

	private final static String SENDER_KEY = "mxsender";
	private final static String RECIPIENT_KEY = "mxrecipient";
	private static Logger log = LoggerFactory.getLogger(Provider.class);
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
				returnMessagePlain=property.getValue();
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
			boolean result = mail.getStatus().equals(Mail.Status.deliver) 
					&& !mail.getProperties().containsKey("org.mxhero.feature.disclaimer."+group);
			log.debug("result:"+result);
			return result;
		}
		
	}
	
	private class DAction implements Actionable{
		private Integer ruleId;
		private String group;
		private String returnMessage = "";
		private String returnMessagePlain = "";

		public DAction(Integer ruleId, String group, String returnMessage,
				String returnMessagePlain) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnMessage = returnMessage;
			this.returnMessagePlain = returnMessagePlain;
		}

		public void exec(Mail mail) {
			log.debug("adding text:"+returnMessagePlain);
			mail.getProperties().put("org.mxhero.feature.disclaimer."+group, ruleId.toString());
			mail.getBody().addText("</p></p>"+replaceTextVars(mail,returnMessage), Body.AddTextPosition.botton, Body.AddTextPartType.html);
			mail.getBody().addText("\n\n"+replaceTextVars(mail,returnMessagePlain), Body.AddTextPosition.botton, Body.AddTextPartType.plain);
			mail.getHeaders().addHeader("X-mxHero-Disclaimer", "ruleId="+ruleId);
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.disclaimer", Boolean.TRUE.toString()));
		}
		
		private String replaceTextVars(Mail mail, String content) {
			String tagregex = "\\$\\{[^\\{]*\\}";
			Pattern p2 = Pattern.compile(tagregex);
			StringBuffer sb = new StringBuffer();
			Matcher m2 = p2.matcher(content);
			int lastIndex = 0;

			while (m2.find()) {
				lastIndex = m2.end();
				String key = content.substring(m2.start() + 2, m2.end() - 1);

				if (key.equalsIgnoreCase(SENDER_KEY)) {
					m2.appendReplacement(sb, mail.getSender().getMail());
				} else if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
					m2.appendReplacement(sb, mail.getRecipient().getMail());
				} else {
					// this should be a header
					String value = mail.getHeaders().getHeaderValue(key);
					if (value != null) {
						m2.appendReplacement(sb, value);
					}
				}

			}
			sb.append(content.substring(lastIndex));
			return sb.toString();
		}
	}
	
}
