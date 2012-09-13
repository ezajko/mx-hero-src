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

import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class FromToEval implements Evaluable{

	private static Logger log = LoggerFactory.getLogger(FromToEval.class);
			
	public static final String DOMAIN = "domain";
	public static final String GROUP = "group";
	public static final String INDIVIDUAL = "individual";
	public static final String ANYONE = "anyone";
	public static final String ANYONEELSE = "anyoneelse";
	public static final String ALLDOMAINS = "alldomains";
	
	private RuleDirection from;
	private RuleDirection to;
	private Boolean twoWays;
	
	/**
	 * @param from
	 * @param to
	 * @param twoWays
	 */
	public FromToEval(RuleDirection from, RuleDirection to, Boolean twoWays){
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
			returnValue= DirectionEval.evalFrom(from, mail) && DirectionEval.evalTo(to, mail) || 
						DirectionEval.evalFrom(to, mail) && DirectionEval.evalTo(from, mail);
		}else{
			returnValue= DirectionEval.evalFrom(from, mail) && DirectionEval.evalTo(to, mail);
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
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FromToEval [from=").append(from).append(", to=")
				.append(to).append(", twoWays=").append(twoWays).append("]");
		return builder.toString();
	}

}
