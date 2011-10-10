package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.rules.BaseLoader;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.queue.QueueTaskPool;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates a task for each mail that enter in the inputQueue. This task is
 * different for each RulePhase. This task will execute rules over the mail.
 * 
 * @author mmarmol
 */
public final class ReceivePool extends QueueTaskPool implements
		PropertiesListener {

	public static final String PHASE=RulePhase.RECEIVE;
	
	private static Logger log = LoggerFactory.getLogger(ReceivePool.class);

	private UserFinder userFinderService;

	private RulesProcessor processor;

	private BaseLoader loader;

	private SessionFiller filler;

	private PropertiesService properties;

	private LogRecord logRecordService;

	private LogStat logStatService;
	
	private MimeMailQueueService queueService;
	
	private long delayTime = 10000;

	/**
	 * Creates the object and pass to super the queue.
	 */
	public ReceivePool(MimeMailQueueService queueService) {
		super(PHASE,queueService);
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
		loader.stop();
	}

	/**
	 * @see org.mxhero.engine.domain.pool.QueueTaskPool#createTask(java.lang.Object)
	 */
	@Override
	protected Runnable createTask(MimeMail object) {
		try{
			if(log.isDebugEnabled()){
				log.debug("Receive Phase for " + object);
			}
			if(loader.getBuilder().getBase()==null){
				final MimeMail mail = object;
				return new Runnable() {
					@Override
					public void run() {
						log.warn("Rule database is not ready " + mail);
						try {
							queueService.delayAndPut(PHASE, mail, delayTime);
						} catch (InterruptedException e1) {
							log.error("error while reEnqueue email:",e1);
							LogMail.saveErrorMail(mail.getMessage(), 
									getProperties().getValue(Core.ERROR_PREFIX),
									getProperties().getValue(Core.ERROR_SUFFIX),
									getProperties().getValue(Core.ERROR_DIRECTORY));
						}
					}
				};
			}
			else if (object.getPhase().equals(RulePhase.RECEIVE)) {
				RecipientRuleTask task = new RecipientRuleTask(loader.getBuilder().getBase(), object,
						userFinderService, this.queueService);
				task.setFiller(this.getFiller());
				task.setProcessor(this.getProcessor());
				task.setLogRecordService(getLogRecordService());
				task.setLogStatService(getLogStatService());
				task.setProperties(properties);
				task.setDelayTime(delayTime);
				return task;
			} else {
				final MimeMail mail = object;
				return new Runnable() {
					@Override
					public void run() {
						log.error("Phase does not exists for " + mail);
						LogMail.saveErrorMail(mail.getMessage(), 
								getProperties().getValue(Core.ERROR_PREFIX),
								getProperties().getValue(Core.ERROR_SUFFIX),
								getProperties().getValue(Core.ERROR_DIRECTORY));
					}
				};
			}
		}catch(Exception e){
			final MimeMail mail = object;
			return new Runnable() {
				@Override
				public void run() {
					log.error("Error while trying to do task for " + mail);
					try {
						queueService.put(PHASE, mail);
					} catch (InterruptedException e) {
						LogMail.saveErrorMail(mail.getMessage(), 
								getProperties().getValue(Core.ERROR_PREFIX),
								getProperties().getValue(Core.ERROR_SUFFIX),
								getProperties().getValue(Core.ERROR_DIRECTORY));
					}
				}
			};
		}
	}

	private void setDelayTime(String value){
		try{
			long parsedValue = Long.parseLong(value);
			if(parsedValue>1000){
				delayTime=parsedValue;
			}
		}catch (Exception e) {}
	}
	
	/**
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		setDelayTime(getProperties().getValue(Core.QUEUE_DELAY_TIME));
		setCorePoolsize(getProperties().getValue(Core.RECEIVEPOOL_COREPOOLSIZE));
		setMaximumPoolSize(getProperties().getValue(Core.RECEIVEPOOL_MAXIMUMPOOLSIZE));
		setKeepAliveTime(getProperties().getValue(Core.RECEIVEPOOL_KEEPALIVETIME));
		setWaitTime(getProperties().getValue(Core.RECEIVEPOOL_QUEUE_WAIT_TIME));
	}

	/**
	 * @return the processor
	 */
	public RulesProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor
	 *            the processor to set
	 */
	public void setProcessor(RulesProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @return the filler
	 */
	public SessionFiller getFiller() {
		return filler;
	}

	/**
	 * @param filler
	 *            the filler to set
	 */
	public void setFiller(SessionFiller filler) {
		this.filler = filler;
	}

	/**
	 * @return the userFinderService
	 */
	public UserFinder getUserFinderService() {
		return userFinderService;
	}

	/**
	 * @param userFinderService
	 *            the userFinderService to set
	 */
	public void setUserFinderService(UserFinder userFinderService) {
		this.userFinderService = userFinderService;
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
	 * @return the loader
	 */
	public BaseLoader getLoader() {
		return loader;
	}

	/**
	 * @param loader
	 *            the loader to set
	 */
	public void setLoader(BaseLoader loader) {
		this.loader = loader;
	}

	/**
	 * @return the logRecordService
	 */
	public LogRecord getLogRecordService() {
		return logRecordService;
	}

	/**
	 * @param logRecordService
	 *            the logRecordService to set
	 */
	public void setLogRecordService(LogRecord logRecordService) {
		this.logRecordService = logRecordService;
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

	public MimeMailQueueService getQueueService() {
		return queueService;
	}

	public void setQueueService(MimeMailQueueService queueService) {
		this.queueService = queueService;
	}

}