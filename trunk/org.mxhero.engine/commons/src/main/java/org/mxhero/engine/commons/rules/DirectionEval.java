package org.mxhero.engine.commons.rules;

import org.mxhero.engine.commons.feature.RuleDirection;
import org.mxhero.engine.commons.mail.api.Mail;

public abstract class DirectionEval {

	public static final String DOMAIN = "domain";
	public static final String GROUP = "group";
	public static final String INDIVIDUAL = "individual";
	public static final String ANYONE = "anyone";
	public static final String ANYONEELSE = "anyoneelse";
	public static final String ALLDOMAINS = "alldomains";
	
	public static boolean evalFrom(RuleDirection from, Mail mail){
		if(from.getDirectionType().equals(ANYONE)){
			return true;
		}else if(from.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			return !(mail.getSender().getDomain().hasAlias(mail.getRecipient().getDomain().getAliases()) 
					|| mail.getFromSender().getDomain().hasAlias(mail.getRecipient().getDomain().getAliases()));
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			return mail.getFromSender().getDomain().getManaged() || mail.getSender().getDomain().getManaged();
		}else if(from.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			return mail.getFromSender().getDomain().hasAlias(getDomainFromRuleDirection(from)) || mail.getSender().getDomain().hasAlias(getDomainFromRuleDirection(from));
		}else if(from.getDirectionType().equals(GROUP)){
			/*group name and domain has to match*/
			return (mail.getFromSender().getGroup()!=null 
						&& mail.getFromSender().getGroup().equalsIgnoreCase(from.getGroup()) 
						&& mail.getFromSender().getDomain().getId().equalsIgnoreCase(from.getDomain())) 
					|| (mail.getSender().getGroup()!=null 
							&& mail.getSender().getGroup().equalsIgnoreCase(from.getGroup()) 
							&& mail.getSender().getDomain().getId().equalsIgnoreCase(from.getDomain()));
		}else if(from.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			return mail.getFromSender().hasAlias(getMailFromRuleDirection(from)) || mail.getSender().getMail().equalsIgnoreCase(getMailFromRuleDirection(from));
		}
		return false;
	}
	
	
	public static boolean evalTo(RuleDirection to, Mail mail){
		if(to.getDirectionType().equals(ANYONE)){
			return true;
		} else if(to.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			return !(mail.getRecipient().getDomain().hasAlias(mail.getSender().getDomain().getAliases()) 
					|| mail.getRecipient().getDomain().hasAlias(mail.getFromSender().getDomain().getAliases()));
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			return mail.getRecipient().getDomain().getManaged();
		}else if(to.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			return mail.getRecipient().getDomain().hasAlias(getDomainFromRuleDirection(to));
		}else if(to.getDirectionType().equals(GROUP)){
			/*group */
			return mail.getRecipient().getGroup()!=null 
			&& mail.getRecipient().getGroup().equalsIgnoreCase(to.getGroup()) 
			&& mail.getRecipient().getDomain().getId().equalsIgnoreCase(to.getDomain());
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			return mail.getRecipient().hasAlias(getMailFromRuleDirection(to));
		}
		return false;
	}
	
	public static String getDomainFromRuleDirection(RuleDirection direction){
			if(direction.getDomain()==null){
				return direction.getFreeValue();
			}else{
				return direction.getDomain();
			}
	}
	
	public static String getMailFromRuleDirection(RuleDirection direction){
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
