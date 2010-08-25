package org.mxhero.engine.domain.statistic;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * Interface that should be implemented so provide a service to log in a data
 * base or other data system mail info. For each mail this will be called for
 * each phase.
 * 
 * @author mmarmol
 */
public interface LogRecord {

	/**
	 * Implements the actual log.
	 * @param mail
	 */
	void log(MimeMail mail);

}
