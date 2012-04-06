package org.mxhero.engine.plugin.clamd.command;

import org.mxhero.engine.commons.mail.command.Command;

/**
 * Interface for command ClamavScan. Used to scan the email for virus.
 * @author mmarmol
 */
public interface ClamavScan extends Command{

	String REMOVE_INFECTED = "removeInfected";
	String ADD_HEADER = "addHeader";
	String HEADER_NAME = "headerName";
	
}
