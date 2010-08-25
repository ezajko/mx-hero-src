package org.mxhero.engine.core.mail.command;

import org.mxhero.engine.core.mail.MailVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Command;
import org.mxhero.engine.domain.mail.command.Result;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the CommandResolver that will search fot the
 * command in the osgi enviroment.
 * 
 * @author mmarmol
 */
public class DefaultCommandResolver implements CommandResolver {

	private static Logger log = LoggerFactory
			.getLogger(DefaultCommandResolver.class);

	private BundleContext bc;

	/**
	 * @param bc
	 */
	public DefaultCommandResolver(BundleContext bc) {
		this.bc = bc;
	}

	/**
	 * @see org.mxhero.engine.core.mail.command.CommandResolver#resolve(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String, java.lang.String[])
	 */
	@Override
	public Result resolve(MimeMail mail, String commandId, String... args) {
		log.debug("trying to found command " + commandId);
		ServiceReference serviceReference = bc.getServiceReference(commandId);
		Command command = null;
		Result result = null;
		if (serviceReference != null) {
			log.debug("command found " + commandId);
			command = (Command) bc.getService(serviceReference);
			if (command != null) {
				result = command.exec(mail, args);
			}
			bc.ungetService(serviceReference);
			log.debug("command reference release");
		}
		log.debug("result is " + result);
		return result;
	}

	/**
	 * utility method used to set this resolver in the mailvo object.
	 */
	public void setResolver() {
		MailVO.setFinder(this);
	}
}
