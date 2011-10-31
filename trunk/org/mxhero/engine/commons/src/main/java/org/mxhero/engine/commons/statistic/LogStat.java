package org.mxhero.engine.commons.statistic;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * Interface that should be implemented to provide the platform a way to log
 * stats for each mail.
 * 
 * @author mmarmol
 */
public interface LogStat {

	/**
	 * Logs the stat.
	 * 
	 * @param mail
	 * @param key
	 * @param value
	 */
	void log(MimeMail mail, String key, String value);
}
