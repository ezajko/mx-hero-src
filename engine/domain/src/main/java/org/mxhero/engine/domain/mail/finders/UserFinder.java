package org.mxhero.engine.domain.mail.finders;

import org.mxhero.engine.domain.mail.business.User;

/**
 * This interface is used to find users and pass their object to the platform for
 * a given mail. Only one implementation should be used at a given time.
 * 
 * @author mmarmol
 */
public interface UserFinder {

	/**
	 * Takes the user mail, the domain and returns the user.
	 * @param mailAdress
	 * @param domainId
	 * @return
	 */
	User getUser(String mailAdress, String domainId);
}
