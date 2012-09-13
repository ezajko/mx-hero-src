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

package org.mxhero.engine.core.internal.filler;

import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;

/**
 * Used to set business objects to the KnowledgeSession.
 * @author mmarmol
 */
public interface SessionFiller {

	/**
	 * Actually do the work.
	 * @param session where to set the objects.
	 * @param userfinder used to find the user of the mail;
	 * @param domainFinder used to find the domain of the mail.
	 * @param mail the object is used to construct the actual business objects.
	 * @return returns the agenda for this mail.
	 */
	void fill(UserFinder userfinder, MimeMail mail);
	
}
