package org.mxhero.engine.core.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.mail.MessagingException;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Attachments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Override the original Attachments to implement MimeMessage related logic.
 * @author mmarmol
 */
public class AttachmentsVO extends Attachments {

	private static Logger log = LoggerFactory.getLogger(AttachmentsVO.class);
	
	private MimeMail mimeMail;
	
	/**
	 * @param mimeMail
	 */
	public AttachmentsVO(MimeMail mimeMail){
		this.mimeMail = mimeMail;
	}
	
	/**
	 * Search for any kind of attachment.
	 * @return the attached
	 */
	@Override
	public boolean isAttached() {
		try {
			return MailUtils.hasAttachments(this.mimeMail.getMessage());
		} catch (MessagingException e) {
			log.warn(MailVO.MIME_ERROR);
			return false;
		} catch (IOException e) {
			log.warn(MailVO.MIME_ERROR);
			return false;
		}
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param attached the attachments to set
	 */
	@Override
	public void setAttached(boolean attached) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * Search for all the type of the mimeMessage
	 * @return the types
	 */
	@Override
	public Collection<String> getTypes() {
		Collection<String> types = new ArrayList<String>();
		try {
			MailUtils.addTypes(this.mimeMail.getMessage(), types);
		} catch (MessagingException e) {
			log.warn(MailVO.MIME_ERROR);
		} catch (IOException e) {
			log.warn(MailVO.MIME_ERROR);
		}
		return types;
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param types the types to set
	 */
	@Override
	public void setTypes(Collection<String> types) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the typesStr
	 */
	@Override
	public String getTypesStr() {
		return Arrays.toString(getTypes().toArray());
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param typesStr the typesStr to set
	 */
	@Override
	public void setTypesStr(String typesStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * Returns all the file names of the mimeMessage
	 * @return the fileNames
	 */
	@Override
	public Collection<String> getFileNames() {
		Collection<String> files = new ArrayList<String>();
		try {
			MailUtils.addFileNames(this.mimeMail.getMessage(), files);
		} catch (MessagingException e) {
			log.warn(MailVO.MIME_ERROR);
		} catch (IOException e) {
			log.warn(MailVO.MIME_ERROR);
		}
		return files;
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param fileNames the fileNames to set
	 */
	@Override
	public void setFileNames(Collection<String> fileNames) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the fileNamesStr
	 */
	@Override
	public String getFileNamesStr() {
		return Arrays.toString(getFileNames().toArray());
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param fileNamesStr the fileNamesStr to set
	 */
	@Override
	public void setFileNamesStr(String fileNamesStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * Returns all the extensions of the mimeMessage
	 * @return the extensions
	 */
	@Override
	public Collection<String> getExtensions() {
		Collection<String> extension = new ArrayList<String>();
		for(String fileName : getFileNames()){
			int i = fileName.lastIndexOf('.');
			if (i > 0 && i < fileName.length() - 1){
				extension.add(fileName.substring(i+1).toLowerCase(Locale.ENGLISH));
			}
		}	
		return extension;
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param extensions the extensions to set
	 */
	@Override
	public void setExtensions(Collection<String> extensions) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	/**
	 * @return the extensionsStr
	 */
	@Override
	public String getExtensionsStr() {
		return Arrays.toString(getExtensions().toArray());
	}

	/**
	 * Should not be call, just override it for drools.
	 * @param extensionsStr the extensionsStr to set
	 */
	@Override
	public void setExtensionsStr(String extensionsStr) {
		log.warn(MailVO.CHANGE_ERROR+this);
	}

	@Override
	public boolean hasMatchingType(String pattern){
		for(String type :getTypes()){
			if(type.matches(pattern)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMatchingType(Collection<String> patterns){
		return this.hasMatchingType((String[])patterns.toArray());
	}
	
	@Override
	public boolean hasMatchingType(String[] patterns){
		for(String type :getTypes()){
			for(String pattern : patterns){
				if(type.matches(pattern)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMatchingName(String pattern){
		for(String name :getFileNames()){
			if(name.matches(pattern)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMatchingName(Collection<String> patterns){
		return this.hasMatchingName((String[])patterns.toArray());
	}
	
	@Override
	public boolean hasMatchingName(String[] patterns){
		for(String name :getFileNames()){
			for(String pattern : patterns){
				if(name.matches(pattern)){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMatchingExtension(String pattern){
		for(String extension :getExtensions()){
			if(extension.matches(pattern)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean hasMatchingExtension(Collection<String> patterns){
		return this.hasMatchingExtension((String[])patterns.toArray());
	}
	
	@Override
	public boolean hasMatchingExtension(String[] patterns){
		for(String extension :getExtensions()){
			for(String pattern : patterns){
				if(extension.matches(pattern)){
					return true;
				}
			}
		}
		return false; 
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttachmentsVO [isAttached()=").append(isAttached())
				.append(", getFileNamesStr()=").append(getFileNamesStr())
				.append(", getExtensionsStr()=").append(getExtensionsStr())
				.append(", getTypesStr()=").append(getTypesStr()).append("]");
		return builder.toString();
	}

}
