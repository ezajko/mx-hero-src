package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.queue.OutputQueue;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.pool.QueueTaskPool;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.domain.mail.MimeMail;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a DeliverTask for each mail that enter in the outputQueue.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool<MimeMail> implements PropertiesListener{

	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private BundleContext bc;
	
	private PropertiesService properties;
	
	private LogStat logStatService;
	
	/**
	 * @param bc
	 */
	public OutputPool(BundleContext bc){
		super(OutputQueue.getInstance());
		if (bc==null){
        	throw new IllegalArgumentException();
        }
		this.bc = bc;		
	}
	
	/**
	 * @see org.mxhero.engine.domain.pool.TaskPool#init()
	 */
	@Override
	protected void init() {
		log.info("INIT");
		updated();
		getProperties().addListener(this);
	}
	
	/**
	 * @see org.mxhero.engine.domain.pool.TaskPool#clean()
	 */
	@Override
	protected void clean() {
		log.info("CLEAN");
		getProperties().removeListener(this);
	}

	/**
	 * @see org.mxhero.engine.domain.pool.QueueTaskPool#createTask(java.lang.Object)
	 */
	@Override
	protected Runnable createTask(MimeMail mail) {
		DeliverTask task = new DeliverTask(mail,bc);
		task.setProperties(getProperties());
		task.setLogStatService(getLogStatService());
		return task;
		
	}

	/**
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		log.debug("Updating.");
		setCorePoolsize(getProperties().getValue(Core.OUTPUTPOOL_COREPOOLSIZE));
		setMaximumPoolSize(getProperties().getValue(Core.OUTPUTPOOL_MAXIMUMPOOLSIZE));
		setKeepAliveTime(getProperties().getValue(Core.OUTPUTPOOL_KEEPALIVETIME));
		setWaitTime(getProperties().getValue(Core.OUTPUTPOOL_QUEUE_WAIT_TIME));
	}

	/**
	 * @return the properties
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
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
	
}
