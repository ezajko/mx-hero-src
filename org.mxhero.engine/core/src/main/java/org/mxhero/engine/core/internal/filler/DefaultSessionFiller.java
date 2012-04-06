package org.mxhero.engine.core.internal.filler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.core.internal.mail.CMail;
import org.mxhero.engine.core.internal.mail.CRecipients;

/**
 * This class add the business objects to the session.
 * 
 * @author mmarmol
 */
public class DefaultSessionFiller implements SessionFiller {

	private static final char DIV_CHAR = '@';


	/**
	 * @see org.mxhero.engine.core.internal.filler.SessionFiller#fill(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public void fill(UserFinder userFinder, MimeMail mail) {
		mail.setBussinesObject(getInitialData(userFinder, mail));
	}

	
	/**
	 * @param userFinder
	 * @param domainFinder
	 * @param mail
	 * @return
	 */
	private CMail getInitialData(UserFinder userFinder, MimeMail mail){
		CMail cMail = null;
		String fromId = null;
		String fromDomainId = null;
		User sender = null;
		Domain senderDomain = null;
		User from = null;
		Domain fromDomain = null;
		User recipient = null;
		Domain recipientDomain = null;
		
		if (mail != null) {
			mail.setSenderId(mail.getSender().trim());
			mail.setRecipientId(mail.getRecipient().trim());
			mail.setSenderDomainId(mail.getSenderId().substring(mail.getSenderId().indexOf(DIV_CHAR) + 1).trim());
			mail.setRecipientDomainId(mail.getRecipientId().substring(mail.getRecipientId().indexOf(DIV_CHAR) + 1).trim());
			
			if (userFinder != null) {
				sender = userFinder.getUser(mail.getSenderId());
				if(sender!=null){
					senderDomain=sender.getDomain();
					mail.setSenderId(sender.getMail());
					mail.setSenderDomainId(senderDomain.getId());
				}
			}

			try {
				if(mail.getMessage().getFrom()!=null && mail.getMessage().getFrom().length>0){
					fromId = new InternetAddress(mail.getMessage().getFrom()[0].toString()).getAddress().trim();
				}else{
					fromId = mail.getSenderId();
				}
			} catch (Exception e) {
				fromId = mail.getSenderId();
			} 
			
			fromDomainId = fromId.substring(fromId.indexOf(DIV_CHAR) + 1).trim();
			
			if(!fromId.equals(mail.getSenderId())){
				if (userFinder != null) {
					from = userFinder.getUser(fromId);
					if(from!=null){
						fromDomain = from.getDomain();
					}
				}
			}else{
				fromDomain = senderDomain;
				from = sender;
			}

			
			if (userFinder != null) {
				recipient = userFinder.getUser(mail.getRecipientId());
				if(recipient!=null){
					recipientDomain=recipient.getDomain();
					mail.setRecipientDomainId(recipientDomain.getId());
					mail.setRecipientId(recipient.getMail());
				}
			}
		
			if (senderDomain == null){
				senderDomain = new Domain();
				senderDomain.setId(mail.getSenderDomainId());
				senderDomain.setManaged(false);
				senderDomain.setAliases(new HashSet<String>());
				senderDomain.getAliases().add(senderDomain.getId());
			}
			
			if (fromDomain == null){
				fromDomain = new Domain();
				fromDomain.setId(fromDomainId);
				fromDomain.setManaged(false);
				fromDomain.setAliases(new HashSet<String>());
				fromDomain.getAliases().add(fromDomain.getId());
			}
			
			if(recipientDomain==null){
				recipientDomain = new Domain();
				recipientDomain.setId(mail.getRecipientDomainId());
				recipientDomain.setManaged(false);
				recipientDomain.setAliases(new HashSet<String>());
				recipientDomain.getAliases().add(recipientDomain.getId());
			}
			
			if (sender == null){
				sender = new User();
				sender.setMail(mail.getSenderId());
				sender.setManaged(false);
				sender.setDomain(senderDomain);
				sender.setAliases(new HashSet<String>());
				sender.getAliases().add(sender.getMail());
			}
			
			if (from == null){
				from = new User();
				from.setMail(fromId);
				from.setManaged(false);
				from.setDomain(fromDomain);
				from.setAliases(new HashSet<String>());
				from.getAliases().add(from.getMail());
			}
			
			if (recipient == null){
				recipient = new User();
				recipient.setMail(mail.getRecipientId());
				recipient.setManaged(false);
				recipient.setDomain(recipientDomain);
				recipient.setAliases(new HashSet<String>());
				recipient.getAliases().add(recipient.getMail());
			}
			
			if(sender.getDomain().getManaged() && recipient.getDomain().getManaged()){
				mail.setFlow(Mail.Flow.both);
			}else if(sender.getDomain().getManaged()){
				mail.setFlow(Mail.Flow.out);
			}else if(recipient.getDomain().getManaged()){
				mail.setFlow(Mail.Flow.in);
			}else {
				mail.setFlow(Mail.Flow.none);
			}
			
			mail.setSenderGroup(sender.getGroup());
			mail.setRecipientGroup(recipient.getGroup());
			
			List<User> recipientsInHeader = new ArrayList<User>();
			
			for(String address : new CRecipients(mail).getRecipients(RecipientType.all)){
				recipientsInHeader.add(getUser(userFinder,address));
			}
			
			cMail = new CMail(mail, sender, recipient, from, recipientsInHeader);
		}
		
		return cMail;
	}


	private User getUser(UserFinder userFinder, String email){
		User user = userFinder.getUser(email);
		if(user==null){
			user = new User();
			user.setMail(email);
			user.setManaged(false);
			user.setAliases(new HashSet<String>());
			user.getAliases().add(email);
			user.setDomain(new Domain());
			user.getDomain().setId(email.substring(email.indexOf(DIV_CHAR) + 1).trim());
			user.getDomain().setManaged(false);
			user.getDomain().setAliases(new HashSet<String>());
			user.getDomain().getAliases().add(email);
		}
		return user;
	}
	
}

