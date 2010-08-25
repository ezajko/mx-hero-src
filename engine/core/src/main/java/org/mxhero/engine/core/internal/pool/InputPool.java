package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.drools.KnowledgeBaseLoader;
import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.pool.spliter.Spliter;
import org.mxhero.engine.core.internal.queue.InputQueue;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.pool.QueueTaskPool;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
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
public final class InputPool extends QueueTaskPool<MimeMail> implements
		PropertiesListener {

	private static Logger log = LoggerFactory.getLogger(InputPool.class);

	private DomainFinder domainFinderService;

	private UserFinder userFinderService;

	private RulesProcessor processor;

	private KnowledgeBaseLoader loader;

	private Spliter spliter;

	private SessionFiller filler;

	private PropertiesService properties;

	private LogRecord logRecordService;

	private LogStat logStatService;

	/**
	 * Creates the object and pass to super the queue.
	 */
	public InputPool() {
		super(InputQueue.getInstance());
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
		if (object.getPhase().equals(RulePhase.SEND)) {
			SenderRuleTask task = new SenderRuleTask(loader.getBuilder()
					.getKnowledgeBase(), object, domainFinderService,
					userFinderService);
			task.setSpliter(this.getSpliter());
			task.setFiller(this.getFiller());
			task.setProcessor(this.getProcessor());
			task.setLogRecordService(getLogRecordService());
			task.setLogStatService(getLogStatService());
			task.setProperties(properties);
			return task;
		} else if (object.getPhase().equals(RulePhase.RECEIVE)) {
			RecipientRuleTask task = new RecipientRuleTask(loader.getBuilder()
					.getKnowledgeBase(), object, domainFinderService,
					userFinderService);
			task.setFiller(this.getFiller());
			task.setProcessor(this.getProcessor());
			task.setLogRecordService(getLogRecordService());
			task.setLogStatService(getLogStatService());
			task.setProperties(properties);
			return task;
		} else {
			final MimeMail mail = object;
			return new Runnable() {
				@Override
				public void run() {
					log.error("Phase does not exists for " + mail);
				}
			};
		}
	}

	/**
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		log.debug("Updating.");
		setCorePoolsize(getProperties().getValue(Core.INPUTPOOL_COREPOOLSIZE));
		setMaximumPoolSize(getProperties().getValue(
				Core.INPUTPOOL_MAXIMUMPOOLSIZE));
		setKeepAliveTime(getProperties().getValue(Core.INPUTPOOL_KEEPALIVETIME));
		setWaitTime(getProperties().getValue(Core.INPUTPOOL_QUEUE_WAIT_TIME));
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
	 * @return the spliter
	 */
	public Spliter getSpliter() {
		return spliter;
	}

	/**
	 * @param spliter
	 *            the spliter to set
	 */
	public void setSpliter(Spliter spliter) {
		this.spliter = spliter;
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
	 * @return the domainFinderService
	 */
	public DomainFinder getDomainFinderService() {
		return domainFinderService;
	}

	/**
	 * @param domainFinderService
	 *            the domainFinderService to set
	 */
	public void setDomainFinderService(DomainFinder domainFinderService) {
		this.domainFinderService = domainFinderService;
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

}
