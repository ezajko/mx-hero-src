package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

// TODO: Auto-generated Javadoc
/**
 * The Class TicketResponse.
 */
@XmlRootElement(name = "response")
public class TicketResponse{
	
	/** The status. */
	private String status;
	
	/** The ticket. */
	private String ticket;
	
	/** The auth_token. */
	private String auth_token;
	
	/** The user. */
	private User user;
	
	/**
	 * Gets the auth_token.
	 *
	 * @return the auth_token
	 */
	@XmlElement(required = false)
	public String getAuth_token() {
		return auth_token;
	}
	
	/**
	 * Sets the auth_token.
	 *
	 * @param auth_token the new auth_token
	 */
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	@XmlElement(required = false)
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@XmlElement
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Gets the ticket.
	 *
	 * @return the ticket
	 */
	@XmlElement(required = false)
	public String getTicket() {
		return ticket;
	}
	
	/**
	 * Sets the ticket.
	 *
	 * @param ticket the new ticket
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
