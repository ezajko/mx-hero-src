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

package org.mxhero.engine.commons.rules;

import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class CoreRule implements Comparable<CoreRule>{

	private static Logger log = LoggerFactory.getLogger(CoreRule.class);
	
	private Integer id;
	
	private Integer priority;

	private String group;
	
	private List<Evaluable> evals;
	
	private List<Actionable> actions;

	/**
	 * @param id
	 * @param priority
	 * @param group
	 */
	public CoreRule(Integer id, Integer priority, String group){
		this.id=id;
		this.priority=priority;
		this.group=group;
	}
	
	/**
	 * @param mail
	 */
	public void process(Mail mail){
		//rules should have at least one action
		if(evals!=null && actions!=null && actions.size()>0){
			if(mail.getForcedPhasePriority()!=null && this.priority<mail.getForcedPhasePriority()){
				log.trace("skipped ruleId="+this.getId()+" priority="+priority+" forcedPhasePriority="+mail.getForcedPhasePriority());
				return;
			}else{
				log.trace("NOT skipped ruleId="+this.getId()+" priority="+priority+" forcedPhasePriority="+mail.getForcedPhasePriority());
			}
			for(Evaluable eval : evals){
				//if any evaluation is false, just return false and do not make any action
				if(eval.eval(mail)==false){
					if(log.isTraceEnabled()){
						log.trace(eval.toString()+" is false");
					}
					return;
				}
				if(log.isTraceEnabled()){
					log.trace(eval.getClass().getSimpleName()+" is true");
				}
			}
			//just execute all actions
			log.debug("rule fired "+this.toString());
			for(Actionable action : actions){
				action.exec(mail);
				if(log.isTraceEnabled()){
					log.trace(action.getClass().getSimpleName()+" executed");
				}
			}
		}
	}
	
	/**
	 * @param evaluation
	 */
	public void addEvaluation(Evaluable evaluation){
		if(evals==null){
			evals=new ArrayList<Evaluable>();
		}
		evals.add(evaluation);
	}
	
	/**
	 * @param action
	 */
	public void addAction(Actionable action){
		if(actions==null){
			actions= new ArrayList<Actionable>();
		}
		actions.add(action);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoreRule other = (CoreRule) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CoreRule o) {
		return priority.compareTo(o.priority)*-1;
	}
	
	/**
	 * @return
	 */
	public String getGroup(){
		return this.group;
	}
	
	/**
	 * @return
	 */
	public Integer getId(){
		return id;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoreRule [id=").append(id).append(", priority=")
				.append(priority).append(", group=").append(group).append("]");
		return builder.toString();
	}
	
}
