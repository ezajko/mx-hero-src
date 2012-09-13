/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.core.internal.mail.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Command;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.core.internal.mail.CMail;
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
	public Result resolve(MimeMail mail, String commandId, NamedParameters parameters) {

		ServiceReference serviceReference = bc.getServiceReference(commandId);
		Command command = null;
		Result result = null;
		if (serviceReference != null) {
			if(log.isDebugEnabled()){
				log.debug("command found " + commandId);
			}
			command = (Command) bc.getService(serviceReference);
			if (command != null) {
				result = command.exec(mail, parameters);
			}
			bc.ungetService(serviceReference);
		}
		log.debug("result is " + result);
		return result;
	}

	/**
	 * utility method used to set this resolver in the CMail object.
	 */
	public void setResolver() {
		CMail.setResolver(this);
	}
}
