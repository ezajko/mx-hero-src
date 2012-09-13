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
