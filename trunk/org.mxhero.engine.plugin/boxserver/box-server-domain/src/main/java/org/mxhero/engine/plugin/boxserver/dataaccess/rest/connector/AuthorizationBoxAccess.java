package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector;

import org.mxhero.engine.plugin.boxserver.dataaccess.rest.AuthorizationAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.TicketResponse;
import org.springframework.web.client.RestTemplate;

/**
 * The Class AuthorizationBoxAccess.
 */
public class AuthorizationBoxAccess implements AuthorizationAccess {
	
	/** The rest template. */
	private RestTemplate restTemplate;
	
	/** The ticket url. */
	private String ticketUrl;
	
	/** The user url. */
	private String userUrl;
	
	/** The api key. */
	private String apiKey;

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.internal.box.dataaccess.rest.AuthorizationAccess#createNewTicket()
	 */
	@Override
	public String createNewTicket() {
		TicketResponse forObject = getRestTemplate().getForObject(getTicketUrl(), TicketResponse.class, getApiKey());
		return forObject.getTicket();
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxserver.internal.box.dataaccess.rest.AuthorizationAccess#getUserToken(java.lang.String)
	 */
	@Override
	public TicketResponse getUserToken(String ticket) {
		TicketResponse forObject = getRestTemplate().getForObject(getUserUrl(), TicketResponse.class, getApiKey(), ticket);
		return forObject;
	}

	/**
	 * Gets the rest template.
	 *
	 * @return the rest template
	 */
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/**
	 * Sets the rest template.
	 *
	 * @param restTemplate the new rest template
	 */
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Gets the ticket url.
	 *
	 * @return the ticket url
	 */
	public String getTicketUrl() {
		return ticketUrl;
	}

	/**
	 * Sets the ticket url.
	 *
	 * @param ticketUrl the new ticket url
	 */
	public void setTicketUrl(String ticketUrl) {
		this.ticketUrl = ticketUrl;
	}


	/**
	 * Gets the user url.
	 *
	 * @return the user url
	 */
	public String getUserUrl() {
		return userUrl;
	}

	/**
	 * Sets the user url.
	 *
	 * @param userUrl the new user url
	 */
	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
