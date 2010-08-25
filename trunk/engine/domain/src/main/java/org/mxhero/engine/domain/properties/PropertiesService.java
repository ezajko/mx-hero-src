package org.mxhero.engine.domain.properties;

import java.util.Dictionary;
import java.util.LinkedHashSet;
import java.util.Set;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This service is used used so we can have a service that is not really related
 * to a service, and can be related to a plugin so we can use the console and
 * the Config Admin from osgi to update properties in the hole plugin.
 * 
 * @author mmarmol
 */
public class PropertiesService implements ManagedService {

	@SuppressWarnings("unchecked")
	private Dictionary properties;

	private Set<PropertiesListener> listeners = new LinkedHashSet<PropertiesListener>();

	/**
	 * @param properties
	 */
	@SuppressWarnings("unchecked")
	public PropertiesService(Dictionary properties) {
		this.properties = properties;
	}

	/**
	 * @param key
	 * @return
	 */
	public final String getValue(String key) {
		if (properties == null) {
			return null;
		}
		return (String) properties.get(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public final String getValue(String key, String defaultValue) {
		if (properties == null) {
			return defaultValue;
		}
		String value = (String) properties.get(key);
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * @param listener
	 */
	public final void addListener(PropertiesListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public final void removeListener(PropertiesListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Implementation of the updated methos, it will call all the listeners and
	 * call their updated() method.
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void updated(Dictionary properties)
			throws ConfigurationException {
		this.properties = properties;
		for (PropertiesListener listener : listeners) {
			listener.updated();
		}
	}

}
