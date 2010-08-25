package org.mxhero.engine.plugin.xmlfinder.internal.service;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This service holds all the properties from this module.
 * @author mmarmol
 */
public final class XMLFinder extends PropertiesService{

	public static final String DOMAINS_FILE_PATH = "updatable.domains.file.path";
	public static final String LOADER_CHECK_TIME = "updatable.loader.check.time";
	public static final String LOADER_LOAD_TIME = "updatable.loader.load.time";
	
	private static Logger log = LoggerFactory.getLogger(XMLFinder.class);
	
	private static final String PROPERTIES_FILE = "xmlfinder.properties";


	/**
	 * Call super and pass default properties.
	 */
	public XMLFinder(){
		super(getDefaults());
	}

	
	/**
	 * Returns the properties that are loaded from the classpath.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Dictionary getDefaults(){
		Properties initialProperties = new Properties(); 
		try {
			initialProperties.load(XMLFinder.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			log.error("Error while loading initial properties.",e);
		}
		return initialProperties;
	}

}
