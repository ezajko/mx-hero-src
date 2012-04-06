package org.mxhero.engine.plugin.basecommands.command;

import org.mxhero.engine.commons.mail.command.Command;

public interface Reply extends Command {

	String SENDER = "sender";
	String RECIPIENT = "recipient";
	String PLAIN_TEXT = "plainText";
	String HTML_TEXT = "htmlText";
	String INCLUDE_MESSAGE = "includeMessage" ;
	String OUTPUT_SERVICE= "outputService";
	
}
