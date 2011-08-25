package org.mxhero.engine.domain.rules;

import org.mxhero.engine.domain.feature.RuleDirection;
import org.mxhero.engine.domain.mail.business.Mail;

public class FromToEval implements Evaluable{


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
		if(twoWays){
			return evalFrom(from, mail) && evalTo(to, mail) || evalFrom(to, mail) && evalTo(from, mail);
		}else{
			return evalFrom(from, mail) && evalTo(to, mail);
		}
	}
	
	private boolean evalFrom(RuleDirection from, Mail mail){
		if(from.getDirectionType().equals(ANYONE)){
			return true;
		}else if(from.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			return !mail.getInitialData().getFromSender().getDomain().getId().equalsIgnoreCase(mail.getInitialData().getRecipient().getDomain().getId()) && !mail.getInitialData().getSender().getDomain().getId().equalsIgnoreCase(mail.getInitialData().getRecipient().getDomain().getId());
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			return mail.getInitialData().getFromSender().getDomain().getManaged() || mail.getInitialData().getSender().getDomain().getManaged();
		}else if(from.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			return mail.getInitialData().getFromSender().getDomain().getId().equalsIgnoreCase(from.getDomain()) || mail.getInitialData().getSender().getDomain().getId().equalsIgnoreCase(from.getDomain());
		}else if(from.getDirectionType().equals(GROUP)){
			/*group name and domain has to match*/
			return (mail.getInitialData().getFromSender().getGroup()!=null && mail.getInitialData().getFromSender().getGroup().equalsIgnoreCase(from.getGroup()) && mail.getInitialData().getFromSender().getDomain().getId().equalsIgnoreCase(from.getDomain())) || (mail.getInitialData().getSender().getGroup()!=null && mail.getInitialData().getSender().getGroup().equalsIgnoreCase(from.getGroup()) && mail.getInitialData().getSender().getDomain().getId().equalsIgnoreCase(from.getDomain()));
		}else if(from.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			return mail.getInitialData().getFromSender().getMail().equalsIgnoreCase(getMailFromRuleDirection(from)) || mail.getInitialData().getSender().getMail().equalsIgnoreCase(getMailFromRuleDirection(from));
		}
		return false;
	}
	
	
	private boolean evalTo(RuleDirection to, Mail mail){
		if(to.getDirectionType().equals(ANYONE)){
			return true;
		} else if(to.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			return mail.getInitialData().getRecipient().getDomain().getId().equalsIgnoreCase(mail.getInitialData().getSender().getDomain().getId());
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			return mail.getInitialData().getRecipient().getDomain().getManaged();
		}else if(to.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			return mail.getInitialData().getRecipient().getDomain().getId().equalsIgnoreCase(to.getDomain());
		}else if(to.getDirectionType().equals(GROUP)){
			/*group id ? What the hell is group id !!!*/
			return mail.getInitialData().getRecipient().getGroup().equalsIgnoreCase(to.getGroup()) && mail.getInitialData().getRecipient().getDomain().getId().equalsIgnoreCase(to.getDomain());
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			return mail.getInitialData().getRecipient().getMail().equalsIgnoreCase(getMailFromRuleDirection(to));
		}
		return false;
	}
	
	private String getMailFromRuleDirection(RuleDirection direction){
		String mail = null;
		if(direction.getDirectionType().equalsIgnoreCase(INDIVIDUAL)){
			if(direction.getAccount()!=null 
					&& direction.getDomain()!=null
					&& direction.getAccount().trim().length()>0
					&& direction.getDomain().trim().length()>0){
				mail = direction.getAccount().trim().toLowerCase()+"@"+direction.getDomain().trim().toLowerCase();
			}else{
				mail = direction.getFreeValue().trim().toLowerCase();
			}
		}
		return mail;
	}

}
