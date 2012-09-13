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

package org.mxhero.engine.plugin.basecommands.command.clone;

import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class CloneParameters extends NamedParameters{

	public static final String SENDER = "sender";
	public static final String RECIPIENT = "recipient";
	public static final String OUTPUT_SERVICE = "outputService";
	public static final String GENERATE_ID = "generateId";
	public static final String OVERRIDE = "override";
	public static final String PHASE = "phase";
	
	/**
	 * 
	 */
	public CloneParameters(){	
		super();
	}
	
	/**
	 * @param sender
	 * @param recipient
	 */
	public CloneParameters(String sender, String recipient){	
		super();
		this.setSender(sender);
		this.setRecipient(recipient);
	}
	
	/**
	 * @param parameters
	 */
	public CloneParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getSender() {
		return get(SENDER);
	}
	
	/**
	 * @param sender
	 */
	public void setSender(String sender){
		put(SENDER,sender);
	}

	/**
	 * @return
	 */
	public String getRecipient() {
		return get(RECIPIENT);
	}
	
	/**
	 * @param recipient
	 */
	public void setRecipient(String recipient){
		put(RECIPIENT,recipient);
	}

	/**
	 * @return
	 */
	public String getOutputService() {
		return get(OUTPUT_SERVICE);
	}
	
	/**
	 * @param outputService
	 */
	public void setOutputService(String outputService){
		put(OUTPUT_SERVICE,outputService);
	}

	/**
	 * @return
	 */
	public Boolean getGenerateId() {
		return get(GENERATE_ID);
	}
	
	/**
	 * @param generateId
	 */
	public void setGenerateId(Boolean generateId){
		put(GENERATE_ID,generateId);
	}

	/**
	 * @return
	 */
	public String getOverride() {
		return get(OVERRIDE);
	}
	
	/**
	 * @param override
	 */
	public void setOverride(String override){
		put(OVERRIDE,override);
	}

	/**
	 * @return
	 */
	public Mail.Phase getPhase(){
		return get(PHASE);
	}
	
	/**
	 * @param phase
	 */
	public void setPhase(Mail.Phase phase){
		put(PHASE,phase);
	}
	
}
