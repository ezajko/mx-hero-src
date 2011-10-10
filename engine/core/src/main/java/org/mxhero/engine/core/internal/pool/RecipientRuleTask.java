package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.core.internal.pool.filler.SessionFiller;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.domain.rules.RuleBase;
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

	private RuleBase base;

	private UserFinder userFinderService;

	private MimeMail mail;

	private SessionFiller filler;

	private RulesProcessor processor;

	private PropertiesService properties;

	private LogRecord logRecordService;

	private LogStat logStatService;
	
	private MimeMailQueueService queueService;
	
	private long delayTime = 10000;

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
	public RecipientRuleTask(RuleBase base, MimeMail mail, UserFinder userFinderService, MimeMailQueueService queueService) {
		this.mail = mail;
		this.base = base;
		this.userFinderService = userFinderService;
		this.queueService = queueService;
	}

	/**
	 * Logs the mail and call the RulesProcessor, if an error occurs it will try
	 * to add a stat.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			this.processor.process(base, filler, userFinderService, mail);
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getValue(Core.PROCESS_ERROR_STAT),
						e.getMessage());
			}
			log.error("error while processing rules:",e);
		}
		try{
			try{
				if (getLogRecordService() != null) {
					getLogRecordService().log(mail);
				}
			}catch(Exception e){
				log.error("error while saving stats",e);
			}
			
			if (mail.getStatus().equals(MailState.DELIVER)) {
				mail.getMessage().saveChanges();
				queueService.put(OutputPool.PHASE, mail);
			} else if(mail.getStatus().equals(MailState.REQUEUE)){
				mail.getMessage().saveChanges();
				mail.setStatus(MailState.DELIVER);
				queueService.delayAndPut(ReceivePool.PHASE, mail, delayTime);
			} else if(mail.getStatus().equals(MailState.DROP)) {
				queueService.unstore(mail);
			}
		} catch (Exception e) {
			log.error("error while sending email to next phase:",e);
			LogMail.saveErrorMail(mail.getMessage(), 
					getProperties().getValue(Core.ERROR_PREFIX),
					getProperties().getValue(Core.ERROR_SUFFIX),
					getProperties().getValue(Core.ERROR_DIRECTORY));
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getValue(Core.PROCESS_ERROR_STAT),
						e.getMessage());
			}
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

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

}