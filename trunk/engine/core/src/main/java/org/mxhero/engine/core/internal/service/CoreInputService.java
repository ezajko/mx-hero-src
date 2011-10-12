package org.mxhero.engine.core.internal.service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.core.mail.filter.MailFilter;
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.connector.QueueFullException;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation takes the parameters and creates a new Mail
 * assigning a sequence and adding the requirement into the input queue.
 * @author mmarmol
 * @see org.mxhero.engine.domain.connector.InputService
 */
public final class CoreInputService implements InputService {

	private static Logger log = LoggerFactory.getLogger(CoreInputService.class);
	
	private static final long WAIT_TIME = 5000;
	
	private MimeMailQueueService queueService;
	
	private Collection<MailFilter> inFilters;

	private PropertiesService properties;
	
	/**
	 * @see org.mxhero.engine.domain.connector.InputService#addMail(byte[], java.lang.String)
	 */
	public void addMail(MimeMail mail) throws QueueFullException{
		boolean added = false;
		if (mail==null || mail.getResponseServiceId()==null){
			throw new IllegalArgumentException("mail:"+mail);
		}
		if(getInFilters()!=null){
			for(MailFilter filter : getInFilters()){
				filter.process(mail);
			}
		}
		
		try {
			added = queueService.store( mail.getPhase(), mail,WAIT_TIME, TimeUnit.MILLISECONDS );
		} catch (InterruptedException e) {
			log.error("interrupted while waiting:"+mail,e);
		}
		if(!added){
			throw new QueueFullException();
		}
		log.info("STORED "+mail);
		queueService.logState();
	}

	public Collection<MailFilter> getInFilters() {
		return inFilters;
	}

	public void setInFilters(Collection<MailFilter> inFilters) {
		this.inFilters = inFilters;
	}

	public MimeMailQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(MimeMailQueueService queueService) {
		this.queueService = queueService;
	}

	public PropertiesService getProperties() {
		return properties;
	}

	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}
