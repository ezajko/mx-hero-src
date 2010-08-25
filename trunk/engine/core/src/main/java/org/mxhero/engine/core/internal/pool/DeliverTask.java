package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.connector.OutputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
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

	/**
	 * @param mail
	 * @param bc
	 */
	public DeliverTask(MimeMail mail, BundleContext bc) {
		this.mail = mail;
		this.bc = bc;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		ServiceReference serviceReference;
		OutputService service;
		try {
			serviceReference = bc.getServiceReference(mail
					.getResponseServiceId());
			log.debug("Got this ServiceReference:" + serviceReference);
			if (serviceReference != null) {
				service = (OutputService) bc.getService(serviceReference);
				log.debug("Got this OutputService:" + service);
				if (service != null) {
					service.addOutMail(mail);
					log.debug("Mail sent to connector OutQueue:" + mail);
				}
				bc.ungetService(serviceReference);
				log
						.debug("UnGetting this ServiceReference:"
								+ serviceReference);
			} else {
				log.debug("Output Service was not found for mail:" + mail);
				if (getLogStatService() != null) {
					getLogStatService().log(mail, Core.CONNECTOR_ERROR_STAT,
							Core.CONNECTOR_NOT_FAUND_VALUE);
				}
			}
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail, Core.CONNECTOR_ERROR_STAT,
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

}
