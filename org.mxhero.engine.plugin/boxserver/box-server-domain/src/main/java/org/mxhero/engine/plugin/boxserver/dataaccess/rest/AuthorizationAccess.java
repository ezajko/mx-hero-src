package org.mxhero.engine.plugin.boxserver.dataaccess.rest;

import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.TicketResponse;

/**
 * The Interface AuthorizationAccess.
 */
public interface AuthorizationAccess {

	/**
	 * Creates the new ticket.
	 *
	 * @return the string
	 */
	String createNewTicket();

	/**
	 * Gets the user token.
	 *
	 * @param ticket the ticket
	 * @return the user token
	 */
	TicketResponse getUserToken(String ticket);

}
