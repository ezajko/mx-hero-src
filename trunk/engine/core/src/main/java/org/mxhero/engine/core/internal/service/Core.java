package org.mxhero.engine.core.internal.service;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to hold all the properties that are used in this module.
 * @author mmarmol
 */
public final class Core extends PropertiesService{

	public static final String SCAN_INTERVAL = "updatable.resource.scanner.interval";
	public static final String ERROR_SUFFIX = "updatable.error.suffix";
	public static final String ERROR_PREFIX = "updatable.error.prefix";
	public static final String ERROR_DIRECTORY = "updatable.error.folder";
	public static final String SENDPOOL_COREPOOLSIZE = "updatable.sendpool.corepoolsize";
	public static final String SENDPOOL_MAXIMUMPOOLSIZE = "updatable.sendpool.maximumpoolsize";
	public static final String SENDPOOL_KEEPALIVETIME = "updatable.sendpool.keepalivetime";
	public static final String SENDPOOL_QUEUE_WAIT_TIME = "updatable.sendpool.queue.wait.time";
	public static final String RECEIVEPOOL_COREPOOLSIZE = "updatable.receivepool.corepoolsize";
	public static final String RECEIVEPOOL_MAXIMUMPOOLSIZE = "updatable.receivepool.maximumpoolsize";
	public static final String RECEIVEPOOL_KEEPALIVETIME = "updatable.receivepool.keepalivetime";
	public static final String RECEIVEPOOL_QUEUE_WAIT_TIME = "updatable.receivepool.queue.wait.time";
	public static final String OUTPUTPOOL_COREPOOLSIZE = "updatable.outputpool.corepoolsize";
	public static final String OUTPUTPOOL_MAXIMUMPOOLSIZE = "updatable.outputpool.maximumpoolsize";
	public static final String OUTPUTPOOL_KEEPALIVETIME = "updatable.outputpool.keepalivetime";
	public static final String OUTPUTPOOL_QUEUE_WAIT_TIME = "updatable.outputpool.queue.wait.time";	
	public static final String GROUP_ID_TOP = "updatable.group.id.top";
	public static final String GROUP_ID_BOTTOM = "updatable.group.id.bottom";
	public static final String PROCESS_ERROR_STAT = "updatable.process.error.stat";
	public static final String CONNECTOR_ERROR_STAT = "updatable.connector.error.stat";
	public static final String CONNECTOR_NOT_FAUND_VALUE = "updatable.connector.not.found.value";
	public static final String QUEUE_DELAY_TIME = "updatable.queue.delay.time";
	private static Logger log = LoggerFactory.getLogger(Core.class);
	
	private static final String PROPERTIES_FILE = "core.properties";
	
	/**
	 * Create the object calling tu super and passing the default properties.
	 */
	public Core(){
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
			initialProperties.load(Core.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			log.error("Error while loading initial properties.",e);
		}
		return initialProperties;
	}
	
}
