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

package org.mxhero.engine.commons.mail.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;

/**
 * Represents data of the mail so it can be used inside rules.
 * @author mmarmol
 */
public interface Mail {
	
	/**
	 * Current state of the mail object.
	 * @author mxhero
	 */
	public enum Status {
		deliver,drop,requeue,redirect;
	}
	
	/**
	 * Current phase of the email been processed by the engine.
	 * @author mxhero
	 */
	public enum Phase {
		send,receive,out;
	}
	
	/**
	 * 
	 * @author mxhero
	 */
	public enum Flow {
		in,out,both,none;
	}
	
	/**
	 * @return
	 */
	public String getId();
	
	/**
	 * @param commandServiceId
	 * @param params
	 * @return
	 */
	public Result cmd(String commandServiceId, NamedParameters params);
	
	/**
	 * @return
	 */
	public Phase getPhase();
	
	/**
	 * @return
	 */
	public Status getStatus();
	
	/**
	 * @return
	 */
	public Flow getFlow();

	/**
	 * @return
	 */
	public String getStatusReason();
	
	/**
	 * @return
	 */
	public long getInitialSize();
	
	
	/**
	 * @return
	 */
	public long getSize();
	
	/**
	 * @return
	 */
	public User getSender();
	
	/**
	 * @return
	 */
	public User getFromSender();
	
	/**
	 * @return
	 */
	public User getRecipient();
	
	/**
	 * @return
	 */
	public Date getSentDate();
	
	/**
	 * @return
	 */
	public List<User> getRecipientsInHeaders();

	/**
	 * @return
	 */
	public Map<String, String> getProperties();

	/**
	 * @param reason
	 * @return
	 */
	public abstract boolean drop(String reason);
	
	/**
	 * @param reason
	 * @return
	 */
	public abstract boolean redirect(String reason);

	/**
	 * @return
	 */
	public Headers getHeaders();

	/**
	 * @return
	 */
	public abstract String getSubject();

	/**
	 * @param subject
	 */
	public abstract void setSubject(String subject);

	/**
	 * @return
	 */
	public Recipients getRecipients();

	/**
	 * @return
	 */
	public Body getBody();

	/**
	 * @return
	 */
	public Attachments getAttachments();
	
	public Long getForcedPhasePriority();
	
}
