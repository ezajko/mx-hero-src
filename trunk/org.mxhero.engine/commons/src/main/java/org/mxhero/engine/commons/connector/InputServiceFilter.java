package org.mxhero.engine.commons.connector;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * @author mmarmol
 *
 */
public interface InputServiceFilter {

	/**
	 * @param mail
	 */
	void dofilter(MimeMail mail);
}
