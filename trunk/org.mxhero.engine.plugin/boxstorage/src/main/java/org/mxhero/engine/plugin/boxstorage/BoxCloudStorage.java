package org.mxhero.engine.plugin.boxstorage;

import java.util.Map;

import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;

/**
 * The Interface BoxCloudStorage.
 */
public interface BoxCloudStorage {

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.storageapi.CloudStorage#process(org.mxhero.engine.commons.mail.command.NamedParameters)
	 */
	/**
	 * Process.
	 *
	 * @param params the params
	 * @return the map
	 */
	public Map<UserResulType, UserResult> process(Map<String, Object> params);

}