package org.mxhero.engine.commons.mail.api;

import java.util.Collection;

/**
 * @author mmarmol
 */
public interface Recipients {

	public enum RecipientType{
		to,bcc,cc,ng,all;
	}
	
	/**
	 * @param type
	 * @return
	 */
	public Collection<String> getRecipients(Recipients.RecipientType type);

	/**
	 * @param type
	 * @return
	 */
	public boolean hasRecipient(Recipients.RecipientType type, String recipient); 

	/**
	 * @param type
	 * @param recipient
	 */
	public void addRecipient(Recipients.RecipientType type, String recipient); 
	
	/**
	 * @param type
	 * @param recipient
	 * @return
	 */
	public boolean removeRecipient(Recipients.RecipientType type, String recipient);
	
	/**
	 * @param type
	 */
	public void removeAll(Recipients.RecipientType type);
}
