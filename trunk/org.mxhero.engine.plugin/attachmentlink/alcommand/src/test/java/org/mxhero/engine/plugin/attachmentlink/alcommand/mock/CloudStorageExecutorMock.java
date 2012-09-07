package org.mxhero.engine.plugin.attachmentlink.alcommand.mock;

import java.util.Map;

import org.mxhero.engine.plugin.storageapi.CloudStorageExecutor;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;

import com.google.common.collect.Maps;

public class CloudStorageExecutorMock implements CloudStorageExecutor {

	@Override
	public Map<UserResulType, UserResult> execute(Map<String, Object> params,
			String storageId) {
		return Maps.newHashMap();
	}

}
