package org.mxhero.engine.plugin.attachmentlink.alcommand;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * The Class ALCommandParameters.
 *
 * @author mmarmol
 */
public class ALCommandParameters extends NamedParameters{

	/** The Constant LOCALE. */
	public static final String LOCALE = "locale";
	
	/** The Constant NOTIFY. */
	public static final String NOTIFY = "notify";
	
	/** The Constant NOTIFY_MESSAGE. */
	public static final String NOTIFY_MESSAGE = "notifyMessage";
	
	/** The Constant NOTIFY_MESSAGE_HTML. */
	public static final String NOTIFY_MESSAGE_HTML = "notifyMessageHtml";
	
	/** The Constant STORAGE_ID. */
	public static final String STORAGE_ID = "storageId";
	
	/** The Constant SENDER_STORAGE. */
	public static final String SENDER_STORAGE = "senderStorage";
	
	/** The Constant RECIPIENT_STORAGE. */
	public static final String RECIPIENT_STORAGE = "recipientStorage";
	
	/**
	 * Instantiates a new aL command parameters.
	 */
	public ALCommandParameters(){
		super();
	}

	/**
	 * Instantiates a new aL command parameters.
	 *
	 * @param locale the locale
	 * @param notify the notify
	 * @param notifyMessage the notify message
	 * @param notifyMessageHtml the notify message html
	 */
	public ALCommandParameters(String locale, Boolean notify, String notifyMessage, String notifyMessageHtml){
		super();
		this.setLocale(locale);
		this.setNotify(notify);
		this.setNotifyMessage(notifyMessage);
		this.setNotifyMessageHtml(notifyMessageHtml);
	}
	
	/**
	 * Instantiates a new aL command parameters.
	 *
	 * @param parameters the parameters
	 */
	public ALCommandParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	public String getLocale() {
		return get(LOCALE);
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale the new locale
	 */
	public void setLocale(String locale) {
		put(LOCALE, locale);
	}

	/**
	 * Gets the notify.
	 *
	 * @return the notify
	 */
	public Boolean getNotify() {
		return get(NOTIFY);
	}

	/**
	 * Sets the notify.
	 *
	 * @param notify the new notify
	 */
	public void setNotify(Boolean notify) {
		put(NOTIFY, notify);
	}

	/**
	 * Gets the notify message.
	 *
	 * @return the notify message
	 */
	public String getNotifyMessage() {
		return get(NOTIFY_MESSAGE);
	}

	/**
	 * Sets the notify message.
	 *
	 * @param notifyMessage the new notify message
	 */
	public void setNotifyMessage(String notifyMessage) {
		put(NOTIFY_MESSAGE, notifyMessage);
	}

	/**
	 * Gets the notify message html.
	 *
	 * @return the notify message html
	 */
	public String getNotifyMessageHtml() {
		return get(NOTIFY_MESSAGE_HTML);
	}

	/**
	 * Sets the notify message html.
	 *
	 * @param notifyMessageHtml the new notify message html
	 */
	public void setNotifyMessageHtml(String notifyMessageHtml) {
		put(NOTIFY_MESSAGE_HTML, notifyMessageHtml);
	}

	/**
	 * Gets the storage id.
	 *
	 * @return the storage id
	 */
	public String getStorageId() {
		return get(STORAGE_ID);
	}
	
	/**
	 * Sets the storage id.
	 *
	 * @param storageId the new storage id
	 */
	public void setStorageId(String storageId){
		put(STORAGE_ID, storageId);
	}

	/**
	 * Gets the sender storage.
	 *
	 * @return the sender storage
	 */
	public String getSenderStorage() {
		return get(SENDER_STORAGE);
	}
	
	/**
	 * Sets the sender storage.
	 *
	 * @param emailSender the new sender storage
	 */
	public void setSenderStorage(String emailSender){
		put(SENDER_STORAGE, emailSender);
	}


	/**
	 * Gets the recipient storage.
	 *
	 * @return the recipient storage
	 */
	public String getRecipientStorage() {
		return get(RECIPIENT_STORAGE);
	}
	
	/**
	 * Sets the recipient storage.
	 *
	 * @param recipientStorage the new recipient storage
	 */
	public void setRecipientStorage(String recipientStorage){
		put(RECIPIENT_STORAGE, recipientStorage);
	}
}
