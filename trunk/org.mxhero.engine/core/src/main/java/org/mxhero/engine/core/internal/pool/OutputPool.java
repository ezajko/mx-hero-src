package org.mxhero.engine.core.internal.pool;

import java.util.Observable;
import java.util.Observer;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.core.internal.CoreProperties;
import org.mxhero.engine.core.internal.pool.task.DeliverTask;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a DeliverTask for each mail that enter in the outputQueue.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool implements Observer {
	
	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private BundleContext bc;
	
	private CoreProperties properties;
	
	private LogStat logStatService;
	
	private MimeMailQueueService queueService;
	
	/**
	 * @param bc
	 */
	public OutputPool(BundleContext bc, MimeMailQueueService queueService){
		super(Mail.Phase.out,queueService);
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
	protected Runnable createTask(MimeMail mail) {
		DeliverTask task = new DeliverTask(mail,bc,queueService);
		task.setProperties(getProperties());
		task.setLogStatService(getLogStatService());
		if(log.isDebugEnabled()){
			log.debug("Out Phase task created for " + mail);
		}
		return task;
		
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
	public CoreProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 */
	public void setProperties(CoreProperties properties) {
		this.properties = properties;
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
