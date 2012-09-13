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

package org.mxhero.engine.core.internal.pool.task;

import java.util.List;

import org.mxhero.engine.commons.connector.OutputService;
import org.mxhero.engine.commons.connector.OutputServiceFilter;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.core.internal.CoreProperties;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a task that will find the output service from the mail
 * outputServiceId and call it.
 * 
 * @author mmarmol
 */
public final class DeliverTask implements Runnable {

	private static Logger log = LoggerFactory.getLogger(DeliverTask.class);
	private MimeMail mail;
	private BundleContext bc;
	private LogStat logStatService;
	private CoreProperties properties;
	private MimeMailQueueService queueService;
	@SuppressWarnings("rawtypes")
	private List filters;
	/**
	 * @param mail
	 * @param bc
	 */
	public DeliverTask(MimeMail mail, BundleContext bc, MimeMailQueueService queueService) {
		this.mail = mail;
		this.bc = bc;
		this.queueService=queueService;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.debug("email has status:"+mail.getStatus().name());
		if(filters!=null){
			for(Object filter : filters){
				if(filter instanceof OutputServiceFilter){
					try{
						((OutputServiceFilter) filter).dofilter(mail);
						log.debug("doing filter "+filter.getClass().getName());
					}catch(Exception e){
						log.warn("error while doing filter "+filter.getClass().getCanonicalName(),e);
					}
				}
			}
		}
		if(mail.getStatus().equals(Mail.Status.deliver)){
			log.debug("doing delivery for "+mail);
			deliver();
		}else{
			log.info(mail.getStatus().name().toUpperCase() +" "+mail.toString());
			queueService.unstore(mail);
		}
	}
	
	private void deliver(){
		ServiceReference serviceReference;
		OutputService service;
		boolean delivered = false;
		try {
			serviceReference = bc.getServiceReference(mail
					.getResponseServiceId());
			if (serviceReference != null) {
				service = (OutputService) bc.getService(serviceReference);
				if (service != null) {
					try{
						service.addOutMail(mail);
						delivered=true;
					}catch (Exception e){
						log.error("Error delivering email:" + mail,e);
					}
				}else{
					throw new Exception("service is null");
				}
				bc.ungetService(serviceReference);
			} 
			
			if(delivered){
				log.info("DELIVERED "+mail);
				queueService.unstore(mail);
				queueService.logState();
			}else{
				log.info("Mail fail to deliver:" + mail);
				if(getProperties().getQueueRetries()<1 
						|| mail.getDeliverTries()<getProperties().getQueueRetries()){
					mail.setDeliverTries(mail.getDeliverTries()+1);
					queueService.delayAndPut(Mail.Phase.out, mail, mail.getDeliverTries()*getProperties().getQueueDelayTime());
					log.info("Mail added to queue again:" + mail);
				}else{
					log.info("Mail take out to later deliver:" + mail);
					queueService.saveToAndUnstore(mail, getProperties().getDeliveredErrorPath(), true);
				}
			}
		} catch (Exception e) {			
			log.error("Error sending mail to connector:" + mail + e.toString());
			log.debug("Error sending mail to connector:" + mail,e);
			queueService.saveToAndUnstore(mail,getProperties().getErrorFolder(), true);
			if (getLogStatService() != null) {
				getLogStatService().log(mail, getProperties().getConnectorErrorStat(),
						e.getMessage());
			}
		}
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

	public CoreProperties getProperties() {
		return properties;
	}

	public void setProperties(CoreProperties properties) {
		this.properties = properties;
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
