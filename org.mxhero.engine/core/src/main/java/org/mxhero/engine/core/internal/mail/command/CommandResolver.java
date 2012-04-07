package org.mxhero.engine.core.internal.mail.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;


/**
 * Interface used to implement classes that will find command in the framework
 * and call their execution passing a result.
 * 
 * @author mmarmol
 */
public interface CommandResolver {

	/**
	 * @param mail
	 * @param commandId
	 * @param parameters
	 * @return
	 */
	Result resolve(MimeMail mail, String commandId, NamedParameters parameters);
}
