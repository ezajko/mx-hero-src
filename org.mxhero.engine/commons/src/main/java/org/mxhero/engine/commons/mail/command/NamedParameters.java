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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mmarmol
 */
public class NamedParameters {

	protected final Map<String, Object> nameToInstance = new HashMap<String, Object>();

	/**
	 * 
	 */
	public NamedParameters(){
	}
	
	/**
	 * @param name
	 * @param parameter
	 */
	public NamedParameters(String name, Object parameter){
		nameToInstance.put(name, parameter);
	}
	
	/**
	 * Add parameter with the given name.
	 * @param <T> The type of the parameter.
	 * @param name The name of the parameter.
	 * @param parameter The parameter.
	 * @return This instance of NamedParameters (so that you can chain multiple
	 *         put calls).
	 */
	public <T> NamedParameters put(String name, T parameter) {
		this.nameToInstance.put(name, parameter);
		return this;
	}

	/**
	 * Get parameter with the given name and type. Returns null if a parameter
	 * with the name exists but has a different type.
	 * @param <T> The type of the parameter.
	 * @param name The name of the parameter.
	 * @return The parameter with the given name or null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		try {
			return (T) this.nameToInstance.get(name);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	/**
	 * @return
	 */
	public int getSize(){
		return nameToInstance.size();
	}
	
	/**
	 * @param name
	 * @return
	 */
	public boolean hasParameter(String name){
		return nameToInstance.containsKey(name);
	}

	/**
	 * @return
	 */
	public Map<String, Object> getNameToInstance() {
		return nameToInstance;
	}

}
