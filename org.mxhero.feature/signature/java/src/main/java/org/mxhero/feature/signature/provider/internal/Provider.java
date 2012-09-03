package org.mxhero.feature.signature.provider.internal;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.body.command.AppendCurrentCommand;
import org.mxhero.engine.plugin.body.command.AppendCurrentParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Provider extends RulesByFeature{

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
		coreRule.addEvaluation(new SEvaluation(coreRule.getGroup()));
		coreRule.addAction(new SAction(rule.getId(),coreRule.getGroup(),returnMessage,returnMessagePlain));
		return coreRule;
	}

	private class SEvaluation implements Evaluable{

		private String group;

		public SEvaluation(String group) {
			this.group = group;
		}

		public boolean eval(Mail mail) {
			boolean result = mail.getStatus().equals(Mail.Status.deliver) 
					&& !mail.getProperties().containsKey("org.mxhero.feature.signature."+group);
			log.debug("result:"+result);
			return result;
		}
		
	}
	
	private class SAction implements Actionable{
		private Integer ruleId;
		private String group;
		private String returnMessage = "";
		private String returnMessagePlain = "";

		public SAction(Integer ruleId, String group, String returnMessage,
				String returnMessagePlain) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnMessage = returnMessage;
			this.returnMessagePlain = returnMessagePlain;
		}

		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.signature."+group, ruleId.toString());
			String processedReturnMessage = replaceTextVars(mail.getSender().getProperties(),returnMessage);
			String processedReturnMessagePlain = replaceTextVars(mail.getSender().getProperties(),returnMessagePlain);
			mail.cmd(AppendCurrentCommand.class.getName(), new AppendCurrentParameters("\n\n"+processedReturnMessagePlain,"</p></p>"+ processedReturnMessage));
			mail.getHeaders().addHeader("X-mxHero-Signature", "ruleId="+ruleId);
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.signature", Boolean.TRUE.toString()));
		}
		
		private String replaceTextVars(Map<String,String> properties, String content) {
			String tagregex = "\\$\\{[^\\{]*\\}";
			Pattern p2 = Pattern.compile(tagregex);
			StringBuffer sb = new StringBuffer();
			Matcher m2 = p2.matcher(content);
			int lastIndex = 0;

			while (m2.find()) {
				lastIndex = m2.end();
				String key = content.substring(m2.start() + 2, m2.end() - 1);
				log.debug("key:"+key);
				// this should be a header
				String value = properties.get(key);
				if (value != null) {
					log.debug("value:"+value);
					m2.appendReplacement(sb, value);
				}else{
					m2.appendReplacement(sb, "");
				}

			}
			sb.append(content.substring(lastIndex));
			return sb.toString();
		}
	}
	
}
