package org.mxhero.feature.addressprotection.provider.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.ParameterList;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.threadlight.ThreadLightHeaders;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RulesByFeatureWithFixed extends RulesByFeature {
	
	private static Logger log = LoggerFactory.getLogger(RulesByFeatureWithFixed.class);
	private String group;
	private ThreadRowService service;
	
	@Override
	public Map<String, Set<CoreRule>> getRules() {
		Map<String, Set<CoreRule>> rules = super.getRules();
		if(rules==null){
			rules = new HashMap<String, Set<CoreRule>>();
		}
		if(rules.get(group)!=null){
			rules.get(group).add(getFixedRule());
		}else{
			HashSet<CoreRule> groupRules = new HashSet<CoreRule>();
			groupRules.add(getFixedRule());
			rules.put(group,groupRules);
		}
		return rules;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	private CoreRule getFixedRule(){
		CoreRule coreRule = new CoreRule(getFeature().getId()*(-1), getFeature().getBasePriority(), group);
		coreRule.addEvaluation(new APFixedEval());
		coreRule.addAction(new APFixedAction());
		return coreRule;
	}
	
	private class APFixedEval implements Evaluable{
		@Override
		public boolean eval(Mail mail) {
			if(log.isDebugEnabled()){
				log.debug("has follower:"+mail.getHeaders().hasHeader(ThreadLightHeaders.FOLLOWER));
				if(mail.getHeaders().hasHeader(ThreadLightHeaders.FOLLOWER)){
					log.debug("parameters:"+HeaderUtils.getParametersList(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID));
				}
			}
			return mail.getHeaders().hasHeader(ThreadLightHeaders.FOLLOWER)
			&& HeaderUtils.getParametersList(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID)!=null;
		}
	}
	
	private class APFixedAction implements Actionable{
		@Override
		public void exec(Mail mail) {
			ParameterList list = HeaderUtils.getParametersList(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID);
			log.debug(list.toString());
			
			List<String> allHeaderValues = new ArrayList<String>(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER));
			
			//find header and remove it from the list recovered
			for(String value : allHeaderValues){
				if(value.contains(Provider.FOLLOWER_ID)){
					allHeaderValues.remove(value);
					log.debug("found header to remove:"+value);
					break;
				}
			}
			//remove all headers and put them again but without the one processed
			mail.getHeaders().removeHeader(ThreadLightHeaders.FOLLOWER);
			for(String value : allHeaderValues){
				mail.getHeaders().addHeader(ThreadLightHeaders.FOLLOWER, value);
			}
			
			String[] allemails = list.get(ThreadLightHeaders.FOLLOWER_PARAMETERS).split(";");
			String[] toEmails = allemails[0].split(",");
			String[] ccEmails = allemails[1].split(",");
			String group = allemails[2];
			log.debug("toEmails:"+allemails[0]);
			log.debug("ccEmails:"+allemails[0]);
			log.debug("group:"+group);
			
			if(toEmails!=null && toEmails.length>0){
				for(String email : toEmails){
					if(email!=null && !email.trim().isEmpty()){
						mail.getRecipients().addRecipient(RecipientType.to, email);
						log.debug("added to recipient:"+email);
					}
				}
			}
			if(ccEmails!=null && ccEmails.length>0){
				for(String email : ccEmails){
					if(email!=null && !email.trim().isEmpty()){
						mail.getRecipients().addRecipient(RecipientType.cc, email);
						log.debug("added cc recipient:"+email);
					}
				}
			}
			
			if(toEmails!=null && toEmails.length>0){
				for(String email : toEmails){
					if(email!=null && !email.trim().isEmpty()){
						CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(),email);
						cloneParameters.setPhase(Mail.Phase.send);
						mail.cmd(Clone.class.getName(), cloneParameters);
						log.debug("sent email to:"+email);
					}
				}
			}
			if(ccEmails!=null && ccEmails.length>0){
				for(String email : ccEmails){
					if(email!=null && !email.trim().isEmpty()){
						CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(),email);
						cloneParameters.setPhase(Mail.Phase.send);
						mail.cmd(Clone.class.getName(), cloneParameters);
						log.debug("sent email to:"+email);
					}
				}
			}
			
			boolean removeRecipients = false;
			if(group==null){
				removeRecipients = true;
			}else if((group.equalsIgnoreCase("after") || group.equalsIgnoreCase("before")) && !mail.getRecipient().getDomain().getManaged()){
				removeRecipients = true;
			}else if(!mail.getRecipient().getDomain().hasAlias(group)){
				removeRecipients = true;
			}
			if(removeRecipients){
				log.debug("remove recipients");
				if(toEmails!=null && toEmails.length>0){
					for(String email : toEmails){
						if(email!=null && !email.trim().isEmpty()){
							mail.getRecipients().removeRecipient(RecipientType.to, email);
						}
					}
				}
				if(ccEmails!=null && ccEmails.length>0){
					for(String email : ccEmails){
						if(email!=null && !email.trim().isEmpty()){
							mail.getRecipients().removeRecipient(RecipientType.cc, email);
						}
					}
				}
			}
			if(service!=null){
				ThreadRowPk pk = new ThreadRowPk(mail.getHeaders().getHeaderValue(ThreadLightHeaders.MESSAGE_ID), mail.getHeaders().getHeaderValue(ThreadLightHeaders.SENDER), mail.getHeaders().getHeaderValue(ThreadLightHeaders.RECIPIENT));
				service.unfollow(pk, Provider.FOLLOWER_ID);
			}
		}
	}

	public ThreadRowService getService() {
		return service;
	}

	public void setService(ThreadRowService service) {
		this.service = service;
	}

}
