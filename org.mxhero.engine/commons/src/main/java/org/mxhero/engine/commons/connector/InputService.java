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

package org.mxhero.engine.commons.connector;

import org.mxhero.engine.commons.mail.MimeMail;


/**
 * This interface is used by Core module to implement a service where any connector can use to add mails to be processed.
 * @author mmarmol
 */
public interface InputService {

	/**
	 * This method is used to add mails to the processing queue.
	 * 
	 * @param mail is the of the mail
	 * @throws IllegalArgumentException may throw this exception is any of the parameters are null.
	 */
	void addMail(MimeMail mail) throws QueueFullException;
	
}
