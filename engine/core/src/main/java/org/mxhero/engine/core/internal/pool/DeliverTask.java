package org.mxhero.engine.core.internal.pool;

import java.util.Collection;

import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.core.mail.filter.MailFilter;
import org.mxhero.engine.domain.connector.OutputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.statistic.LogStat;
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
	private PropertiesService properties;
	private Collection<MailFilter> outFilters;
	private MimeMailQueueService queueService;
	private long delayTime = 10000;
	private int retries = 10;
	private String path;

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
		ServiceReference serviceReference;
		OutputService service;
		boolean delivered = false;
		try {
			if(getOutFilters()!=null){
				for(MailFilter filter : getOutFilters()){
					filter.process(mail);
				}
			}
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
				if(retries<0 || mail.getDeliverTries()<retries){
					mail.setDeliverTries(mail.getDeliverTries()+1);
					queueService.delayAndPut(RulePhase.OUT, mail, mail.getDeliverTries()*delayTime);
					log.info("Mail added to queue again:" + mail);
				}else{
					log.info("Mail take out to later deliver:" + mail);
					queueService.saveToAndUnstore(mail, path, true);
				}
			}
		} catch (Exception e) {			
			log.debug("Error sending mail to connector:" + mail,e);
			log.error("Error sending mail to connector:" + mail + e.toString());
			queueService.saveToAndUnstore(mail, getProperties().getValue(Core.ERROR_DIRECTORY), true);
			if (getLogStatService() != null) {
				getLogStatService().log(mail, properties.getValue(Core.CONNECTOR_ERROR_STAT),
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

	/**
	 * @return the properties
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

	/**
	 * @return
	 */
	public Collection<MailFilter> getOutFilters() {
		return outFilters;
	}

	/**
	 * @param outFilters
	 */
	public void setOutFilters(Collection<MailFilter> outFilters) {
		this.outFilters = outFilters;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
