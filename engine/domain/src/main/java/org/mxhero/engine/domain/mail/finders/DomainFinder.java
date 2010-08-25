package org.mxhero.engine.domain.mail.finders;

import org.mxhero.engine.domain.mail.business.Domain;

/**
 * This interface is used to find domains and pass their object to the platform for
 * a given mail. Only one implementation should be used at a given time.
 * 
 * @author mmarmol
 */
public interface DomainFinder {

	/**
	 * Takes the mail if the user an returns the domain.
	 * @param mailAdress
	 * @return
	 */
	Domain getDomain(String mailAdress);

}
