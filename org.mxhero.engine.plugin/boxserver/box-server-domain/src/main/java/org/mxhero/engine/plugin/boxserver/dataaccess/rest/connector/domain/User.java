package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * The Class User.
 */
@XmlRootElement(name = "user")
public class User {
	
	/** The email. */
	private String email;
	
	/** The user_id. */
	private String user_id;
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	@XmlElement
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the user_id.
	 *
	 * @return the user_id
	 */
	@XmlElement
	public String getUser_id() {
		return user_id;
	}
	
	/**
	 * Sets the user_id.
	 *
	 * @param user_id the new user_id
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
