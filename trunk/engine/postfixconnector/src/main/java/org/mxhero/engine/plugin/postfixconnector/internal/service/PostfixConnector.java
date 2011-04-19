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
	public static final String INPUTPOOL_COREPOOLSIZE = "updatable.inputpool.corepoolsize";
	public static final String INPUTPOOL_MAXIMUMPOOLSIZE = "updatable.inputpool.maximumpoolsize";
	public static final String INPUTPOOL_KEEPALIVETIME = "updatable.inputpool.keepalivetime";
	public static final String INPUTPOOL_QUEUE_WAIT_TIME = "updatable.inputpool.queue.wait.time";
	public static final String OUTPUTPOOL_COREPOOLSIZE = "updatable.outputpool.corepoolsize";
	public static final String OUTPUTPOOL_MAXIMUMPOOLSIZE = "updatable.outputpool.maximumpoolsize";
	public static final String OUTPUTPOOL_KEEPALIVETIME = "updatable.outputpool.keepalivetime";
	public static final String OUTPUTPOOL_QUEUE_WAIT_TIME = "updatable.outputpool.queue.wait.time";
	public static final String IN_TIME_STAT = "updatable.in.time.stat";
	public static final String OUT_TIME_STAT = "updatable.out.time.stat";
	public static final String CORE_ERROR_STAT = "updatable.core.error.stat";
	public static final String DELIVER_ERROR_STAT = "updatable.deliver.error.stat";
	public static final String STATS_TIME_FORMAT = "initial.stats.time.format";
	
	private static Logger log = LoggerFactory.getLogger(PostfixConnector.class);
	
	private static final String PROPERTIES_FILE = "postfixconnector.properties";
	
	/**
	 * Creates the instance using initial properties.
	 * @param properties
	 */
	public PostfixConnector(){
		super(getDefaults());
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
