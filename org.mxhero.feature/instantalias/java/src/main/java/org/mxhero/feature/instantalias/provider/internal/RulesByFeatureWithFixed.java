package org.mxhero.feature.instantalias.provider.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class RulesByFeatureWithFixed extends RulesByFeature {

	protected static final String REPLY_ALIAS="--";
	protected static final String REPLY_START="mxhero-instantalias";
	
	private static Logger log = LoggerFactory.getLogger(RulesByFeatureWithFixed.class);
	private String group;
	
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
		coreRule.addEvaluation(new IAFixedEval());
		coreRule.addAction(new IAFixedAction());
		return coreRule;
	}
	
	private class IAFixedEval implements Evaluable{
		@Override
		public boolean eval(Mail mail) {
			boolean result = mail.getState().equalsIgnoreCase(MailState.DELIVER) &&
					mail.getInitialData().getRecipient().getMail().startsWith(REPLY_START)
					&& mail.getInitialData().getSender().getDomain().getManaged();
			log.debug("result:"+result);
			return result;
		}
	}
	
	private class IAFixedAction implements Actionable{
		@Override
		public void exec(Mail mail) {
			String[] emailComposition = mail.getInitialData().getRecipient().getMail().split(REPLY_ALIAS);
			String senderDomain=emailComposition[2];
			String senderAccount=emailComposition[1];
			log.debug("senderDomain:"+senderDomain);
			log.debug("senderAccount:"+senderAccount);
			//if sender is from the domain of the alias and the account is in the alias
			if(mail.getInitialData().getSender().getDomain().hasAlias(senderDomain)
					&& senderAccount.startsWith(mail.getInitialData().getSender().getMail().split("@")[0])){
					String sender = senderAccount+"@"+senderDomain;
					String recipient = mail.getInitialData().getRecipient().getMail().substring(emailComposition[0].length()+emailComposition[1].length()+emailComposition[2].length()+(REPLY_ALIAS.length()*3));
					log.debug("sender:"+sender+", recipient:"+recipient);
					mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,sender,recipient,null,"false","both");
					mail.drop("org.mxhero.feature.redirect");
			} else{
				log.debug("no match");
			}
		}
	}
}
