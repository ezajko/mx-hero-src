package org.mxhero.engine.plugin.statistics.internal.entity;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create entities from mails.
 * @author mmarmol
 */
public final class Utils {

	private static final char DIV_CHAR = '@';
	
	private static Logger log = LoggerFactory.getLogger(Utils.class);
	
	
	/**
	 * private so it can be instantiated
	 */
	private Utils(){
	}

	/**
	 * Creates a record from a mail
	 * @param mail 
	 * @return
	 */
	public static Record createRecord(MimeMail mail){
		Record record = new Record();
		RecordPk pk = new RecordPk();
		pk.setSequence(mail.getSequence());
		pk.setInsertDate(mail.getTime());
		record.setId(pk);
		record.setPhase(mail.getPhase());
		record.setState(mail.getStatus());
		record.setStateReason(mail.getStatusReason());
		record.setSender(mail.getInitialSender());
		record.setRecipient(mail.getRecipient());
		record.setBccRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.BCC));
		record.setCcRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.CC));
		record.setToRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.TO));
		record.setNgRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.NEWSGROUPS));
		record.setParentInsertDate(mail.getParentTime());
		record.setParentSequence(mail.getParentSequence());
		if (mail.getSenderId()!=null){
			record.setSenderId(mail.getSenderId());
		} else {
			record.setSenderId(mail.getInitialSender());
		}
		if(mail.getSenderDomainId()!=null){
			record.setSenderDomainId(mail.getSenderDomainId());
		} else {
			if (record.getSenderId()!=null){
				record.setSenderDomainId(record.getSenderId().substring(record.getSenderId().indexOf(DIV_CHAR) + 1));
			} else if (mail.getInitialSender()!=null){
				record.setSenderDomainId(mail.getInitialSender().substring(mail.getInitialSender().indexOf(DIV_CHAR) + 1));
			}
		}

		if (mail.getRecipientId()!=null){
			record.setRecipientId(mail.getRecipientId());
		} else {
			record.setRecipientId(mail.getRecipient());
		}
		if(mail.getRecipientDomainId()!=null){
			record.setRecipientDomainId(mail.getRecipientDomainId());
		} else {
			if (record.getRecipientId()!=null){
				record.setRecipientDomainId(record.getRecipientId().substring(record.getRecipientId().indexOf(DIV_CHAR) + 1));
			} else if (mail.getRecipient()!=null){
				record.setRecipientDomainId(mail.getRecipient().substring(mail.getRecipient().indexOf(DIV_CHAR) + 1));
			}
		}
		record.setFlow(mail.getFlow());
		
		try {
			record.setMessageId(mail.getMessage().getMessageID());
		} catch (MessagingException e1) {
			log.error("could not read message id from email");
			record.setMessageId("");
		}
		
		try {
			Address[] froms = mail.getMessage().getFrom();
			if(froms!=null & froms.length>0){
				record.setFrom(((InternetAddress)froms[0]).getAddress());
			}
		} catch (MessagingException e) {
			log.error("could not read From from email");
			record.setFrom("");
		}
		record.setSender(mail.getInitialSender());

		try {
			record.setSentDate(mail.getMessage().getSentDate());
		} catch (MessagingException e) {
			log.error("could not read SentDate id from email");
			record.setSentDate(null);
		}
		try {
			record.setSubject(mail.getMessage().getSubject());
		} catch (MessagingException e) {
			log.error("could not read Subject id from email");
			record.setSubject(null);
		}
		record.setBytesSize(mail.getInitialSize());
		return record;
	}
	
	/**
	 * Creates a stat using the mail info, the key and value of the stat.
	 * @param mail
	 * @param key
	 * @param value
	 * @return
	 */
	public static Stat createStat(MimeMail mail, String key, String value){
		Stat stat = new Stat();
		StatPk pk = new StatPk();
		pk.setKey(key);
		stat.setId(pk);
		stat.setValue(value);
		return stat;
	}
	
	/**
	 * Private method used to transform recipients array to strings. 
	 * @param message
	 * @param type
	 * @return
	 */
	private static String getRecipientsByTypeString(MimeMessage message, javax.mail.Message.RecipientType type){
		try {
			Address[] addresses = message.getRecipients(type);
			return ((addresses!=null && addresses.length>0)?Arrays.toString(addresses):"").replace("[","").replace("]","");
		} catch (MessagingException e) {
			return "";
		}
	}
}
