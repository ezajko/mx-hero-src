package org.mxhero.engine.plugin.threadlight.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class AddThreadWatchParameters extends NamedParameters{

	public static final String FOLLOWER = "follower";
	public static final String PARAMETERS = "parameters";

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
	
}
