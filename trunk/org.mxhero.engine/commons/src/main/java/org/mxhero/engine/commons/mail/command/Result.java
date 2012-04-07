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
	
	/**
	 * @return
	 */
	public boolean isConditionTrue() {
		return conditionTrue;
	}

	/**
	 * @param conditionTrue
	 */
	public void setConditionTrue(boolean conditionTrue) {
		this.conditionTrue = conditionTrue;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public boolean isAnError() {
		return anError;
	}

	/**
	 * @param anError
	 */
	public void setAnError(boolean anError) {
		this.anError = anError;
	}

	/**
	 * @return
	 */
	public NamedParameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 */
	public void setParameters(NamedParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [conditionTrue=").append(conditionTrue)
				.append(", message=").append(message).append(", anError=")
				.append(anError).append("]");
		return builder.toString();
	}

}
