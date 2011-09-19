package org.mxhero.engine.plugin.postfixconnector.internal.service;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 * Implementation of the PropertiesService to hold properties of this plugin.
 */
public final class PostfixConnector extends PropertiesService{

	public static final String SMTP_HOST_NAME = "initial.mail.smtp.host";
	public static final String SMTP_HOST_PORT = "initial.mail.smtp.port";
	public static final String SMTP_HOST = "initial.smtp.host";
	public static final String SMTP_PORT = "initial.smtp.port";
	public static final String DEFERRED_SIZE = "initial.deferred.size";
	public static final String RECEIVE_BUFFER_SIZE = "initial.receive.buffer.size";
	public static final String MESSAGE_MAX_SIZE = "initial.message.max.size";
	public static final String ERROR_SUFFIX = "updatable.error.suffix";
	public static final String ERROR_PREFIX = "updatable.error.prefix";
	public static final String ERROR_DIRECTORY = "updatable.error.folder";
	public static final String CORE_ERROR_STAT = "updatable.core.error.stat";
	public static final String DELIVER_ERROR_STAT = "updatable.deliver.error.stat";
	public static final String MAX_CONNECTIONS = "initial.max.connections";
	public static final String CONNECTION_TIMEOUT = "initial.connection.timeout";
	public static final String HOSTNAME = "initial.hostname";
	public static final String CHARSET = "initial.charset";
	public static final String MAX_RECIPIENTS = "initial.max.recipients";
	
	private static PostfixConnector lastInstance;
	
	private static Logger log = LoggerFactory.getLogger(PostfixConnector.class);
	
	private static final String PROPERTIES_FILE = "postfixconnector.properties";
	
	/**
	 * Creates the instance using initial properties.
	 * @param properties
	 */
	public PostfixConnector(){
		super(getDefaults());
		PostfixConnector.lastInstance=this;
	}
	
	public static String getCurrentInstanceValue(String key){
		if(lastInstance!=null){
			return lastInstance.getValue(key);
		}
		return null;
	}
	
	/**
	 * Returns the properties that are loaded from the classpath.
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public static Dictionary getDefaults(){
		Properties initialProperties = new Properties(); 
		try {
			initialProperties.load(PostfixConnector.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			log.error("Error while loading initial properties.",e);
		}
		return initialProperties;
	}
}
