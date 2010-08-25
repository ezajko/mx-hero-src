package org.mxhero.engine.core.mail.command;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;

/**
 * Interface used to implement classes that will find command in the framework
 * and call their execution passing a result.
 * 
 * @author mmarmol
 */
public interface CommandResolver {

	/**
	 * Actually do the work
	 * @param mail where the command should be executed
	 * @param commandId id of the command to be found
	 * @param args parameters for the command
	 * @return result of the execution
	 */
	Result resolve(MimeMail mail, String commandId, String... args);
}
