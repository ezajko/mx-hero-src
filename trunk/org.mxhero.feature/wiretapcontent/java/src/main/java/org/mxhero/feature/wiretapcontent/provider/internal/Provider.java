/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.feature.wiretapcontent.provider.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String ANDOR_SELECTION_PROPERTY = "andor.selection";
	private static final String WORD_LIST_PROPERTY = "word.list";
	private static final String EMAIL_VALUE_PROPERTY = "email.value";
	private static final String AND_VALUE = "and";
	private static final String OR_VALUE = "or";
	private static final String FOUND_ACTION = "found.action";
	private static final String FOUND_ACTION_COPY = "copy";
	private static final String FOUND_ACTION_REDIRECT = "redirect";
	private static final String FOUND_ACTION_DROP = "drop";
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		
		String andor = null;
		String emailCopy = null;
		List<String> words = new ArrayList<String>();
		String action = null;
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ANDOR_SELECTION_PROPERTY)){
				andor=property.getValue();
			} else if(property.getKey().equals(WORD_LIST_PROPERTY)){
				words.add(StringEscapeUtils.escapeJava(property.getValue()));
			} else if(property.getKey().equals(EMAIL_VALUE_PROPERTY)){
				emailCopy = property.getValue().toLowerCase();
			} else if(property.getKey().equals(FOUND_ACTION)){
				action = property.getValue();
			}
		}

		coreRule.addEvaluation(new WCEvaluate(andor, words));
		coreRule.addAction(new WCAction(coreRule.getId(), emailCopy, action));
		
		return coreRule;
	}

	private class WCEvaluate implements Evaluable{

		private String andor;
		private String[] words;
		
		public WCEvaluate(String andor, List<String> words) {
			super();
			this.andor = andor;
			if(words!=null){
				this.words = words.toArray(new String[words.size()]);
			}
			
		}

		@Override
		public boolean eval(Mail mail) {
			return mail.getStatus().equals(Mail.Status.deliver)
			&& mail.getHeaders()!=null
			&& mail.getBody()!=null
			&&(andor!=null && 
				((andor.equals(AND_VALUE) && subjectHasAll(mail.getSubject(),words))
				||(andor.equals(OR_VALUE) && subjectHasAny(mail.getSubject(),words))
				||(andor.equals(AND_VALUE)&&(mail.getBody().textHasAll(words))||mail.getBody().htmlTextHasAll(words))
				||(andor.equals(OR_VALUE)&&(mail.getBody().textHasAny(words))||mail.getBody().textHasAny(words))));
		}
		
		public boolean subjectHasAll(String subject, String... words) {
			for (String word : words){
				if(!Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(subject).find()){
					return false;
				}
			}
			return true;
		}
		
		public boolean subjectHasAny(String subject, String... words) {
			for (String word : words){
				if(Pattern.compile("(\\W|^)"+word+"(\\W|$)", Pattern.CASE_INSENSITIVE).matcher(subject).find()){
					return true;
				}
			}
			return false;
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
			if(action==null || action.equalsIgnoreCase(FOUND_ACTION_COPY)){
				for(String individualMail : emailCopy.split(",")){
					try {
						InternetAddress emailAddress = new InternetAddress(individualMail,false);
						if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
							mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
							mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
							mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapcontent.copy."+emailAddress.getAddress(), Boolean.TRUE.toString()));
						}
					} catch (AddressException e) {
						log.warn("wrong email address",e);
					}
				}
			}else if(action.equalsIgnoreCase(FOUND_ACTION_REDIRECT)){
				for(String individualMail : emailCopy.split(",")){
					try {
						InternetAddress emailAddress = new InternetAddress(individualMail,false);
						if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
							mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
							mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));
							mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapcontent.redirect."+emailAddress.getAddress(), Boolean.TRUE.toString()));
							mail.getHeaders().addHeader("X-mxHero-WiretapContent","rule:"+ruleId);
						}
					} catch (AddressException e) {
						log.warn("wrong email address",e);
					}
				}				
				mail.redirect("org.mxhero.feature.wiretapcontent");
			}else if(action.equalsIgnoreCase(FOUND_ACTION_DROP)){
				mail.drop("org.mxhero.feature.wiretapcontent");
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.wiretapcontent", Boolean.TRUE.toString()));
		}
		
	}
	
}
