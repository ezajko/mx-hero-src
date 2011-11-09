package org.mxhero.console.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="system_properties",schema="mxhero")
public class SystemProperty {

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
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="property_key", nullable=false, length=100)
	private String propertyKey;
	
	@Column(name="property_value", length=100 )
	private String propertyValue;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String key) {
		this.propertyKey = key;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String value) {
		this.propertyValue = value;
	}

}
