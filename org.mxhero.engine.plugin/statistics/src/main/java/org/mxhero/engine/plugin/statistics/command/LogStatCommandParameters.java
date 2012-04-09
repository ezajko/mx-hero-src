package org.mxhero.engine.plugin.statistics.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class LogStatCommandParameters extends NamedParameters {

	public static final String KEY = "key";
	public static final String VALUE = "value";

	/**
	 * 
	 */
	public LogStatCommandParameters(){
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public LogStatCommandParameters(String key, String value){
		this.setKey(key);
		this.setValue(value);
	}
	
	/**
	 * @param parameters
	 */
	public LogStatCommandParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getKey() {
		return get(KEY);
	}

	/**
	 * @param key
	 */
	public void setKey(String key) {
		put(KEY, key);
	}

	/**
	 * @return
	 */
	public String getValue() {
		return get(VALUE);
	}

	/**
	 * @param value
	 */
	public void setValue(String value) {
		put(VALUE, value);
	}

}
