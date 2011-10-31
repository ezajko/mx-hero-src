package org.mxhero.engine.commons.mail.command;

import org.mxhero.engine.commons.mail.MimeMail;

/**
 * Interface that each new command needs to implement in order to be used inside rules.
 * @author mmarmol
 */
public interface Command {


	/**
	 * Execute a command for the given mail and parameters
	 * @param mail mail where it will execute
	 * @param args parameters
	 * @return result or the execution
	 */
	Result exec(MimeMail mail, String... args);
}
