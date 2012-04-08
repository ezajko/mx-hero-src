package org.mxhero.engine.plugin.statistics.command;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class LogStatParameters extends NamedParameters {

	public static final String KEY = "key";
	public static final String VALUE = "value";

	/**
	 * 
	 */
	public LogStatParameters(){
	}
	
	/**
	 * @param parameters
	 */
	public LogStatParameters(NamedParameters parameters){
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
