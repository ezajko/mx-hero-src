/*
 * 
 */
package org.mxhero.engine.plugin.boxstorage.internal.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.boxstorage.BoxCloudStorage;
import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.ApplicationService;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.mxhero.engine.plugin.storageapi.CloudStorage;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.mxhero.engine.plugin.storageapi.UserResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * The Class BoxCloudStorageClient.
 */
public class BoxCloudStorageClient implements BeanFactoryAware, CloudStorage, BoxCloudStorage{
	
	/** The bean factory. */
	private BeanFactory beanFactory;
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(BoxCloudStorageClient.class);
	
	/**
	 * Inits the.
	 */
	public void init(){
		try {
			initAppKey();
			initApiBoxKey();
		} catch (Exception e) {
			String errorMsg = "Could not init module. Error authenticating with mxhero Box Server";
			logger.error(errorMsg);
			logger.error("Error message {}", e.getMessage());
			logger.error("Error class {}", e.getClass().getName());
			throw new RuntimeException(errorMsg);
		}
	}
	
	/**
	 * Inits the api box key.
	 */
	private void initApiBoxKey() {
		getApplicationService().initApiBoxKey();
	}

	/**
	 * Inits the app key.
	 */
	private void initAppKey() {
		getApplicationService().initAppKey();
	}

	/**
	 * Gets the user box instance.
	 *
	 * @param email the email
	 * @return the user box instance
	 */
	private UserBoxClient getUserBoxInstance(String email) {
		return (UserBoxClient) this.beanFactory.getBean("userBox", email);
	}

	/**
	 * Gets the user box instance.
	 *
	 * @param request the email
	 * @return the user box instance
	 */
	private UserBoxClient getUserBoxInstance(UserRequest request){
		return (UserBoxClient) this.beanFactory.getBean("userBox", request);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.storageapi.CloudStorage#process(org.mxhero.engine.commons.mail.command.NamedParameters)
	 */
	@Override
	public Map<UserResulType, UserResult> process(Map<String, Object> params) {
		Map<UserResulType, UserResult> result = new HashMap<UserResulType, UserResult>();
		String emailSender = (String) params.get("senderStorage");
		if(!StringUtils.isEmpty(emailSender)){
			processEmail(emailSender, true, result);
		}
		String recipientEmail = (String) params.get("recipientStorage");
		if(!StringUtils.isEmpty(recipientEmail)){
			processEmail(recipientEmail, false, result);
		}
		return result;
	}

	/**
	 * Process email.
	 *
	 * @param email the email
	 * @param sender the sender
	 * @param result the result
	 */
	private void processEmail(String email, boolean sender, Map<UserResulType, UserResult> result) {
		try {
			UserRequest request = new UserRequest(email,sender);
			UserBoxClient boxInstance = getUserBoxInstance(request);
			UserResult userResult = boxInstance.process();
			result.put(userResult.getResultType(), userResult);
		} catch (Exception e) {
			logger.error("Error message: {}", e.getMessage());
			logger.error("Error class: {}", e.getClass());
			result.put(UserResulType.ERROR, new UserResult(false, UserResultMessage.ERROR));
		}
	}
	
	/**
	 * Register token.
	 *
	 * @param email the email
	 * @param token the token
	 */
	public void registerToken(String email, String token) {
		UserBoxClient userBoxInstance = getUserBoxInstance(email);
		userBoxInstance.registerToken(token);
	}


	/**
	 * Gets the application service.
	 *
	 * @return the application service
	 */
	private ApplicationService getApplicationService(){
		return this.beanFactory.getBean(ApplicationService.class);
	}

	/**
	 * Store.
	 *
	 * @param tx the tx
	 * @return the storage result
	 */
	public StorageResult store(TransactionAttachment tx) {
		UserBoxClient user = getUserBoxInstance(tx.getEmail());
		StorageResult result = user.store(tx);
		return result;
	}
}
