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

package org.mxhero.engine.plugin.clamd.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class ClamavScanParameters extends NamedParameters{

	public static final String REMOVE_INFECTED = "removeInfected";
	public static final String ADD_HEADER = "addHeader";
	public static final String HEADER_NAME = "headerName";

	/**
	 * 
	 */
	public ClamavScanParameters(){
	}
	
	/**
	 * @param parameters
	 */
	public ClamavScanParameters(NamedParameters parameters){
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public Boolean getRemoveInfected() {
		return get(REMOVE_INFECTED);
	}

	/**
	 * @param removeInfected
	 */
	public void setRemoveInfected(Boolean removeInfected) {
		put(REMOVE_INFECTED, removeInfected);
	}

	/**
	 * @return
	 */
	public Boolean getAddHeader() {
		return get(ADD_HEADER);
	}

	/**
	 * @param addHeader
	 */
	public void setAddHeader(Boolean addHeader) {
		put(ADD_HEADER, addHeader);
	}

	/**
	 * @return
	 */
	public String getHeaderName() {
		return get(HEADER_NAME);
	}

	/**
	 * @param headerName
	 */
	public void setHeaderName(String headerName) {
		put(HEADER_NAME, headerName);
	}

}
