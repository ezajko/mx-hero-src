package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.commons.connector.OutputService;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.RulePhase;
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
					queueService.delayAndPut(RulePhase.OUT, mail, mail.getDeliverTries()*getProperties().getQueueDelayTime());
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

}
