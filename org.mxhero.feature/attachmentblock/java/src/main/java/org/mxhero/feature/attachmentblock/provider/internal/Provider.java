package org.mxhero.feature.attachmentblock.provider.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String DISCARD_ACTION = "discard.action";
	private static final String RETURN_ACTION = "return.action";
	private static final String COPY_ACTION = "copy.action";
	private static final String REDIRECT_ACTION = "redirect.email";
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
		String email = "";
		List<String> extensions = new ArrayList<String>();
		List<String> names = new ArrayList<String>();
		List<String> types = new ArrayList<String>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(DISCARD_ACTION)){
				action=property.getKey();
			}else if (property.getKey().equals(RETURN_ACTION)){
				action=property.getKey();
			}else if (property.getKey().equals(RETURN_ACTION_TEXT)){
				returnMessage = property.getValue();
			}else if (property.getKey().equals(FILE_EXTENSION)){
				extensions.add(StringEscapeUtils.escapeJava(property.getValue()));
			}else if (property.getKey().equals(FILE_TYPE)){
				types.add(StringEscapeUtils.escapeJava(property.getValue()));
			}else if (property.getKey().equals(FILE_NAME)){
				names.add(StringEscapeUtils.escapeJava(property.getValue()));
			}else if (property.getKey().equals(RETURN_ACTION_TEXT_PLAIN)){
				returnMessagePlain=StringEscapeUtils.escapeJava(property.getValue());
			}else if (property.getKey().equals(COPY_ACTION)){
				action=property.getKey();
			}else if (property.getKey().equals(REDIRECT_ACTION)){
				action=property.getKey();
			}else if(property.getKey().equals("copy.email") || property.getKey().equals("redirect.email")){
				email=property.getValue();
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
										,types
										,email));
		
		return coreRule;
	}

	private class ABEvaluation implements  Evaluable{

		private String group;
		
		public ABEvaluation(String group){
			this.group=group;
		}
		
		@Override
		public boolean eval(Mail mail) {
			boolean result = mail.getStatus().equals(Mail.Status.deliver)
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
		private String email = "";

		public ABAction(String group, Integer ruleId, String noreplyMail, String action,
				String returnMessage,String returnMessagePlain, List<String> extensions,
				List<String> names, List<String> types, String email) {
			this.group = group;
			this.ruleId = ruleId;
			this.noreplyMail = noreplyMail;
			this.action = action;
			this.returnMessage = returnMessage;
			this.extensions = extensions;
			this.names = names;
			this.types = types;
			this.email = email;
		}



		@Override
		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.attachementblock:"+group, ruleId.toString());
			boolean found = false;
			
			if(extensions.size()>0){
				if(mail.getStatus().equals(Mail.Status.deliver) && mail.getAttachments().hasMatchingExtension(extensions.toArray(new String[extensions.size()]))){
					found=true;
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.reason","extension"));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.extensions",Arrays.deepToString(mail.getAttachments().getExtensions().toArray())));
				}
			}
			if(names.size()>0){
				if(mail.getStatus().equals(Mail.Status.deliver) && mail.getAttachments().hasMatchingName(names.toArray(new String[names.size()]))){
					found=true;
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.reason","names"));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.names",Arrays.deepToString(mail.getAttachments().getFileNames().toArray())));
				}
			}
			if(types.size()>0){
				if(mail.getStatus().equals(Mail.Status.deliver) && mail.getAttachments().hasMatchingType(types.toArray(new String[types.size()]))){
					found=true;
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.reason","types"));
					mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock.types",Arrays.deepToString(mail.getAttachments().getTypes().toArray())));
				}
			}

			boolean droppedByAttachments=false;
			if(action!=null && action.equals(RETURN_ACTION) && found){
				log.debug("return message");
				ReplyParameters replyParameters = new ReplyParameters(noreplyMail,returnMessagePlain,returnMessage);
				replyParameters.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(),replyParameters);
				mail.drop("org.mxhero.feature.attachmentblock");
				droppedByAttachments=mail.getStatus().equals(Mail.Status.drop);
			}else if(action!=null && action.equals(COPY_ACTION) && found){
				for(String individualMail : email.split(",")){
					try {
						InternetAddress emailAddress = new InternetAddress(individualMail,false);
						if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
							mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
							mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
							mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.attachementblock.copy."+emailAddress.getAddress(), Boolean.TRUE.toString()));
						}
					} catch (AddressException e) {
						log.warn("wrong email address",e);
					}
				}
			}else if(action!=null && action.equals(REDIRECT_ACTION) && found){
				for(String individualMail : email.split(",")){
					try {
						InternetAddress emailAddress = new InternetAddress(individualMail,false);
						if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
							mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
							mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
							mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.attachementblock.redirect."+emailAddress.getAddress(), Boolean.TRUE.toString()));
						}
					} catch (AddressException e) {
						log.warn("wrong email address",e);
					}
				}				
				mail.redirect("org.mxhero.feature.attachmentblock");
			}else{
				if(found){
					mail.drop("org.mxhero.feature.attachmentblock");
					droppedByAttachments=mail.getStatus().equals(Mail.Status.drop);
				}
			}
			mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("org.mxhero.feature.attachementblock",Boolean.toString(droppedByAttachments)));
			if(droppedByAttachments){
				mail.cmd(LogStatCommand.class.getName(),new LogStatCommandParameters("email.blocked","org.mxhero.feature.attachementblock"));
			}
			mail.getHeaders().addHeader("X-mxHero-AttachmentBlock","rule="+ruleId.toString()+";result="+Boolean.toString(droppedByAttachments));
		}
		
	}
	
}
