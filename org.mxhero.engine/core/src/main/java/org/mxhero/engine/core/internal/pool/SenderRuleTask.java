package org.mxhero.engine.core.internal.pool;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.queue.MimeMailQueueService;
import org.mxhero.engine.commons.rules.RuleBase;
import org.mxhero.engine.commons.statistic.LogRecord;
import org.mxhero.engine.commons.statistic.LogStat;
import org.mxhero.engine.commons.util.LogMail;
import org.mxhero.engine.core.internal.CoreProperties;
import org.mxhero.engine.core.internal.pool.processor.RulesProcessor;
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

	private RulesProcessor processor;

	private LogRecord logRecordService;

	private LogStat logStatService;

	private CoreProperties properties;

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
	public SenderRuleTask(RuleBase base, MimeMail mail, MimeMailQueueService queueService) {
		this.mail = mail;
		this.base = base;
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
			this.processor.process(base, mail.getBussinesObject());
		} catch (Exception e) {
			if (getLogStatService() != null) {
				getLogStatService().log(mail,
						getProperties().getProcessErrorStat(),
						e.toString());
			}
			log.error("error while processing rules:"+ e.toString());
			log.debug("error while processing rules", e);
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
				queueService.delayAndPut(SendPool.PHASE, mail, getProperties().getQueueDelayTime());
				log.info("REQUEUED "+mail);
			} else if (mail.getStatus().equalsIgnoreCase(MailState.DROP)) {
				queueService.unstore(mail);
				log.info("DROPPED "+mail);
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
						getProperties().getErrorPrefix(),
						getProperties().getErrorSuffix(),
						getProperties().getErrorFolder());
			}
		}
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
	public CoreProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(CoreProperties properties) {
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
