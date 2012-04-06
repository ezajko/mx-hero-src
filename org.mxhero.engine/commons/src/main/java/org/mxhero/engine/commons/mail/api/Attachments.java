package org.mxhero.engine.commons.mail.api;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author mmarmol
 */
public interface Attachments {

	/**
	 * @return the attached
	 */
	public boolean isAttached();

	/**
	 * @return the types
	 */
	public Collection<String> getTypes();

	/**
	 * @return the fileNames
	 */
	public Collection<String> getFileNames();

	/**
	 * @return the extensions
	 */
	public Collection<String> getExtensions();

	/**
	 * @param pattern
	 * @return
	 */
	public boolean hasMatchingType(String... patterns);
	
	/**
	 * @param pattern
	 * @return
	 */
	public boolean hasMatchingName(String... patterns);

	/**
	 * @param pattern
	 * @return
	 */
	public boolean hasMatchingExtension(String... patterns);
	
	/**
	 * @param name
	 * @return
	 */
	public List<String> removeByName(String... names);
	
	/**
	 * @param extension
	 * @return
	 */
	public List<String> removeByExtension(String... extensions);
	
	/**
	 * @param type
	 * @return
	 */
	public List<String> removeByType(String... types);
	
}
