package org.mxhero.feature.wiretapcontent.provider.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;

public class Provider extends RulesByFeature{

	private static final String ANDOR_SELECTION_PROPERTY = "andor.selection";
	private static final String WORD_LIST_PROPERTY = "word.list";
	private static final String EMAIL_VALUE_PROPERTY = "email.value";
	private static final String AND_VALUE = "and";
	private static final String OR_VALUE = "or";
	private static final String FOUND_ACTION = "found.action";
	private static final String FOUND_ACTION_COPY = "found.action";
	private static final String FOUND_ACTION_REDIRECT = "found.action";
	private static final String FOUND_ACTION_DROP = "found.action";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String andor = null;
		String emailCopy = null;
		List<String> words = new ArrayList<String>();
		String action = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(ANDOR_SELECTION_PROPERTY)){
				andor=property.getPropertyValue();
			} else if(property.getPropertyKey().equals(WORD_LIST_PROPERTY)){
				words.add(StringEscapeUtils.escapeJava(property.getPropertyValue()));
			} else if(property.getPropertyKey().equals(EMAIL_VALUE_PROPERTY)){
				emailCopy = property.getPropertyValue();
			} else if(property.getPropertyKey().equals(FOUND_ACTION)){
				action = property.getPropertyValue();
			}
		}

		coreRule.addEvaluation(new WCEvaluate(andor, emailCopy, words));
		coreRule.addAction(new WCAction(coreRule.getId(), emailCopy, action));
		
		return coreRule;
	}

	private class WCEvaluate implements Evaluable{

		private String andor;
		private String emailCopy;
		private List<String> words;
		
		public WCEvaluate(String andor, String emailCopy, List<String> words) {
			super();
			this.andor = andor;
			this.emailCopy = emailCopy;
			this.words = words;
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getState().equalsIgnoreCase(MailState.DELIVER)
			&& mail.getHeaders()!=null
			&& mail.getBody()!=null
			&& !mail.getProperties().containsKey("redirected:"+emailCopy)
			&&(andor!=null && ((andor.equals(AND_VALUE)&&(mail.getBody().textHasAll(words)||mail.getBody().htmlTextHasAll(words)))
								||(andor.equals(OR_VALUE)&&(mail.getBody().textHasAny(words)||mail.getBody().textHasAny(words)))));
		}
		
	}
	
	private class WCAction implements Actionable{

		private Integer ruleId;
		private String emailCopy;
		private String action;
		
		public WCAction(Integer ruleId, String emailCopy, String action) {
			super();
			this.ruleId = ruleId;
			this.emailCopy = emailCopy;
			this.action = action;
		}

		@Override
		public void exec(Mail mail) {
			mail.getHeaders().addHeader("X-mxHero-WiretapContent","rule="+ruleId+";hidden_copied:"+emailCopy);
			if(action==null || action.equalsIgnoreCase(FOUND_ACTION_COPY)){
				mail.getProperties().put("redirected:"+emailCopy,ruleId.toString());
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),emailCopy);
			}else if(action.equalsIgnoreCase(FOUND_ACTION_REDIRECT)){
				mail.getProperties().put("redirected:"+emailCopy,ruleId.toString());
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Clone",RulePhase.RECEIVE,mail.getInitialData().getSender().getMail(),emailCopy);
				mail.drop("org.mxhero.feature.redirect");
			}else if(action.equalsIgnoreCase(FOUND_ACTION_DROP)){
				mail.drop("org.mxhero.feature.redirect");
			}
			mail.cmd("org.mxhero.engine.plugin.statistics.command.LogStat","org.mxhero.feature.wiretapcontent","true");
		}
		
	}
	
}
