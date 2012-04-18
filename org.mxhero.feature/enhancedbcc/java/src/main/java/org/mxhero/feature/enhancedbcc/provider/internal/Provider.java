package org.mxhero.feature.enhancedbcc.provider.internal;

import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatchParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeatureWithFixed{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	public static final String FOLLOWER_ID = "org.mxhero.feature.enhancedbcc";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		coreRule.addEvaluation(new EBEvaluation());
		coreRule.addAction(new EBAction());
		return coreRule;
	}

	
	private class EBEvaluation implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			List<String> allRecipients = new ArrayList<String>();
			if(mail.getRecipients().getRecipients(RecipientType.cc)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.cc));
			}
			if(mail.getRecipients().getRecipients(RecipientType.to)!=null){
				allRecipients.addAll(mail.getRecipients().getRecipients(RecipientType.to));
			}
			return mail.getStatus().equals(Mail.Status.deliver) 
					&& mail.getRecipients()!=null
					&& !mail.getProperties().containsKey("org.mxhero.feature.bccusagedetection")
					&& !mail.getProperties().containsKey("org.mxhero.engine.plugin.basecommands.command.Reply")
					&& !mail.getRecipient().hasAlias(allRecipients.toArray(new String[allRecipients.size()]));
		}
		
	}
	
	private class EBAction implements Actionable{

		@Override
		public void exec(Mail mail) {
			for(User user : mail.getRecipientsInHeaders()){
				AddThreadWatchParameters parameters = new AddThreadWatchParameters(FOLLOWER_ID+"."+mail.getRecipient().getMail(),"none");
				parameters.setSenderId(user.getMail());
				parameters.setRecipientId(mail.getSender().getMail());
				log.debug("wathc thread for sender:"+user.getMail()+" and recipient:"+mail.getSender().getMail());
				mail.cmd(AddThreadWatch.class.getName(), parameters);
			}
			
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.addressprotection", "true"));
		}
		
	}
}
