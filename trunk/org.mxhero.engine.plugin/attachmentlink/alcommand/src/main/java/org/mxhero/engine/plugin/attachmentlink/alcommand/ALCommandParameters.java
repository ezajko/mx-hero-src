package org.mxhero.engine.plugin.attachmentlink.alcommand;

import org.mxhero.engine.commons.mail.command.NamedParameters;

/**
 * @author mmarmol
 *
 */
public class ALCommandParameters extends NamedParameters{

	public static final String LOCALE = "locale";
	public static final String NOTIFY = "notify";
	public static final String NOTIFY_MESSAGE = "notifyMessage";
	public static final String NOTIFY_MESSAGE_HTML = "notifyMessageHtml";	
	
	/**
	 * 
	 */
	public ALCommandParameters(){
		super();
	}

	public ALCommandParameters(String locale, Boolean notify, String notifyMessage, String notifyMessageHtml){
		super();
		this.setLocale(locale);
		this.setNotify(notify);
		this.setNotifyMessage(notifyMessage);
		this.setNotifyMessageHtml(notifyMessageHtml);
	}
	
	/**
	 * @param parameters
	 */
	public ALCommandParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	/**
	 * @return
	 */
	public String getLocale() {
		return get(LOCALE);
	}

	/**
	 * @param locale
	 */
	public void setLocale(String locale) {
		put(LOCALE, locale);
	}

	/**
	 * @return
	 */
	public Boolean getNotify() {
		return get(NOTIFY);
	}

	/**
	 * @param notify
	 */
	public void setNotify(Boolean notify) {
		put(NOTIFY, notify);
	}

	/**
	 * @return
	 */
	public String getNotifyMessage() {
		return get(NOTIFY_MESSAGE);
	}

	/**
	 * @param notifyMessage
	 */
	public void setNotifyMessage(String notifyMessage) {
		put(NOTIFY_MESSAGE, notifyMessage);
	}

	/**
	 * @return
	 */
	public String getNotifyMessageHtml() {
		return get(NOTIFY_MESSAGE_HTML);
	}

	/**
	 * @param notifyMessage
	 */
	public void setNotifyMessageHtml(String notifyMessageHtml) {
		put(NOTIFY_MESSAGE_HTML, notifyMessageHtml);
	}

}
