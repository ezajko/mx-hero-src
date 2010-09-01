package org.mxhero.engine.plugin.statistics.command;

import org.mxhero.engine.domain.mail.command.Command;
import org.springframework.transaction.annotation.Transactional;


/**
 * Used to get the amount of emails from a user in the last X hours.
 * @author mmarmol
 */
@Transactional(readOnly=true)
public interface UserMailsPerHour extends Command {

}
