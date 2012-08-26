package org.mxhero.engine.plugin.storageapi;

import java.util.Map;

/**
 * The Interface CloudStorageExecutor.
 */
public interface CloudStorageExecutor {
	
	/**
	 * Execute.
	 *
	 * @param params the params
	 * @param storageId the storage id
	 * @return the map
	 */
	public Map<UserResulType, UserResult>  execute(Map<String, Object> params, String storageId);

}
