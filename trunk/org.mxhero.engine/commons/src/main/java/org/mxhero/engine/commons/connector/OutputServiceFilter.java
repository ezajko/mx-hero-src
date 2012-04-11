package org.mxhero.engine.commons.connector;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * @author mmarmol
 *
 */
public interface OutputServiceFilter {

	/**
	 * @param mail
	 */
	void dofilter(MimeMail mail);
	
}
