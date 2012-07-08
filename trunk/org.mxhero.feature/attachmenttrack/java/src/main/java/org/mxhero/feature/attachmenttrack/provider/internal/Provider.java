package org.mxhero.feature.attachmenttrack.provider.internal;

import java.util.Arrays;
import java.util.Collection;

import org.jsoup.Jsoup;
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
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_MESSAGE = "return.message";	
	private static final String RETURN_MESSAGE_PLAIN = "return.message.plain";	
	private static final String LOCALE = "locale";
	private static final String DEFAULT_LOCALE = "en_US";
	private static final String HEADER = "X-mxHero-Actions";
	private static final String ACTION_VALUE = "attachmentTrack";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		boolean notify = false;
		String message = "";
		String messagePlain = "";
		String locale = DEFAULT_LOCALE;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equalsIgnoreCase(ACTION_SELECTION)){
				if(property.getValue().equalsIgnoreCase(ACTION_RETURN)){
					notify=true;
				}
			} else if(property.getKey().equalsIgnoreCase(RETURN_MESSAGE)){
				message = property.getValue();
			} else if(property.getKey().equalsIgnoreCase(RETURN_MESSAGE_PLAIN)){
				messagePlain = property.getValue();
			} else if(property.getKey().equalsIgnoreCase(LOCALE)){
				locale = property.getValue();
			} 
		}
		
		coreRule.addEvaluation(new ATEvaluation(this.getNoReplyEmail(rule.getDomain())));
		coreRule.addAction(new ATAction(rule.getId(), notify, message, messagePlain, locale));
		
		return coreRule;
	}

	public class ATEvaluation implements Evaluable{
		
		private String noreplyEmail;

		public ATEvaluation(String noreplyEmail) {
			this.noreplyEmail = noreplyEmail;
		}

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
				if(mail.getHeaders().hasHeader(HEADER)){
					log.debug("header raw value="+Arrays.deepToString(mail.getHeaders().getHeaderValues(HEADER).toArray(new String[0])));
					log.debug("header value="+HeaderUtils.getParametersList(mail.getHeaders().getHeaderValues(HEADER).toArray(new String[0]), ACTION_VALUE));
				}
			}
			
			if(mail.getHeaders().hasHeader(HEADER) 
					&& !mail.getProperties().containsKey("org.mxhero.feature.attachmenttrack.erromail")
					&& HeaderUtils.getParametersList(values.toArray(new String[0]), ACTION_VALUE)==null){
				for(String value : values){
					if(value.contains(ACTION_VALUE)){
						String text = "<p>The mxHero Zimlet installed in your Zimbra installation is no longer supported by your mxHero installation.</p><p>Please notify your email administrator.</p><p>Thank you, mxHero</p>";
						log.debug("sending cliente error message");
						ReplyParameters replyParameters = new ReplyParameters(noreplyEmail, Jsoup.parse(text).text(), text);
						replyParameters.setSubject("mxHero client error");
						replyParameters.setRecipient(mail.getSender().getMail());
						mail.cmd(Reply.class.getName(), replyParameters);
						mail.getProperties().put("org.mxhero.feature.attachmenttrack.erromail", "error");
						break;
					}
				}
			}
			
			return result;
		}
	}
	
	public class ATAction implements Actionable{
		
		private Integer ruleId;
		private boolean notify = false;
		private String  message = null;
		private String messagePlain = null;
		private String locale = null;
		
		
		public ATAction(Integer ruleId, boolean notify, String message, String messagePlain, String locale) {
			this.notify = notify;
			this.message = message;
			this.locale = locale;
			this.ruleId = ruleId;
			this.messagePlain = messagePlain;
		}

		@Override
		public void exec(Mail mail) {
			String files = Arrays.deepToString(mail.getAttachments().getFileNames().toArray());
			if(!mail.getProperties().containsKey("org.mxhero.feature.attachmentlink")
				&& !mail.getProperties().containsKey("org.mxhero.feature.attachmenttrack")){
				Result result = mail.cmd(AlCommand.class.getName(),new ALCommandParameters(locale, notify, messagePlain, message));
				if(!mail.getStatus().equals(Mail.Status.requeue)){
					mail.setSubject(mail.getSubject().replaceFirst("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*", ""));
					mail.getHeaders().addHeader("X-mxHero-AttachmentTrack","rule="+ruleId+";result="+result.isConditionTrue());
					mail.getProperties().put("org.mxhero.feature.attachmentlink", ruleId.toString());
					mail.getProperties().put("org.mxhero.feature.attachmenttrack", ruleId.toString());
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmentlink",Boolean.toString(result.isConditionTrue())));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmenttrack",Boolean.toString(result.isConditionTrue())));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachmenttrack.files",files));
					log.debug("exec, attached");
				}
			}else{
				mail.setSubject(mail.getSubject().replaceFirst("(?i)\\s*\\[\\s*mxatt\\s*\\]\\s*", ""));
			}

		}
	}
		
}
