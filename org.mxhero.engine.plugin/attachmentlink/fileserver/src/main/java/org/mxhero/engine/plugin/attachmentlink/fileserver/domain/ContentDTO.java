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

package org.mxhero.engine.plugin.attachmentlink.fileserver.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * @author royojp
 * 
 */
@SuppressWarnings("serial")
public class ContentDTO implements Serializable {
	
	private static final String DOWNLOAD = "1";

	private String path;
	private String contentType;
	private String fileName;
	private Integer length;
	private Long idMessageAttach;
	private Long idMessage;
	private String senderMail;
	private String recipientMail;
	private String msgMail;
	private String msgMailHtml;
	private boolean processMsg;
	private boolean accessed;
	private String messageId;
	private String subject;
	private String publicUrl;

	private InputStream in;

	/**
	 * 
	 */
	public ContentDTO() {
	}
	
	public Long getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(Long idMessage) {
		this.idMessage = idMessage;
	}

	public Long getIdMessageAttach() {
		return idMessageAttach;
	}

	public void setIdMessageAttach(Long idMessageAttach) {
		this.idMessageAttach = idMessageAttach;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getContentType(String type) {
		if(DOWNLOAD.equals(type)){
			return "application/force-download"; 
		}else{
			return getContentType();
		}
	}
	
	public boolean hasToBeOpen(String type){
		return !DOWNLOAD.equals(type);
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getInputStream() throws FileNotFoundException {
		if (in == null) {
			in = new FileInputStream(getPath());
		}
		return in;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean wasAccessed() {
		return isAccessed();
	}

	public void setAccessed(boolean accessed) {
		this.accessed = accessed;
	}

	public boolean isAccessed() {
		return accessed;
	}

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}

	public void setMsgMail(String msgMail) {
		this.msgMail = msgMail;
	}

	public String getMsgMail() {
		return msgMail;
	}

	public String getMsgMailHtml() {
		return msgMailHtml;
	}

	public void setMsgMailHtml(String msgMailHtml) {
		this.msgMailHtml = msgMailHtml;
	}

	public void setProcessMsg(boolean processMsg) {
		this.processMsg = processMsg;
	}

	public boolean isProcessMsg() {
		return processMsg;
	}

	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

	public String getRecipientMail() {
		return recipientMail;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPublicUrl(String url) {
		this.publicUrl = url;
	}
	
	public String getPublicUrl() {
		return publicUrl;
	}

	public boolean hasPublicUrl() {
		return !StringUtils.isEmpty(publicUrl);
	}

}
