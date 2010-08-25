package org.mxhero.engine.plugin.spamd.internal.service;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to manage al the properties of this module.
 * @author mmarmol
 */
public class Spamd extends PropertiesService {

	public static final String HOSTNAME = "updatable.hostname";
	public static final String PORT = "updatable.port";
    public static final String STATUS_MAIL_ATTRIBUTE_NAME = "updatable.status.mail.attribute.name";
    public static final String FLAG_MAIL_ATTRIBUTE_NAME = "updatable.flag.mail.attribute.name";

	
	private static Logger log = LoggerFactory.getLogger(Spamd.class);
	
	private static final String PROPERTIES_FILE = "spamd.properties";

	/**
	 * Default constructor that pass the default properties.
	 */
	public Spamd() {
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
			initialProperties.load(Spamd.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			log.error("Error while loading initial properties.",e);
		}
		return initialProperties;
	}
}
