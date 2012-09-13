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

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 */
public class FromInHeaders implements Evaluable{

	private static Logger log = LoggerFactory.getLogger(FromInHeaders.class);
	
	private RuleDirection from;
	private RuleDirection to;
	private Boolean twoWays;
	
	/**
	 * @param from
	 * @param to
	 * @param twoWays
	 */
	public FromInHeaders(RuleDirection from, RuleDirection to, Boolean twoWays){
		this.from=from;
		this.to=to;
		this.twoWays=twoWays;
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.commons.rules.Evaluable#eval(org.mxhero.engine.commons.mail.api.Mail)
	 */
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
			log.trace(" sender:"+mail.getSender()
					+" senderDomain:"
					+mail.getSender().getDomain()
					+" recipient:"
					+mail.getRecipient()
					+" recipientDomain:"
					+mail.getRecipient().getDomain()
					+" getMailFromRuleDirection(from):"
					+DirectionEval.getMailFromRuleDirection(from)
					+"getMailFromRuleDirection(to):"
					+DirectionEval.getMailFromRuleDirection(to));
		}
		return returnValue;
	}
	
	/**
	 * @param toDirection
	 * @param mail
	 * @return
	 */
	private boolean evalInHeaders(RuleDirection toDirection, Mail mail){
		if(mail.getRecipientsInHeaders()!=null){
			for(User user : mail.getRecipientsInHeaders()){
				if(evalUser(toDirection,mail,user)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param to
	 * @param mail
	 * @param user
	 * @return
	 */
	private boolean evalUser(RuleDirection to, Mail mail, User user){
		if(to.getDirectionType().equals(DirectionEval.ANYONE)){
			return true;
		} else if(to.getDirectionType().equals(DirectionEval.ANYONEELSE)){
			/*anyone not in the other side domain*/
			return !(user.getDomain().hasAlias(mail.getSender().getDomain().getAliases()) 
					|| user.getDomain().hasAlias(mail.getFromSender().getDomain().getAliases()));
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
