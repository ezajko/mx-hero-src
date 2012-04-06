package org.mxhero.engine.plugin.basecommands.command;

import org.mxhero.engine.commons.mail.command.Command;

/**
 * This interface represents the Create command, used to create a new email when
 * the command is executed.
 * 
 * @author mmarmol
 */
public interface Create extends Command {

	String SENDER = "sender";
	String RECIPIENTS = "recipients";
	String SUBJECT = "subject";
	String TEXT = "text";
	String OUTPUT_SERVICE = "outputService";
	
}
