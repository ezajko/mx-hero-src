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

package org.mxhero.engine.plugin.threadlight.vo;

import java.io.Serializable;

public class ThreadRowPk implements Serializable{

	private static final long serialVersionUID = 2992486699615639710L;
	
	private String messageId;
	private String senderMail;
	private String recipientMail;
	
	public ThreadRowPk(){
	}
	
	public ThreadRowPk(String messageId, String senderMail, String recipientMail) {
		this.messageId = messageId;
		this.senderMail = senderMail;
		this.recipientMail = recipientMail;
	}

	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public String getSenderMail() {
		return senderMail;
	}
	
	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}
	
	public String getRecipientMail() {
		return recipientMail;
	}
	
	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageId == null) ? 0 : messageId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThreadRowPk other = (ThreadRowPk) obj;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (recipientMail == null) {
			if (other.recipientMail != null)
				return false;
		} else if (!recipientMail.equals(other.recipientMail))
			return false;
		if (senderMail == null) {
			if (other.senderMail != null)
				return false;
		} else if (!senderMail.equals(other.senderMail))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRowPk [messageId=").append(messageId)
				.append(", senderMail=").append(senderMail)
				.append(", recipientMail=").append(recipientMail).append("]");
		return builder.toString();
	}
	
}
