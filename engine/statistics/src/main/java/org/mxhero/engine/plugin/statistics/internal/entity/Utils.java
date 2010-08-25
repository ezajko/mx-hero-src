package org.mxhero.engine.plugin.statistics.internal.entity;

import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * Utility class to create entities from mails.
 * @author mmarmol
 */
public final class Utils {

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
		record.setRecipients(Arrays.toString(mail.getRecipients().toArray()));
		record.setBccRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.BCC));
		record.setCcRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.CC));
		record.setToRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.TO));
		record.setNgRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.NEWSGROUPS));

		try {
			record.setMessageId(mail.getMessage().getMessageID());
		} catch (MessagingException e1) {
			record.setMessageId("");
		}
		
		try {
			record.setFrom(Arrays.toString(mail.getMessage().getFrom()));
		} catch (MessagingException e) {
			record.setFrom("");
		}
		record.setSender(mail.getInitialSender());
		try {
			record.setReceivedDate(mail.getMessage().getReceivedDate());
		} catch (MessagingException e) {
			record.setReceivedDate(null);
		}
		try {
			record.setSentDate(mail.getMessage().getSentDate());
		} catch (MessagingException e) {
			record.setSentDate(null);
		}
		try {
			record.setSubject(mail.getMessage().getSubject());
		} catch (MessagingException e) {
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
			return Arrays.toString(message.getRecipients(type));
		} catch (MessagingException e) {
			return "";
		}
	}
}
