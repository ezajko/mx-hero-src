package org.mxhero.engine.core.internal.pool;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.core.mail.filter.MailFilter;
import org.mxhero.engine.domain.connector.OutputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.log.LogMail;
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
	private SimpleDateFormat format;
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
					mail.getMessage().saveChanges();
					service.addOutMail(mail);
					log.info("Mail sent using outputservice:" + mail);
					boolean removed = false;
					while(!removed){
						try {
							removed = queueService.remove(OutputPool.MODULE, OutputPool.PHASE, mail);
						} catch (InterruptedException e1) {
							log.error("error while removing email:",e1);
						}
					}
					log.info(queueService.getQueuesCount().toString());
					if(log.isDebugEnabled()){
						queueService.logState();
						}
				}else{
					throw new Exception("service is null");
				}
				bc.ungetService(serviceReference);
			} else {
				log.warn("Output Service was not found for mail:" + mail);
				if (getLogStatService() != null) {
					getLogStatService().log(mail, 
							properties.getValue(Core.CONNECTOR_ERROR_STAT),
							properties.getValue(Core.CONNECTOR_NOT_FAUND_VALUE));
				}
				queueService.reEnqueue(OutputPool.MODULE, OutputPool.PHASE, mail);
			}
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail, properties.getValue(Core.CONNECTOR_ERROR_STAT),
						e.getMessage());
			}
			log.debug("Error sending mail to connector:" + mail,e);
			try {
				queueService.reEnqueue(OutputPool.MODULE, OutputPool.PHASE, mail);
			} catch (InterruptedException e2) {
				LogMail.saveErrorMail(mail.getMessage(), 
						getProperties().getValue(Core.ERROR_PREFIX),
						getProperties().getValue(Core.ERROR_SUFFIX),
						getProperties().getValue(Core.ERROR_DIRECTORY));
				boolean removed = false;
				while(!removed){
					try {
						removed = queueService.remove(OutputPool.MODULE, OutputPool.PHASE, mail);
					} catch (InterruptedException e1) {
						log.error("error while removing email:",e1);
					}
				}
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

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}
}
