package org.mxhero.engine.core.internal.pool.spliter;

import java.util.Collection;

import org.mxhero.engine.domain.mail.MimeMail;

/**
 * Interface that object will implement to split mails based in recipients after
 * the send phase has ended.
 * 
 * @author mmarmol
 * 
 */
public interface Spliter {

	/**
	 * Actually do the job
	 * @param mail mail that is going to be split.
	 * @return collection of mail split by recipient.
	 */
	Collection<MimeMail> split(MimeMail mail);
}
