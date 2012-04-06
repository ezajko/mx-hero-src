package org.mxhero.engine.commons.mail.command;

import java.util.HashMap;
import java.util.Map;

public class NamedParameters {

	private final Map<String, Object> nameToInstance = new HashMap<String, Object>();

	public NamedParameters(){
	}
	
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
	
	public int getSize(){
		return nameToInstance.size();
	}
	
	public boolean hasParameter(String name){
		return nameToInstance.containsKey(name);
	}

}
