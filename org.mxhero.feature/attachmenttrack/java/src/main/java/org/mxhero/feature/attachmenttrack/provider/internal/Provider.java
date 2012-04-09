package org.mxhero.feature.attachmenttrack.provider.internal;

import java.util.Collection;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;
import org.mxhero.engine.plugin.attachmentlink.alcommand.ALCommandParameters;
import org.mxhero.engine.plugin.attachmentlink.alcommand.AlCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";	
	private static final String LOCALE = "locale";
	private static final String DEFAULT_LOCALE = "en_US";
	private static final String HEADER = "X-mxHero-Actions";
	private static final String ACTION_VALUE = "attachmentTrack";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		boolean notify = false;
		String message = "";
		String locale = DEFAULT_LOCALE;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equalsIgnoreCase(ACTION_SELECTION)){
				if(property.getValue().equalsIgnoreCase(ACTION_RETURN)){
					notify=true;
				}
			} else if(property.getKey().equalsIgnoreCase(RETURN_MESSAGE)){
				message = property.getValue();
			} else if(property.getKey().equalsIgnoreCase(LOCALE)){
				locale = property.getValue();
			} 
		}
		
		coreRule.addEvaluation(new ATEvaluation());
		coreRule.addAction(new ATAction(rule.getId(), notify, message, locale));
		
		return coreRule;
	}

	public class ATEvaluation implements Evaluable{
		
		@Override
		public boolean eval(Mail mail) {
			Collection<String> values = mail.getHeaders().getHeaderValues(HEADER);
			boolean result =  mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& mail.getAttachments()!=null
			&& mail.getAttachments().isAttached()
			&& (mail.getSubject().matches("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*.*") ||
					(values!=null && values.size()>0 && HeaderUtils.getParametersList(values.toArray(new String[0]), ACTION_VALUE)!=null));
			if(log.isDebugEnabled()){
				log.debug("eval="+result);
				log.debug("has header="+mail.getHeaders().hasHeader(HEADER));
				log.debug("is attached="+mail.getAttachments().isAttached());
				log.debug("match subject="+mail.getSubject().matches("(?i).*\\[\\s*mxatt\\s*\\]\\s*.*") );
				log.debug("header value="+HeaderUtils.getParametersList(mail.getHeaders().getHeaderValues(HEADER).toArray(new String[0]), ACTION_VALUE));
			}
			return result;
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
			if(!mail.getProperties().containsKey("org.mxhero.feature.attachmentlink")
				&& !mail.getProperties().containsKey("org.mxhero.feature.attachmenttrack")){
				Result result = mail.cmd(AlCommand.class.getName(),new ALCommandParameters(locale, notify, message));
				if(!mail.getStatus().equals(Mail.Status.requeue)){
					mail.setSubject(mail.getSubject().replaceFirst("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*", ""));
					mail.getHeaders().addHeader("X-mxHero-AttachmentTrack","rule="+ruleId+";result="+result.isConditionTrue());
					mail.getProperties().put("org.mxhero.feature.attachmentlink", ruleId.toString());
					mail.getProperties().put("org.mxhero.feature.attachmenttrack", ruleId.toString());
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmentlink",Boolean.toString(result.isConditionTrue())));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmenttrack",Boolean.toString(result.isConditionTrue())));
					log.debug("exec, attached");
				}
			}else{
				mail.setSubject(mail.getSubject().replaceFirst("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*", ""));
			}

		}
	}
		
}
