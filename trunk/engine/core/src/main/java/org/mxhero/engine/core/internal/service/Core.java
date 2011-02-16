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

	public static final String DROOLS_CHANGESET_PATH = "updatable.drools.changeset.path";
	public static final String DROOLS_SCAN_INTERVAL = "updatable.drools.resource.scanner.interval";
	public static final String DROOLS_RESOURCES_BACKUPDIR_BASE = "updatable.drools.resources.backupdir.base";
	public static final String INPUTPOOL_COREPOOLSIZE = "updatable.inputpool.corepoolsize";
	public static final String INPUTPOOL_MAXIMUMPOOLSIZE = "updatable.inputpool.maximumpoolsize";
	public static final String INPUTPOOL_KEEPALIVETIME = "updatable.inputpool.keepalivetime";
	public static final String INPUTPOOL_QUEUE_WAIT_TIME = "updatable.inputpool.queue.wait.time";
	public static final String OUTPUTPOOL_COREPOOLSIZE = "updatable.outputpool.corepoolsize";
	public static final String OUTPUTPOOL_MAXIMUMPOOLSIZE = "updatable.outputpool.maximumpoolsize";
	public static final String OUTPUTPOOL_KEEPALIVETIME = "updatable.outputpool.keepalivetime";
	public static final String OUTPUTPOOL_QUEUE_WAIT_TIME = "updatable.outputpool.queue.wait.time";	
	public static final String GROUP_ID_TOP = "updatable.group.id.top";
	public static final String GROUP_ID_BOTTOM = "updatable.group.id.bottom";
	public static final String PROCESS_ERROR_STAT = "updatable.process.error.stat";
	public static final String CONNECTOR_ERROR_STAT = "updatable.connector.error.stat";
	public static final String IN_TIME_STAT = "updatable.in.time.stat";
	public static final String OUT_TIME_STAT = "updatable.out.time.stat";
	public static final String STATS_TIME_FORMAT = "initial.stats.time.format";
	public static final String CONNECTOR_NOT_FAUND_VALUE = "updatable.connector.not.found.value";
	
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
