package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.mailster.smtp.api.SessionContext;
import org.mailster.smtp.api.listener.MessageListener;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.internal.util.StreamUtils;
import org.mxhero.engine.plugin.postfixconnector.queue.InputQueue;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 * This class just takes the mails and added it to the queue creating a copy of the Stream.
 */
public final class SMTPMessageListener implements MessageListener{

	private static Logger log = LoggerFactory.getLogger(SMTPMessageListener.class);
	
	private BlockingQueue<MimeMail> queue = InputQueue.getInstance();
	
	private LogRecord logRecordService;
	
	private LogStat logStatService;
	
	private PropertiesService properties;
	
	private SimpleDateFormat format;
	
	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#accept(org.mailster.smtp.api.SessionContext, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean accept(SessionContext arg0, String arg1, String arg2) {
		/*accepts everything so it can be enqueued for processing*/
		return true;
	}

	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#deliver(org.mailster.smtp.api.SessionContext, java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public void deliver(SessionContext ctx, String from, String recipient,
			InputStream data) throws IOException {
		
		MimeMail mail;
		try {
			Collection<String> originalRcpts = Arrays.asList(recipient.split(CustomDeliveryHandler.SPLIT_CHAR));
			mail = new MimeMail(from,originalRcpts,StreamUtils.getBytes(data),PostFixConnectorOutputService.class.getName());
			mail.getMessage().setSender(new InternetAddress(from));
			log.debug("recipients:");
		} catch (MessagingException e1) {
			throw new IOException(e1);
		}
		log.debug("Mail received:"+mail);
		try {
			if(getLogRecordService()!=null){
				getLogRecordService().log(mail);
			}
			if(getLogStatService()!=null){
				getLogStatService().log(mail, getProperties().getValue(PostfixConnector.IN_TIME_STAT), getFormat().format(Calendar.getInstance().getTime()));
			}		
			queue.put(mail);
			log.debug("Mail added to queue:"+mail);
		} catch (InterruptedException e) {
			log.debug("Mail interrupted while adding to queue:"+mail,e);
		}
	}

	/**
	 * @return the logRecordService
	 */
	public LogRecord getLogRecordService() {
		return logRecordService;
	}

	/**
	 * @param logRecordService the logRecordService to set
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
	 * @param logStatService the logStatService to set
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
	 * @param properties the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		if(format==null){
			format = new SimpleDateFormat(getProperties().getValue(PostfixConnector.STATS_TIME_FORMAT));
		}
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

}
