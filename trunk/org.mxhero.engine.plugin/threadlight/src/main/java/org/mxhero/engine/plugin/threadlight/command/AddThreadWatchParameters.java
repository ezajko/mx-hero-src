package org.mxhero.engine.plugin.threadlight.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

public class AddThreadWatchParameters extends NamedParameters{

	public static final String FOLLOWER = "follower";
	public static final String PARAMETERS = "parameters";

	public AddThreadWatchParameters(){
	}
	
	public AddThreadWatchParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	public String getFollower() {
		return get(FOLLOWER);
	}

	public void setFollower(String follower) {
		put(FOLLOWER,follower);
	}

	public String getParameters() {
		return get(PARAMETERS);
	}

	public void setParameters(String parameters) {
		put(PARAMETERS,parameters);
	}
	
}
