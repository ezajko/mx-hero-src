package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Represents the attachments in a mail so can be used in rules.
 * @author mmarmol
 */
public class Attachments {

	private boolean attached;
	
	private Collection<String> types;
	
	private String typesStr;
	
	private Collection<String> fileNames;
	
	private String fileNamesStr;
	
	private Collection<String> extensions;
	
	private String extensionsStr;

	/**
	 * @return the attached
	 */
	public boolean isAttached() {
		return attached;
	}

	/**
	 * @param attached the attachments to set
	 */
	public void setAttached(boolean attached) {
		this.attached = attached;
	}

	/**
	 * @return the types
	 */
	public Collection<String> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(Collection<String> types) {
		this.types = types;
	}

	/**
	 * @return the typesStr
	 */
	public String getTypesStr() {
		return typesStr;
	}

	/**
	 * @param typesStr the typesStr to set
	 */
	public void setTypesStr(String typesStr) {
		this.typesStr = typesStr;
	}

	/**
	 * @return the fileNames
	 */
	public Collection<String> getFileNames() {
		return fileNames;
	}

	/**
	 * @param fileNames the fileNames to set
	 */
	public void setFileNames(Collection<String> fileNames) {
		this.fileNames = fileNames;
	}

	/**
	 * @return the fileNamesStr
	 */
	public String getFileNamesStr() {
		return fileNamesStr;
	}

	/**
	 * @param fileNamesStr the fileNamesStr to set
	 */
	public void setFileNamesStr(String fileNamesStr) {
		this.fileNamesStr = fileNamesStr;
	}

	/**
	 * @return the extensions
	 */
	public Collection<String> getExtensions() {
		return extensions;
	}

	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(Collection<String> extensions) {
		this.extensions = extensions;
	}

	/**
	 * @return the extensionsStr
	 */
	public String getExtensionsStr() {
		return extensionsStr;
	}

	/**
	 * @param extensionsStr the extensionsStr to set
	 */
	public void setExtensionsStr(String extensionsStr) {
		this.extensionsStr = extensionsStr;
	}
	
	public boolean hasMatchingType(String pattern){
		return false;
	}
	
	public boolean hasMatchingType(Collection<String> patterns){
		return false;
	}
	
	public boolean hasMatchingType(String[] patterns){
		return false;
	}
	
	public boolean hasMatchingName(String pattern){
		return false;
	}
	
	public boolean hasMatchingName(Collection<String> patterns){
		return false;
	}
	
	public boolean hasMatchingName(String[] patterns){
		return false;
	}
	
	public boolean hasMatchingExtension(String pattern){
		return false;
	}
	
	public boolean hasMatchingExtension(Collection<String> patterns){
		return false;
	}
	
	public boolean hasMatchingExtension(String[] patterns){
		return false;
	}
}
