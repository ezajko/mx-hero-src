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

package org.mxhero.engine.core.internal.pool;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;

/**
 * 
 * This class is used to implements a ThreadPool based on tasks that
 * are created from object polled from a queue.
 * @author mmarmol
 * @param <T> Type of the object that is pooled of the queue.
 */
public abstract class QueueTaskPool extends TaskPool{

	private MimeMailQueueService queue;
	
	private Mail.Phase phase;
	
	/**
	 * Constructor that receives the queue where object to be processed are.
	 * @param queue
	 */
	public QueueTaskPool( Mail.Phase phase, MimeMailQueueService queue){
		this.queue = queue;
		this.phase=phase;
	}

	/**
	 * Will try to get and object of type <T> from the queue, if this 
	 * object is not null it will call createTask(object) to get the task 
	 * to be added to the pool if that task is not null. If task is null 
	 * the object will be added again into the queue.
	 */
	@Override
	protected void work(){
		MimeMail object = null;
		Runnable task = null;
		try{
			object = queue.poll(phase,getWaitTime(), TimeUnit.MILLISECONDS);
			if (object!=null){
				task = createTask(object);
				if (task != null){
					getExecutor().execute(task);
				} else {
					queue.put(phase, object);
					Thread.sleep(getWaitTime());
				}
			}
		} catch (InterruptedException e){
			System.out.println("interrupted while polling");
		}
	}
	
	/**
	 * This is the method factory for task based on the object <T>.
	 * @param object of type <T>
	 * @return the task to be added to the ThreadPool or null if the task should not be created.
	 */
	protected abstract Runnable createTask(MimeMail object);

	public Mail.Phase getPhase() {
		return phase;
	}

}
