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

package org.mxhero.engine.plugin.statistics.internal.entity;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.commons.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create entities from mails.
 * @author mmarmol
 */
public final class Utils {

	private static final char DIV_CHAR = '@';
	
	private static Logger log = LoggerFactory.getLogger(Utils.class);
	
	private static String serverName = "MXHERO";
	
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
	public Record createRecord(MimeMail mail){
		Record record = new Record();
		record.setServerName(serverName);
		record.setSequence(mail.getSequence());
		record.setInsertDate(mail.getTime());
		record.setPhase(mail.getPhase().name());
		record.setState(mail.getStatus().name());
		record.setStateReason(mail.getStatusReason());
		record.setSender(mail.getSender());
		record.setRecipient(mail.getRecipient());
		record.setBccRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.BCC));
		record.setCcRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.CC));
		record.setToRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.TO));
		record.setNgRecipients(getRecipientsByTypeString(mail.getMessage(),RecipientType.NEWSGROUPS));

		if (mail.getSenderId()!=null){
			record.setSenderId(mail.getSenderId());
		} else {
			record.setSenderId(mail.getSender());
		}
		if(mail.getSenderDomainId()!=null){
			record.setSenderDomainId(mail.getSenderDomainId());
		} else {
			if (record.getSenderId()!=null){
				record.setSenderDomainId(record.getSenderId().substring(record.getSenderId().indexOf(DIV_CHAR) + 1));
			} else if (mail.getSender()!=null){
				record.setSenderDomainId(mail.getSender().substring(mail.getSender().indexOf(DIV_CHAR) + 1));
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
		record.setFlow(mail.getFlow().name());
		
		record.setMessageId(mail.getMessageId());
		
		try {
			Address[] froms = mail.getMessage().getFrom();
			if(froms!=null && froms.length>0){
				record.setFrom(((InternetAddress)froms[0]).getAddress());
			}
		} catch (MessagingException e) {
			log.debug("could not read From from email");
			record.setFrom("<>");
		}
		if(record.getFrom()==null || record.getFrom().trim().length()<1){
			record.setFrom(mail.getSender());
		}
		record.setSender(mail.getSender());

		try {
			record.setSubject(mail.getMessage().getSubject());
		} catch (MessagingException e) {
			log.debug("could not read Subject id from email");
			record.setSubject(null);
		}
		record.setBytesSize(mail.getInitialSize());
		record.setSenderGroup(mail.getSenderGroup());
		record.setRecipientGroup(mail.getRecipientGroup());
		return record;
	}
	
	/**
	 * Creates a stat using the mail info, the key and value of the stat.
	 * @param mail
	 * @param key
	 * @param value
	 * @return
	 */
	public Stat createStat(MimeMail mail, String key, String value){
		Stat stat = new Stat();
		stat.setServerName(serverName);
		stat.setKey(key);
		stat.setPhase(mail.getPhase().name());
		stat.setInsertDate(mail.getTime());
		stat.setSequence(mail.getSequence());
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

	public String getServerName() {
		return Utils.serverName;
	}

	public void setServerName(String serverName) {
		Utils.serverName = serverName;
	}

}
