package org.mxhero.engine.domain.properties;

/**
 *This interface is used to implement an update method to classes listening to
 * a PropertiesService that gets updated.
 * 
 * @author mmarmol
 */
public interface PropertiesListener {

	/**
	 * Called when PropertiesService gets updated.
	 */
	void updated();
}
