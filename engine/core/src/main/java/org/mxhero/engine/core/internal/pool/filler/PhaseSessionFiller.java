package org.mxhero.engine.core.internal.pool.filler;

import java.util.HashSet;

import javax.mail.internet.InternetAddress;

import org.mxhero.engine.core.mail.InitialDataVO;
import org.mxhero.engine.core.mail.MailVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.MailFlow;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.properties.PropertiesService;

/**
 * This class add the business objects to the session.
 * 
 * @author mmarmol
 */
public class PhaseSessionFiller implements SessionFiller {

	private static final char DIV_CHAR = '@';

	private PropertiesService properties;

	
	/**
	 * @see org.mxhero.engine.core.internal.pool.filler.SessionFiller#fill(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public MailVO fill(UserFinder userFinder, MimeMail mail) {
		return new MailVO(mail,getInitialData(userFinder, mail));
	}

	
	/**
	 * @param userFinder
	 * @param domainFinder
	 * @param mail
	 * @return
	 */
	private InitialDataVO getInitialData(UserFinder userFinder, MimeMail mail){
		InitialDataVO initialData = null;
		String fromId = null;
		String fromDomainId = null;
		User sender = null;
		Domain senderDomain = null;
		User from = null;
		Domain fromDomain = null;
		User recipient = null;
		Domain recipientDomain = null;
		
		if (mail != null) {
			mail.setSenderId(mail.getInitialSender().trim());
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
				mail.setFlow(MailFlow.BOTH);
			}else if(sender.getDomain().getManaged()){
				mail.setFlow(MailFlow.OUT);
			}else if(recipient.getDomain().getManaged()){
				mail.setFlow(MailFlow.IN);
			}else {
				mail.setFlow(MailFlow.NONE);
			}
			
			initialData = new InitialDataVO(mail, sender, recipient, from);
		}
		
		return initialData;
	}
	

	/**
	 * @return the properties
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}

