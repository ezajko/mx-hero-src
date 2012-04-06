package org.mxhero.engine.commons.finders;

import org.mxhero.engine.commons.domain.User;

/**
 * This interface is used to find users and pass their object to the platform for
 * a given mail. Only one implementation should be used at a given time.
 * 
 * @author mmarmol
 */
public interface UserFinder {

	/**
	 * Takes the user mail and returns the mxhero user.
	 * @param mailAdress
	 * @param domainId
	 * @return
	 */
	User getUser(String mailAdress);
}
