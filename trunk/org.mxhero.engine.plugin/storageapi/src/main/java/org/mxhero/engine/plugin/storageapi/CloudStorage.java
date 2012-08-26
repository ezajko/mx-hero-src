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
	
	/**
	 * Store.
	 *
	 * @param email the email
	 * @param filePaths the file paths
	 * @return the storage result
	 */
	public StorageResult store(String email, String filePath);
}
