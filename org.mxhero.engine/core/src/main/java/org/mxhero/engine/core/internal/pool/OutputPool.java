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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.core.internal.CoreProperties;
import org.mxhero.engine.core.internal.pool.task.DeliverTask;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a DeliverTask for each mail that enter in the outputQueue.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool implements Observer {
	
	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private BundleContext bc;
	
	private CoreProperties properties;
	
	private LogStat logStatService;
	
	private MimeMailQueueService queueService;
	
	@SuppressWarnings("rawtypes")
	private List filters;
	
	/**
	 * @param bc
	 */
	public OutputPool(BundleContext bc, MimeMailQueueService queueService){
		super(Mail.Phase.out,queueService);
		if (bc==null){
        	throw new IllegalArgumentException();
        }
		this.bc = bc;	
		this.queueService=queueService;
	}
	
	/**
	 * @see org.mxhero.engine.domain.pool.TaskPool#init()
	 */
	@Override
	protected void init() {
		log.info("INIT");
		getProperties().addObserver(this);
		update(null, null);
	}
	
	/**
	 * @see org.mxhero.engine.domain.pool.TaskPool#clean()
	 */
	@Override
	protected void clean() {
		log.info("CLEAN");
		getProperties().deleteObserver(this);
	}

	/**
	 * @see org.mxhero.engine.core.internal.pool.domain.pool.QueueTaskPool#createTask(java.lang.Object)
	 */
	@Override
	protected Runnable createTask(MimeMail mail) {
		DeliverTask task = new DeliverTask(mail,bc,queueService);
		task.setProperties(getProperties());
		task.setLogStatService(getLogStatService());
		task.setFilters(filters);
		if(log.isDebugEnabled()){
			log.debug("Out Phase task created for " + mail);
		}
		return task;
		
	}

	/**
	 * @return the logStatService
	 */
	public LogStat getLogStatService() {
		return logStatService;
	}

	/**
	 * @param logStatService the logStatService to set
	 */
	public void setLogStatService(LogStat logStatService) {
		this.logStatService = logStatService;
	}

	/**
	 * @return
	 */
	public CoreProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 */
	public void setProperties(CoreProperties properties) {
		this.properties = properties;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setCorePoolsize(getProperties().getCorePoolsize());
		this.setKeepAliveTime(getProperties().getKeepAliveTime());
		this.setMaximumPoolSize(getProperties().getMaximumPoolSize());
		this.setWaitTime(getProperties().getWaitTime());
		log.debug("updated with "+getProperties().toString());
	}

	@SuppressWarnings("rawtypes")
	public List getFilters() {
		return filters;
	}

	@SuppressWarnings("rawtypes")
	public void setFilters(List filters) {
		this.filters = filters;
	}

}
