package org.mxhero.engine.plugin.basecommands.command;

import org.mxhero.engine.commons.mail.command.Command;

/**
 * This interface represents the GetSize command, used to return the actual size
 * of the email where the command is executed.
 * 
 * @author mmarmol
 */
public interface GetSize extends Command {

	String FORMAT = "format";
	
}
