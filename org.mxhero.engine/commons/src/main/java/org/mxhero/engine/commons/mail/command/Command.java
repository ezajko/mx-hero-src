package org.mxhero.engine.commons.mail.command;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * Interface that each new command needs to implement in order to be used inside rules.
 * @author mmarmol
 */
public interface Command {

	/**
	 * Execute a command for the given mail and parameters
	 * @param mail
	 * @param parameters
	 * @return
	 */
	Result exec(MimeMail mail, NamedParameters parameters);
}
