package org.mxhero.engine.core.internal.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Recipients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class CRecipients implements Recipients{

	private static Logger log = LoggerFactory.getLogger(CRecipients.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public CRecipients(MimeMail mimeMail) {
		this.mimeMail = mimeMail;
	}

	@Override
	public Collection<String> getRecipients(RecipientType type) {
		Address[] addresses = null;
		Collection<String> recipients = new ArrayList<String>();
		try {
			switch(type){
				case to : addresses = mimeMail.getMessage().getRecipients(javax.mail.internet.MimeMessage.RecipientType.TO);
					break;
				case cc : addresses = mimeMail.getMessage().getRecipients(javax.mail.internet.MimeMessage.RecipientType.CC);
					break;
				case all : addresses = mimeMail.getMessage().getAllRecipients();
					break;
				case bcc : addresses = mimeMail.getMessage().getRecipients(javax.mail.internet.MimeMessage.RecipientType.BCC);
					break;
				case ng : addresses = mimeMail.getMessage().getRecipients(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS);
					break;
			}
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}
		
		if(addresses!=null && addresses.length>0){
			
			for(Address address : addresses){
				if (address instanceof InternetAddress){
					try{
						recipients.add(((InternetAddress) address).getAddress().toLowerCase().trim());
					}catch (Exception e){log.warn(e.getMessage());}
				}
			}
			return recipients;
		}
		return recipients;
	}

	@Override
	public boolean hasRecipient(RecipientType type, String recipient) {
		Collection<String> recipientsInMail = getRecipients(type);
		if(recipientsInMail!=null){
			for(String recipientInMail : recipientsInMail){
				if(recipientInMail.equalsIgnoreCase(recipient)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addRecipient(RecipientType type, String recipient) {
		try {
			switch(type){
				case to : mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.TO, new InternetAddress(recipient, false)); 
					break;
				case cc : mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.CC, new InternetAddress(recipient, false));
					break;
				case all : 	mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.TO, new InternetAddress(recipient, false));
							mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.CC, new InternetAddress(recipient, false));
							mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.BCC, new InternetAddress(recipient, false));
							mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS, new InternetAddress(recipient, false));
					break;
				case bcc : mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.BCC, new InternetAddress(recipient, false));
					break;
				case ng : mimeMail.getMessage().addRecipient(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS, new InternetAddress(recipient, false));
					break;
			}
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}		
	}

	@Override
	public boolean removeRecipient(RecipientType type, String recipient) {
		boolean result = false;
		switch (type) {
		case to: result = removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.TO,recipient);
			break;
		case cc: result = removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.CC,recipient);
			break;
		case bcc: result = removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.BCC,recipient);
			break;
		case ng: result = removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS,recipient);
			break;
		case all: result = removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.TO,recipient);
				result = result || removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.CC,recipient);
				result = result || removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.BCC,recipient);
				result = result || removeUsingAddress(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS,recipient);			
			break;			
		}
		return result;
	}
	
	private boolean removeUsingAddress(javax.mail.Message.RecipientType type, String recipient){
		boolean removed = false;
		InternetAddress[] nullValue= null;
		try{
			InternetAddress[] addresses = (InternetAddress[])mimeMail.getMessage().getRecipients(type);
			if(addresses!=null){
				List<InternetAddress> addressesList = new ArrayList<InternetAddress>();
				addressesList.addAll(Arrays.asList(addresses));	
				for(InternetAddress address : addresses){
					if(address.getAddress().equalsIgnoreCase(recipient)){
						removed=addressesList.remove(address);
						break;
					}
				}
				if(removed){
					if(addressesList.size()>0){
						mimeMail.getMessage().setRecipients(type, addressesList.toArray(new InternetAddress[addressesList.size()]));
					}else{
						mimeMail.getMessage().setRecipients(type, nullValue);
					}
				}
			}
		}catch(Exception e){
			log.warn("error removing recipient "+recipient,e);
			return false;
		}
		return removed;
	}

	@Override
	public void removeAll(RecipientType type) {
		Address[] nullAddress = null;		
		try {
			switch(type){
				case to : mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, nullAddress); 
					break;
				case cc : mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.CC, nullAddress);
					break;
				case all : 	mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, nullAddress);
							mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.CC, nullAddress);
							mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.BCC, nullAddress);
							mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS, nullAddress);
					break;
				case bcc : mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.BCC, nullAddress);
					break;
				case ng : mimeMail.getMessage().setRecipients(javax.mail.internet.MimeMessage.RecipientType.NEWSGROUPS, nullAddress);
					break;
			}
		} catch (MessagingException e) {
			log.warn(e.getMessage());
		}
	}

}
