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

package org.mxhero.engine.commons.queue;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;

/**
 * @author mmarmol
 *
 */
public interface MimeMailQueueService {

	/**
	 * @param phase
	 * @param mail
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public MimeMail store(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	/**
	 * @param mail
	 */
	public void unstore(MimeMail mail);
	
	/**
	 * @param phase
	 * @param mail
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public boolean offer(Mail.Phase phase, MimeMail mail, long timeout, TimeUnit unit)
    throws InterruptedException;
	
	/**
	 * @param phase
	 * @param mail
	 * @throws InterruptedException
	 */
	public void put(Mail.Phase phase, MimeMail mail)
    throws InterruptedException;
	
	/**
	 * @param phase
	 * @param mail
	 * @param millisenconds
	 * @throws InterruptedException
	 */
	public void delayAndPut(Mail.Phase phase, MimeMail mail, long millisenconds)
	throws InterruptedException;
	
	/**
	 * @param phase
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 */
	public MimeMail poll(Mail.Phase phase, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * @param mail
	 * @param path
	 * @param useTmp
	 */
	public void saveToAndUnstore(MimeMail mail, String path, boolean useTmp);
	
	/**
	 * 
	 */
	public void logState();
	
}
