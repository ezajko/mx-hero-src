package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.queue.QueueTaskPool;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.core.internal.CoreProperties;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a DeliverTask for each mail that enter in the outputQueue.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool {

	public static final String PHASE=RulePhase.OUT;
	
	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private BundleContext bc;
	
	private CoreProperties properties;
	
	private LogStat logStatService;
	
	private MimeMailQueueService queueService;
	
	/**
	 * @param bc
	 */
	public OutputPool(BundleContext bc, MimeMailQueueService queueService){
		super(PHASE,queueService);
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
	}
	
	/**
	 * @see org.mxhero.engine.domain.pool.TaskPool#clean()
	 */
	@Override
	protected void clean() {
		log.info("CLEAN");
	}

	/**
	 * @see org.mxhero.engine.domain.pool.QueueTaskPool#createTask(java.lang.Object)
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

}
