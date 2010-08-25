package org.mxhero.engine.domain.connector;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * This interface is implemented for any connector that needs 
 * to be register to get the mail back after the core has 
 * ended processing it. 
 * @author mmarmol
 */
public interface OutputService {

	/**
	 * @param mail
	 * Takes a mail back after response from the core engine.
	 */
	void addOutMail(MimeMail mail);
	
}
