package org.mxhero.engine.core.internal.pool;

import java.util.Collection;

import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.core.mail.filter.MailFilter;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.queue.QueueTaskPool;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a DeliverTask for each mail that enter in the outputQueue.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool implements PropertiesListener{

	public static final String MODULE="core";
	public static final String PHASE=RulePhase.OUT;
	
	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private BundleContext bc;
	
	private PropertiesService properties;
	
	private LogStat logStatService;
	
	private Collection<MailFilter> outFilters;
	
	private MimeMailQueueService queueService;
	
	/**
	 * @param bc
	 */
	public OutputPool(BundleContext bc, MimeMailQueueService queueService){
		super(MODULE,PHASE,queueService);
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
		DeliverTask task = new DeliverTask(mail,bc,queueService);
		task.setProperties(getProperties());
		task.setLogStatService(getLogStatService());
		task.setOutFilters(getOutFilters());
		if(log.isDebugEnabled()){
			log.debug("Out Phase task created for " + mail);
		}
		return task;
		
	}

	/**
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
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
	
}
