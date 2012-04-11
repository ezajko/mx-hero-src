package org.mxhero.engine.core.internal.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.mxhero.engine.commons.connector.InputServiceFilter;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.finders.UserFinder;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.core.internal.CoreProperties;
import org.mxhero.engine.core.internal.filler.SessionFiller;
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
	
	private CoreProperties properties;
	
	private SessionFiller filler;
	
	private UserFinder userFinderService;
	
	@SuppressWarnings("rawtypes")
	private List filters;
	
	/**
	 * @see org.mxhero.engine.domain.connector.InputService#addMail(byte[], java.lang.String)
	 */
	public void addMail(MimeMail mail) throws QueueFullException{
		MimeMail addedMail = null;
		if (mail==null || mail.getResponseServiceId()==null){
			throw new IllegalArgumentException("mail:"+mail);
		}
		if(mail.getBussinesObject()==null){
			this.getFiller().fill(getUserFinderService(), mail);
		}
		if(filters!=null){
			for(Object filter : filters){
				if(filter instanceof InputServiceFilter){
					try{
						((InputServiceFilter) filter).dofilter(mail);
						log.debug("doing filter "+filter.getClass().getName());
					}catch(Exception e){
						log.warn("error while doing filter "+filter.getClass().getCanonicalName(),e);
					}
				}
			}
		}
		try {
			addedMail = queueService.store( mail.getPhase(), mail, WAIT_TIME, TimeUnit.MILLISECONDS );
		} catch (InterruptedException e) {
			log.error("interrupted while waiting:"+mail,e);
		}
		if(addedMail==null){
			throw new QueueFullException();
		}

		log.info("STORED "+addedMail);
		queueService.logState();
	}

	public MimeMailQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(MimeMailQueueService queueService) {
		this.queueService = queueService;
	}

	public CoreProperties getProperties() {
		return properties;
	}

	public void setProperties(CoreProperties properties) {
		this.properties = properties;
	}

	public SessionFiller getFiller() {
		return filler;
	}

	public void setFiller(SessionFiller filler) {
		this.filler = filler;
	}

	public UserFinder getUserFinderService() {
		return userFinderService;
	}

	public void setUserFinderService(UserFinder userFinderService) {
		this.userFinderService = userFinderService;
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
