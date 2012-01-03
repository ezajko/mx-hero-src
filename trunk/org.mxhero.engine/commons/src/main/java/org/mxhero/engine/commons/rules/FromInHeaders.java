package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromInHeaders implements Evaluable{

	private static Logger log = LoggerFactory.getLogger(FromInHeaders.class);
	
	private RuleDirection from;
	private RuleDirection to;
	private Boolean twoWays;
	
	public FromInHeaders(RuleDirection from, RuleDirection to, Boolean twoWays){
		this.from=from;
		this.to=to;
		this.twoWays=twoWays;
	}
	
	@Override
	public boolean eval(Mail mail) {

		boolean returnValue=false;
		if(twoWays){
			returnValue= DirectionEval.evalFrom(from, mail) && evalInHeaders(to, mail) || 
						DirectionEval.evalFrom(to, mail) && evalInHeaders(from, mail);
		}else{
			returnValue= DirectionEval.evalFrom(from, mail) && evalInHeaders(to, mail);
		}
		if(log.isTraceEnabled()){
			log.trace(" sender:"+mail.getInitialData().getSender()
					+" senderDomain:"
					+mail.getInitialData().getSender().getDomain()
					+" recipient:"
					+mail.getInitialData().getRecipient()
					+" recipientDomain:"
					+mail.getInitialData().getRecipient().getDomain()
					+" getMailFromRuleDirection(from):"
					+DirectionEval.getMailFromRuleDirection(from)
					+"getMailFromRuleDirection(to):"
					+DirectionEval.getMailFromRuleDirection(to));
		}
		return returnValue;
	}
	
	private boolean evalInHeaders(RuleDirection toDirection, Mail mail){
		if(mail.getInitialData().getRecipientsInHeaders()!=null){
			for(User user : mail.getInitialData().getRecipientsInHeaders()){
				if(evalUser(toDirection,mail,user)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean evalUser(RuleDirection to, Mail mail, User user){
		if(to.getDirectionType().equals(DirectionEval.ANYONE)){
			return true;
		} else if(to.getDirectionType().equals(DirectionEval.ANYONEELSE)){
			/*anyone not in the other side domain*/
			return !(user.getDomain().hasAlias(mail.getInitialData().getSender().getDomain().getAliases()) 
					|| user.getDomain().hasAlias(mail.getInitialData().getFromSender().getDomain().getAliases()));
		}else if(to.getDirectionType().equals(DirectionEval.ALLDOMAINS)){
			/*is this side managed?*/
			return user.getDomain().getManaged();
		}else if(to.getDirectionType().equals(DirectionEval.DOMAIN)){
			/*domain id is equal to*/
			return user.getDomain().hasAlias(DirectionEval.getDomainFromRuleDirection(to));
		}else if(to.getDirectionType().equals(DirectionEval.GROUP)){
			/*group */
			return user.getGroup()!=null 
			&& user.getGroup().equalsIgnoreCase(to.getGroup()) 
			&& user.getDomain().getId().equalsIgnoreCase(to.getDomain());
		}else if(to.getDirectionType().equals(DirectionEval.INDIVIDUAL)){
			/* user from this side is equal to*/
			return user.hasAlias(DirectionEval.getMailFromRuleDirection(to));
		}
		return false;
	}
}
