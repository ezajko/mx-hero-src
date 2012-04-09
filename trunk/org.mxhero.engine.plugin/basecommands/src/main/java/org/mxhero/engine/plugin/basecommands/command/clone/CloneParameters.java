package org.mxhero.engine.plugin.basecommands.command.clone;

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
	
	/**
	 * 
	 */
	public CloneParameters(){	
	}
	
	/**
	 * @param sender
	 * @param recipient
	 */
	public CloneParameters(String sender, String recipient){	
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
	
}