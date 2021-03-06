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

import java.util.Observable;
import java.util.Observer;

import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.statistic.LogRecord;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.core.internal.CoreProperties;
import org.mxhero.engine.core.internal.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.pool.task.RecipientRuleTask;
import org.mxhero.engine.core.internal.rules.BaseLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a task for each mail that enter in the inputQueue. This task is
 * different for each RulePhase. This task will execute rules over the mail.
 * 
 * @author mmarmol
 */
public final class ReceivePool extends QueueTaskPool implements Observer {
	
	private static Logger log = LoggerFactory.getLogger(ReceivePool.class);

	private UserFinder userFinderService;

	private RulesProcessor processor;

	private BaseLoader loader;

	private SessionFiller filler;

	private CoreProperties properties;

	private LogRecord logRecordService;

	private LogStat logStatService;
	
	private MimeMailQueueService queueService;


	/**
	 * Creates the object and pass to super the queue.
	 */
	public ReceivePool(MimeMailQueueService queueService) {
		super(Mail.Phase.receive,queueService);
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
	protected Runnable createTask(MimeMail object) {
		try{
			if(log.isDebugEnabled()){
				log.debug("Receive Phase for " + object);
			}
			if(loader.getBuilder().getBase()==null){
				final MimeMail mail = object;
				return new Runnable() {
					@Override
					public void run() {
						log.warn("Rule database is not ready " + mail);
						try {
							queueService.delayAndPut(ReceivePool.this.getPhase(), mail, getProperties().getQueueDelayTime());
						} catch (InterruptedException e1) {
							log.debug("error while reEnqueue email ",e1);
							log.error("error while reEnqueue email "+e1.toString());
						}
					}
				};
			} else {
				object.setPhase(this.getPhase());
				if(object.getBussinesObject()==null){
					this.getFiller().fill(getUserFinderService(), object);
				}
				RecipientRuleTask task = new RecipientRuleTask(loader.getBuilder().getBase(), object, this.queueService);
				task.setProcessor(this.getProcessor());
				task.setLogRecordService(getLogRecordService());
				task.setLogStatService(getLogStatService());
				task.setProperties(getProperties());
				return task;
			} 
		}catch(Exception e){
			final MimeMail mail = object;
			log.error("Error while trying to do task for " + mail +" "+e.getClass().getCanonicalName()+": "+e.getMessage());
			return new Runnable() {
				@Override
				public void run() {
					try {
						queueService.delayAndPut(ReceivePool.this.getPhase(), mail, getProperties().getQueueDelayTime());
					} catch (InterruptedException e) {
						log.debug("Error sending mail to queue again " + mail,e);
						log.error("Error sending mail to queue again " + mail + e.getClass().getCanonicalName() +": " + e.getMessage());
					}
				}
			};
		}
	}


	/**
	 * @return the processor
	 */
	public RulesProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor
	 *            the processor to set
	 */
	public void setProcessor(RulesProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @return the filler
	 */
	public SessionFiller getFiller() {
		return filler;
	}

	/**
	 * @param filler
	 *            the filler to set
	 */
	public void setFiller(SessionFiller filler) {
		this.filler = filler;
	}

	/**
	 * @return the userFinderService
	 */
	public UserFinder getUserFinderService() {
		return userFinderService;
	}

	/**
	 * @param userFinderService
	 *            the userFinderService to set
	 */
	public void setUserFinderService(UserFinder userFinderService) {
		this.userFinderService = userFinderService;
	}

	public CoreProperties getProperties() {
		return properties;
	}

	public void setProperties(CoreProperties properties) {
		this.properties = properties;
	}

	/**
	 * @return the loader
	 */
	public BaseLoader getLoader() {
		return loader;
	}

	/**
	 * @param loader
	 *            the loader to set
	 */
	public void setLoader(BaseLoader loader) {
		this.loader = loader;
	}

	/**
	 * @return the logRecordService
	 */
	public LogRecord getLogRecordService() {
		return logRecordService;
	}

	/**
	 * @param logRecordService
	 *            the logRecordService to set
	 */
	public void setLogRecordService(LogRecord logRecordService) {
		this.logRecordService = logRecordService;
	}

	/**
	 * @return the logStatService
	 */
	public LogStat getLogStatService() {
		return logStatService;
	}

	/**
	 * @param logStatService
	 *            the logStatService to set
	 */
	public void setLogStatService(LogStat logStatService) {
		this.logStatService = logStatService;
	}

	public MimeMailQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(MimeMailQueueService queueService) {
		this.queueService = queueService;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setCorePoolsize(getProperties().getCorePoolsize());
		this.setKeepAliveTime(getProperties().getKeepAliveTime());
		this.setMaximumPoolSize(getProperties().getMaximumPoolSize());
		this.setWaitTime(getProperties().getWaitTime());
		log.debug("updated with "+getProperties().toString());
	}
	
}
