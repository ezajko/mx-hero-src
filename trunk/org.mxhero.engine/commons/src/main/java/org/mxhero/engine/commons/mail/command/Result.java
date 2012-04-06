package org.mxhero.engine.commons.mail.command;

/**
 * Represents the result of a command and holds some properties to help describe what happened.
 * @author mmarmol
 */
public class Result {

	private boolean conditionTrue = false;
	
	private String message;
	
	private boolean anError = false;
	
	private NamedParameters parameters;
	
	public boolean isConditionTrue() {
		return conditionTrue;
	}

	public void setConditionTrue(boolean conditionTrue) {
		this.conditionTrue = conditionTrue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAnError() {
		return anError;
	}

	public void setAnError(boolean anError) {
		this.anError = anError;
	}

	public NamedParameters getParameters() {
		return parameters;
	}

	public void setParameters(NamedParameters parameters) {
		this.parameters = parameters;
	}


}
