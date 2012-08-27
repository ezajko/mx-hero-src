package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import java.util.Map;

import org.mxhero.engine.plugin.storageapi.CloudStorage;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;

public class CloudStorageMock implements CloudStorage {

	@Override
	public Map<UserResulType, UserResult> process(Map<String, Object> params) {
		return null;
	}

	@Override
	public StorageResult store(String email, String filePath) {
		return new StorageResult(true);
	}

}
