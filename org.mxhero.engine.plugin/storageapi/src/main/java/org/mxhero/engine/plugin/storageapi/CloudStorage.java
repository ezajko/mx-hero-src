package org.mxhero.engine.plugin.storageapi;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface CloudStorage.
 */
public interface CloudStorage {
	
	/**
	 * Process.
	 *
	 * @param params the params
	 * @return the result
	 */
	public Map<UserResulType, UserResult> process(Map<String, Object> params);
}
