package org.mxhero.engine.core.internal.pool;

import java.util.concurrent.TimeUnit;

import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.rules.RuleBase;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This task is in charge of processing mail that are in the send phase of the
 * rules.
 * 
 * @author mmarmol
 */
public final class SenderRuleTask implements Runnable {

	private static Logger log = LoggerFactory.getLogger(SenderRuleTask.class);

	private RuleBase base;

	private MimeMail mail;

	private UserFinder userFinderService;

	private RulesProcessor processor;

	private SessionFiller filler;

	private LogRecord logRecordService;

	private LogStat logStatService;

	private PropertiesService properties;
	
	private MimeMailQueueService queueService;

	/**
	 * Creates the object.
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
	public SenderRuleTask(RuleBase base, MimeMail mail, UserFinder userFinderService,MimeMailQueueService queueService) {
		this.mail = mail;
		this.base = base;
		this.userFinderService = userFinderService;
		this.queueService = queueService;
	}

	/**
	 * Logs the mail and call the RulesProcessor and finally call the Spliter.
	 * if an error occurs it will try to add a stat.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			processor.process(base, filler, userFinderService, mail);
			}catch (Exception e) {
				if (getLogStatService() != null) {
					getLogStatService().log(mail,
							getProperties().getValue(Core.PROCESS_ERROR_STAT),
							e.getMessage());
				}
				log.error("error while processing rules:",e);
			}
			
		try{
			/*
			 * added again into input queue so it gets called for recipient
			 * processing
			 */
			if(getLogRecordService()!=null){
				getLogRecordService().log(mail);
			}
			if (mail.getStatus().equals(MailState.DELIVER)) {
				mail.getMessage().saveChanges();
				mail.setPhase(RulePhase.RECEIVE);
				this.queueService.offer(ReceivePool.PHASE, mail, 1000, TimeUnit.MILLISECONDS);
			} else if(mail.getStatus().equals(MailState.REQUEUE)){
				mail.getMessage().saveChanges();
				mail.setStatus(MailState.DELIVER);
				this.queueService.offer(SendPool.PHASE, mail, 1000, TimeUnit.MILLISECONDS);
			} else if(mail.getStatus().equals(MailState.DROP)) {
				queueService.unstore(mail);
			}
		} catch (Exception e) {
			log.error("error, saving mail to disk:",e);
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getValue(Core.PROCESS_ERROR_STAT),
						e.getMessage());
			}
			LogMail.saveErrorMail(mail.getMessage(), 
					getProperties().getValue(Core.ERROR_PREFIX),
					getProperties().getValue(Core.ERROR_SUFFIX),
					getProperties().getValue(Core.ERROR_DIRECTORY));
			log.error("error while sending email to next phase:",e);
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

}
