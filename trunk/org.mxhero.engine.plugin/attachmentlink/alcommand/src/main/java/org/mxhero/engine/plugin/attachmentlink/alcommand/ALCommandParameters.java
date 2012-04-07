package org.mxhero.engine.plugin.attachmentlink.alcommand;

import org.mxhero.engine.commons.mail.command.NamedParameters;

public class ALCommandParameters extends NamedParameters{

	public static final String LOCALE = "locale";
	public static final String NOTIFY = "notify";
	public static final String NOTIFY_MESSAGE = "notifyMessage";
	
	public ALCommandParameters(){
		super();
	}

	public ALCommandParameters(NamedParameters parameters){
		super();
		this.nameToInstance.putAll(parameters.getNameToInstance());
	}
	
	public String getLocale() {
		return get(LOCALE);
	}

	public void setLocale(String locale) {
		put(LOCALE, locale);
	}

	public Boolean getNotify() {
		return get(NOTIFY);
	}

	public void setNotify(Boolean notify) {
		put(NOTIFY, notify);
	}

	public String getNotifyMessage() {
		return get(NOTIFY_MESSAGE);
	}

	public void setNotifyMessage(String notifyMessage) {
		put(NOTIFY_MESSAGE, notifyMessage);
	}

}
