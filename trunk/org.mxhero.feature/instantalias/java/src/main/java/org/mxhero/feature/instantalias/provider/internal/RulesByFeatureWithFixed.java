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

public abstract class RulesByFeatureWithFixed extends RulesByFeature {

	protected static final String REPLY_ALIAS="--";
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
			return mail.getState().equalsIgnoreCase(MailState.DELIVER) &&
					mail.getInitialData().getRecipient().getMail().startsWith("org.mxhero.feature.instantalias");
		}
	}
	
	private class IAFixedAction implements Actionable{
		@Override
		public void exec(Mail mail) {
			//"org.mxhero.feature.instantalias+"+alias+"+"+domain+"+"+replyTo;
			String[] emailComposition = mail.getInitialData().getRecipient().getMail().split(REPLY_ALIAS);
			String sender = emailComposition[1]+"@"+emailComposition[2];
			String recipient = mail.getInitialData().getRecipient().getMail().substring(emailComposition[0].length()+emailComposition[1].length()+emailComposition[2].length()+(REPLY_ALIAS.length()*3));
			mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,sender,recipient,null,"true","both");
			mail.drop("org.mxhero.feature.redirect");
		}
	}
}
