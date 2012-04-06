package org.mxhero.engine.commons.rules;

import java.util.ArrayList;
import java.util.List;

import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreRule implements Comparable<CoreRule>{

	private static Logger log = LoggerFactory.getLogger(CoreRule.class);
	
	private Integer id;
	
	private Integer priority;

	private String group;
	
	private List<Evaluable> evals;
	
	private List<Actionable> actions;

	public CoreRule(Integer id, Integer priority, String group){
		this.id=id;
		this.priority=priority;
		this.group=group;
	}
	
	public void process(Mail mail){
		//rules should have at least one action
		if(evals!=null && actions!=null && actions.size()>0){
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
	
	public void addEvaluation(Evaluable evaluation){
		if(evals==null){
			evals=new ArrayList<Evaluable>();
		}
		evals.add(evaluation);
	}
	
	public void addAction(Actionable action){
		if(actions==null){
			actions= new ArrayList<Actionable>();
		}
		actions.add(action);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}

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

	@Override
	public int compareTo(CoreRule o) {
		return priority.compareTo(o.priority)*-1;
	}
	
	public String getGroup(){
		return this.group;
	}
	
	public Integer getId(){
		return id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoreRule [group=").append(group).append(", id=")
				.append(id).append("]");
		return builder.toString();
	}
	
}
