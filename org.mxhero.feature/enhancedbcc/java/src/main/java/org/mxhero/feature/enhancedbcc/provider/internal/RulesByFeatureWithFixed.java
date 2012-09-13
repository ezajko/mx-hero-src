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

package org.mxhero.feature.enhancedbcc.provider.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.ParameterList;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
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
					log.debug("parameters:"+HeaderUtils.getParametersListLike(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID));
				}
			}
			return mail.getHeaders().hasHeader(ThreadLightHeaders.FOLLOWER)
			&& HeaderUtils.getParametersListLike(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID)!=null;
		}
	}
	
	private class APFixedAction implements Actionable{
		@Override
		public void exec(Mail mail) {
			Collection<ParameterList> list = HeaderUtils.getParametersListLike(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER).toArray(new String[0]), Provider.FOLLOWER_ID, ThreadLightHeaders.FOLLOWER_ID);
			
			List<String> allHeaderValues = new ArrayList<String>(mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER));
			
			//find header and remove it from the list recovered
			for(String value : mail.getHeaders().getHeaderValues(ThreadLightHeaders.FOLLOWER)){
				if(value.contains(Provider.FOLLOWER_ID)){
					allHeaderValues.remove(value);
					log.debug("found header to remove:"+value);
				}
			}
			//remove all headers and put them again but without the one processed
			mail.getHeaders().removeHeader(ThreadLightHeaders.FOLLOWER);
			for(String value : allHeaderValues){
				mail.getHeaders().addHeader(ThreadLightHeaders.FOLLOWER, value);
			}
			
			for(ParameterList follower : list){
				String email = follower.get(ThreadLightHeaders.FOLLOWER_ID).replace(Provider.FOLLOWER_ID+".", "");
				if(email!=null && !email.trim().isEmpty()){
					CloneParameters cloneParameters = new CloneParameters(mail.getSender().getMail(),email);
					cloneParameters.setPhase(Mail.Phase.send);
					mail.cmd(Clone.class.getName(), cloneParameters);
					log.debug("sent email to:"+email);
					if(service!=null){
						ThreadRowPk pk = new ThreadRowPk(mail.getHeaders().getHeaderValue(ThreadLightHeaders.MESSAGE_ID), mail.getHeaders().getHeaderValue(ThreadLightHeaders.SENDER), mail.getHeaders().getHeaderValue(ThreadLightHeaders.RECIPIENT));
						service.unfollow(pk, Provider.FOLLOWER_ID+"."+email);
					}
					mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.enhancedbcc.recipeint."+email, "bcc"));
				}
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
