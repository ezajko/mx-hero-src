package org.mxhero.console.backend.vo;

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
	public final static String DOCUMENTATION_URL="documentation.url";
	public final static String LICENSE="license.data";
	
	private String propertyKey;
	
	private String propertyValue;

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((propertyKey == null) ? 0 : propertyKey.hashCode());
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
		if (propertyKey == null) {
			if (other.propertyKey != null)
				return false;
		} else if (!propertyKey.equals(other.propertyKey))
			return false;
		return true;
	}
	
}
