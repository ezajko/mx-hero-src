package org.mxhero.engine.plugin.basecommands.command;

import org.mxhero.engine.commons.mail.command.Command;

/**
 * This interface represents the Clone command, used to add clone the email
 * where the command is executed.
 * 
 * @author mmarmol
 */
public interface Clone extends Command {

	String PHASE = "phase";
	String SENDER = "sender";
	String RECIPIENT = "recipient";
	String OUTPUT_SERVICE = "outputService";
	String GENERATE_ID = "generateId";
	String OVERRIDE = "override";
	
}
