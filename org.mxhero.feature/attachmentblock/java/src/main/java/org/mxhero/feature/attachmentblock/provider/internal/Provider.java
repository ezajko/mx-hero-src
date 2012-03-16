package org.mxhero.feature.attachmentblock.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String DISCARD_ACTION = "discard.action";
	private static final String RETURN_ACTION = "return.action";
	private static final String FILE_EXTENSION = "file.extension";
	private static final String FILE_TYPE = "file.type";
	private static final String FILE_NAME = "file.name";
	private static final String RETURN_ACTION_TEXT = "return.action.text";
	private static final String RETURN_ACTION_TEXT_PLAIN = "return.action.text.plain";

	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String action = null;
		String returnMessage = "";
		String returnMessagePlain = "";
		List<String> extensions = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(DISCARD_ACTION)){
				action=property.getPropertyKey();
			}else if (property.getPropertyKey().equals(RETURN_ACTION)){
				action=property.getPropertyKey();
			}else if (property.getPropertyKey().equals(RETURN_ACTION_TEXT)){
				returnMessage = property.getPropertyValue();
			}else if (property.getPropertyKey().equals(FILE_EXTENSION)){
				extensions.add(StringEscapeUtils.escapeJava(property.getPropertyValue()));
			}else if (property.getPropertyKey().equals(FILE_TYPE)){
				types.add(StringEscapeUtils.escapeJava(property.getPropertyValue()));
			}else if (property.getPropertyKey().equals(FILE_NAME)){
				names.add(StringEscapeUtils.escapeJava(property.getPropertyValue()));
			}else if (property.getPropertyKey().equals(RETURN_ACTION_TEXT_PLAIN)){
				returnMessagePlain=StringEscapeUtils.escapeJava(property.getPropertyValue());
			}
		}
		
		coreRule.addEvaluation(new ABEvaluation(coreRule.getGroup()));
		
		coreRule.addAction(new ABAction(coreRule.getGroup()
										,coreRule.getId()
										,this.getNoReplyEmail(rule.getDomain())
										,action
										,returnMessage
										,returnMessagePlain
										,extensions
										,names
										,types));
		
		return coreRule;
	}

	private class ABEvaluation implements  Evaluable{

		private String group;
		
		public ABEvaluation(String group){
			this.group=group;
		}
		
		@Override
		public boolean eval(Mail mail) {
			boolean result = mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& mail.getAttachments()!=null
			&& !mail.getProperties().containsKey("org.mxhero.feature.attachementblock:"+group);
			log.debug("result="+result);
			if(log.isDebugEnabled()){
				log.debug(mail.getAttachments().toString());
			}
			return result;
		}
		
	}
	
	private class ABAction implements Actionable{

		private String group;
		private Integer ruleId;
		private String noreplyMail;
		private String action = null;
		private String returnMessage = "";
		private String returnMessagePlain = "";
		private Collection<String> extensions = new ArrayList<String>();
		private Collection<String> names = new ArrayList<String>();
		private Collection<String> types = new ArrayList<String>();

		public ABAction(String group, Integer ruleId, String noreplyMail, String action,
				String returnMessage,String returnMessagePlain, List<String> extensions,
				List<String> names, List<String> types) {
			this.group = group;
			this.ruleId = ruleId;
			this.noreplyMail = noreplyMail;
			this.action = action;
			this.returnMessage = returnMessage;
			this.extensions = extensions;
			this.names = names;
			this.types = types;
		}



		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.attachementblock:"+group, ruleId.toString());
			if(extensions.size()>0){
				log.debug("cheking extensions="+Arrays.deepToString(extensions.toArray()));
				if(mail.getState().equals(MailState.DELIVER) && mail.getAttachments().hasMatchingExtension(extensions)){
					mail.drop("org.mxhero.feature.attachmentblock");
					log.debug("droped by extension");
				}
			}
			if(names.size()>0){
				log.debug("cheking names="+Arrays.deepToString(names.toArray()));
				if(mail.getState().equals(MailState.DELIVER) && mail.getAttachments().hasMatchingName(names)){
					mail.drop("org.mxhero.feature.attachmentblock");
					log.debug("droped by names");
				}
			}
			if(types.size()>0){
				log.debug("cheking types="+Arrays.deepToString(types.toArray()));
				if(mail.getState().equals(MailState.DELIVER) && mail.getAttachments().hasMatchingType(types)){
					mail.drop("org.mxhero.feature.attachmentblock");
					log.debug("droped by types");
				}
			}

			boolean droppedByAttachments=mail.getState().equals(MailState.DROP);
			if(action!=null && action.equals(RETURN_ACTION) && droppedByAttachments){
				log.debug("return message");
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",new String[]{noreplyMail,mail.getInitialData().getSender().getMail(),returnMessagePlain,returnMessage} );
			}
			mail.getHeaders().addHeader("X-mxHero-AttachmentBlock", "rule="+ruleId+";blocked="+droppedByAttachments);
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.attachementblock",Boolean.toString(droppedByAttachments) );
			if(droppedByAttachments){
				log.debug("bloqued stat");
				mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","email.blocked","org.mxhero.feature.attachementblock");
			}
		}
		
	}
	
}
