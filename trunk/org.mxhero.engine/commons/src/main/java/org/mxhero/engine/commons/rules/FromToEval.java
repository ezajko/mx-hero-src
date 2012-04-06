package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.mail.api.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public FromToEval(RuleDirection from, RuleDirection to, Boolean twoWays){
		this.from=from;
		this.to=to;
		this.twoWays=twoWays;
	}
	
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
	


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FromToEval [from=").append(from).append(", to=")
				.append(to).append(", twoWays=").append(twoWays).append("]");
		return builder.toString();
	}

}
