package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.drools.KnowledgeBaseLoader;
import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
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
public final class SendPool extends QueueTaskPool implements
		PropertiesListener {

	public static final String MODULE="core";
	public static final String PHASE=RulePhase.SEND;
	
	private static Logger log = LoggerFactory.getLogger(SendPool.class);

	private UserFinder userFinderService;

	private RulesProcessor processor;

	private KnowledgeBaseLoader loader;

	private SessionFiller filler;

	private PropertiesService properties;

	private LogRecord logRecordService;

	private LogStat logStatService;
	
	private MimeMailQueueService queueService;

	/**
	 * Creates the object and pass to super the queue.
	 */
	public SendPool(MimeMailQueueService queueService) {
		super(MODULE,PHASE,queueService);
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
			if(loader.getBuilder().getKnowledgeBase()==null){
				final MimeMail mail = object;
				return new Runnable() {
					@Override
					public void run() {
						log.warn("Rule database is not ready " + mail);
						try {
							queueService.reEnqueue(MODULE, PHASE, mail);
						} catch (InterruptedException e1) {
							log.error("error while reEnqueue email:",e1);
							LogMail.saveErrorMail(mail.getMessage(), 
									getProperties().getValue(Core.ERROR_PREFIX),
									getProperties().getValue(Core.ERROR_SUFFIX),
									getProperties().getValue(Core.ERROR_DIRECTORY));
							boolean removed = false;
							while(!removed){
								try {
									removed = queueService.remove(MODULE, PHASE, mail);
								} catch (InterruptedException e2) {
									log.error("error while removing email:",e2);
								}
							}
						}
					}
				};
			}
			else if (object.getPhase().equals(RulePhase.SEND)) {
				SenderRuleTask task = new SenderRuleTask(loader.getBuilder()
						.getKnowledgeBase().newStatefulKnowledgeSession(), object,
						userFinderService,queueService);
				task.setFiller(this.getFiller());
				task.setProcessor(this.getProcessor());
				task.setLogRecordService(getLogRecordService());
				task.setLogStatService(getLogStatService());
				task.setProperties(properties);
				return task;
			}  else {
				final MimeMail mail = object;
				return new Runnable() {
					@Override
					public void run() {
						log.error("Phase does not exists for " + mail);
						LogMail.saveErrorMail(mail.getMessage(), 
								getProperties().getValue(Core.ERROR_PREFIX),
								getProperties().getValue(Core.ERROR_SUFFIX),
								getProperties().getValue(Core.ERROR_DIRECTORY));
						boolean removed = false;
						while(!removed){
							try {
								removed = queueService.remove(MODULE, PHASE, mail);
							} catch (InterruptedException e1) {
								log.error("error while removing email:",e1);
							}
						}
					}
				};
			}
		}catch(Exception e){
			final MimeMail mail = object;
			return new Runnable() {
				@Override
				public void run() {
					log.error("Error while trying to do task for " + mail);
					LogMail.saveErrorMail(mail.getMessage(), 
							getProperties().getValue(Core.ERROR_PREFIX),
							getProperties().getValue(Core.ERROR_SUFFIX),
							getProperties().getValue(Core.ERROR_DIRECTORY));
					boolean removed = false;
					while(!removed){
						try {
							removed = queueService.remove(MODULE, PHASE, mail);
						} catch (InterruptedException e1) {
							log.error("error while removing email:",e1);
						}
					}
				}
			};
		}
	}

	/**
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		setCorePoolsize(getProperties().getValue(Core.SENDPOOL_COREPOOLSIZE));
		setMaximumPoolSize(getProperties().getValue(
				Core.SENDPOOL_MAXIMUMPOOLSIZE));
		setKeepAliveTime(getProperties().getValue(Core.SENDPOOL_KEEPALIVETIME));
		setWaitTime(getProperties().getValue(Core.SENDPOOL_QUEUE_WAIT_TIME));
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
	public KnowledgeBaseLoader getLoader() {
		return loader;
	}

	/**
	 * @param loader
	 *            the loader to set
	 */
	public void setLoader(KnowledgeBaseLoader loader) {
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
