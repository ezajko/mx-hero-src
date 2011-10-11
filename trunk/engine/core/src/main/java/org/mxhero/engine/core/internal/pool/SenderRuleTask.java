package org.mxhero.engine.core.internal.pool;

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

	private long delayTime = 10000;

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
	public SenderRuleTask(RuleBase base, MimeMail mail,
			UserFinder userFinderService, MimeMailQueueService queueService) {
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
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getValue(Core.PROCESS_ERROR_STAT),e.getMessage());
			}
			log.error("error while processing rules:"+ e.toString());
		}

		try {
			/*
			 * added again into input queue so it gets called for recipient
			 * processing
			 */
			try{
				if (getLogRecordService() != null) {
					getLogRecordService().log(mail);
				}
			}catch(Exception e){
				log.error("error while saving stats"+e.toString());
			}
			
			if (mail.getStatus().equalsIgnoreCase(MailState.REQUEUE)) {
				mail.setStatus(MailState.DELIVER);
				queueService.delayAndPut(SendPool.PHASE, mail, delayTime);
			} else if (mail.getStatus().equalsIgnoreCase(MailState.DROP)) {
				queueService.unstore(mail);
			} else  {
				mail.setStatus(MailState.DELIVER);
				mail.getMessage().saveChanges();
				mail.setPhase(RulePhase.RECEIVE);
				queueService.put(ReceivePool.PHASE, mail);
			}
		} catch (Exception e) {
			log.error("error while sending email to next phase:"+e.toString());
			if(log.isDebugEnabled()){
				LogMail.saveErrorMail(mail.getMessage(), 
						getProperties().getValue(Core.ERROR_PREFIX),
						getProperties().getValue(Core.ERROR_SUFFIX),
						null);
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

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

}
