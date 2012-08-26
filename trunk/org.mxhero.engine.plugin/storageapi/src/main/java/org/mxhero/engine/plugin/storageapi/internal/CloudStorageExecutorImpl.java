package org.mxhero.engine.plugin.storageapi.internal;

import java.util.HashMap;
import java.util.Map;

import org.mxhero.engine.plugin.storageapi.CloudStorage;
import org.mxhero.engine.plugin.storageapi.CloudStorageExecutor;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.mxhero.engine.plugin.storageapi.UserResultMessage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class CloudStorageExecutorImpl.
 */
public class CloudStorageExecutorImpl implements CloudStorageExecutor {
	
	/** The ctx. */
	@Autowired
	private BundleContext ctx;

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.storageapi.CloudStorageExecutor#execute(java.util.Map, java.lang.String)
	 */
	@Override
	public Map<UserResulType, UserResult> execute(Map<String, Object> params,
			String storageId) {
		Map<UserResulType, UserResult> processResult;
		ServiceReference serviceReference = ctx.getServiceReference(storageId);
		if(serviceReference != null){
			CloudStorage storage = (CloudStorage) ctx.getService(serviceReference);
			processResult = storage.process(params);
			ctx.ungetService(serviceReference);
		}else{
			processResult = new HashMap<UserResulType, UserResult>();
			processResult.put(UserResulType.ERROR, new UserResult(false, UserResultMessage.ERROR));
		}
		return processResult;
	}

}
