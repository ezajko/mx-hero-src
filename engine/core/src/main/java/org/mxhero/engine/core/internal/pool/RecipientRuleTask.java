package org.mxhero.engine.core.internal.pool;

import org.drools.KnowledgeBase;
import org.drools.runtime.StatefulKnowledgeSession;
import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.queue.OutputQueue;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This task is in charge of processing mail that are in the receive phase of
 * the rules.
 * 
 * @author mmarmol
 */
public final class RecipientRuleTask implements Runnable {

	private static Logger log = LoggerFactory
			.getLogger(RecipientRuleTask.class);

	private StatefulKnowledgeSession ksession;

	private DomainFinder domainFinderService;

	private UserFinder userFinderService;

	private MimeMail mail;

	private SessionFiller filler;

	private RulesProcessor processor;

	private PropertiesService properties;

	private LogRecord logRecordService;

	private LogStat logStatService;

	/**
	 * Creates the object
	 * 
	 * @param base
	 *            base where the rule are
	 * @param mail
	 *            mail to be processed
	 * @param domainFinderService
	 *            service to find the mail domain
	 * @param userFinderService
	 *            service to find the mail user in this case recipient
	 */
	public RecipientRuleTask(KnowledgeBase base, MimeMail mail,
			DomainFinder domainFinderService, UserFinder userFinderService) {
		this.mail = mail;
		this.ksession = base.newStatefulKnowledgeSession();
		this.domainFinderService = domainFinderService;
		this.userFinderService = userFinderService;
	}

	/**
	 * Logs the mail and call the RulesProcessor, if an error occurs it will try
	 * to add a stat.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (getLogRecordService() != null) {
			getLogRecordService().log(mail);
		}
		try {
			this.processor.process(ksession, filler, userFinderService,
					domainFinderService, mail);
			if (!mail.getStatus().equals(MailState.DROP)) {
				OutputQueue.getInstance().add(mail);
				log.debug("Mail sent to out queue for " + mail);
			} else {
				if(getLogRecordService()!=null){
					getLogRecordService().log(mail);
				}
				log.debug("mail droped " + mail);
			}
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getValue(Core.PROCESS_ERROR_STAT),
						e.getMessage());
			}
			log.error("error while processing rules:",e);
		}
	}

	/**
	 * @param filler
	 */
	public void setFiller(SessionFiller filler) {
		this.filler = filler;
	}

	/**
	 * @param processor
	 */
	public void setProcessor(RulesProcessor processor) {
		this.processor = processor;
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
