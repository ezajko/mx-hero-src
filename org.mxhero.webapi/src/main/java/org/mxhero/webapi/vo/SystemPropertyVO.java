package org.mxhero.webapi.vo;

public class SystemPropertyVO {

	public final static String MAIL_SMTP_HOST="mail.smtp.host";
	public final static String MAIL_SMTP_AUTH="mail.smtp.auth";
	public final static String MAIL_SMTP_PORT="mail.smtp.port";
	public final static String MAIL_SMTP_SSL_ENABLE="mail.smtp.ssl.enable";
	public final static String MAIL_ADMIN="mail.admin";
	public final static String MAIL_SMTP_USER="mail.smtp.user";
	public final static String MAIL_SMTP_PASSWORD="mail.smtp.password";
	public final static String DEFAULT_USER_LANGUAGE="default.user.language";
	public final static String EXTERNAL_LOGO_PATH="external.logo.path";
	public final static String NEWS_FEED_ENABLED="news.feed.enabled";
	
	private String key;
	
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemPropertyVO other = (SystemPropertyVO) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
}
