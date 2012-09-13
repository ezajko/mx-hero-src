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

package org.mxhero.engine.plugin.threadlight.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class AddThreadWatchParameters extends NamedParameters{

	public static final String FOLLOWER = "follower";
	public static final String PARAMETERS = "parameters";
	public static final String SENDER_ID = "sender_id";
	public static final String RECIPIENT_ID = "recipient_id";
	public static final String SUBJECT = "subject";
	
	/**
	 * 
	 */
	public AddThreadWatchParameters(){
	}
	
	/**
	 * @param follower
	 * @param parameters
	 */
	public AddThreadWatchParameters(String follower, String parameters){
		this.setFollower(follower);
		this.setParameters(parameters);
	}
	
	/**
	 * @param parameters
	 */
	public AddThreadWatchParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getFollower() {
		return get(FOLLOWER);
	}

	/**
	 * @param follower
	 */
	public void setFollower(String follower) {
		put(FOLLOWER,follower);
	}

	/**
	 * @return
	 */
	public String getParameters() {
		return get(PARAMETERS);
	}

	/**
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		put(PARAMETERS,parameters);
	}
	
	/**
	 * @return
	 */
	public String getSenderId(){
		return get(SENDER_ID);
	}
	
	/**
	 * @param senderId
	 */
	public void setSenderId(String senderId){
		put(SENDER_ID,senderId);
	}
	
	/**
	 * @return
	 */
	public String getRecipientId(){
		return get(RECIPIENT_ID);
	}
	
	/**
	 * @param recipientId
	 */
	public void setRecipientId(String recipientId){
		put(RECIPIENT_ID, recipientId);
	}
	
	/**
	 * @return
	 */
	public String getSubject(){
		return get(SUBJECT);
	}
	
	/**
	 * @param subject
	 */
	public void setSubject(String subject){
		put(SUBJECT,subject);
	}
	
}
