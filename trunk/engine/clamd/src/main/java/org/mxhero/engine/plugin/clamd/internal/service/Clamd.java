package org.mxhero.engine.plugin.clamd.internal.service;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to hold all the properties of the module.
 * @author mmarmol
 */
public class Clamd extends PropertiesService{
	
	public static final String CONNECTION_TIMEOUT = "updatable.connection.timeout";
	public static final String RESPONSE_TIMEOUT = "updatable.response.timeout";
	public static final String HOSTNAME = "updatable.hostname";
	public static final String PORT = "updatable.port";
	public static final String BUFFER_SIZE = "updatable.buffer.size";
	public static final String VIRUS_HEADER = "updatable.virus.header";
	
	private static Logger log = LoggerFactory.getLogger(Clamd.class);
	
	private static final String PROPERTIES_FILE = "clamd.properties";

	/**
	 * basic constructor, takes defaults properties.
	 */
	public Clamd(){
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
			initialProperties.load(Clamd.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			log.error("Error while loading initial properties.",e);
		}
		return initialProperties;
	}
}
