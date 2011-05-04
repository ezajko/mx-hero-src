package org.mxhero.engine.plugin.postfixconnector.internal.pool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.internal.util.LogMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class used to send mail with javamail api outside the engine.
 * @author mmarmol
 */
public final class SendMailTask implements Runnable {
	
	private static Logger log = LoggerFactory.getLogger(SendMailTask.class);
	
	private MimeMail mail;
	
	private Properties props;
	
	private LogStat logStat;
	
	private SimpleDateFormat format;
	
	private PropertiesService propertiesService;
	/**
	 * Creates the Task.
	 * @param mail contains the mail to be sent.
	 * @param props mail.smtp.host and mail.smtp.port for javamail.
	 */
	public SendMailTask(MimeMail mail, Properties props){
		this.mail = mail;
		this.props = new Properties(props);
	}
	
	/**
	 * Just sent the mail and exits.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		props.put("mail.user", mail.getInitialSender());
		Session session = Session.getInstance(props);
	    MimeMessage msg = null;
		try {
			msg = mail.getMessage();
			msg.setSender(new InternetAddress(mail.getInitialSender()));
			msg.removeHeader(getPropertiesService().getValue(PostfixConnector.SENDER_HEADER, PostfixConnector.DEFAULT_SENDER_HEADER));
			msg.removeHeader(getPropertiesService().getValue(PostfixConnector.RECIPIENT_HEADER, PostfixConnector.DEFAULT_RECIPIENT_HEADER));
			if(getPropertiesService()!=null 
					&& getPropertiesService().getValue(PostfixConnector.ADD_HEADERS)!=null 
					&& Boolean.parseBoolean(getPropertiesService().getValue(PostfixConnector.ADD_HEADERS))){
				msg.addHeader(getPropertiesService().getValue(PostfixConnector.SENDER_HEADER, PostfixConnector.DEFAULT_SENDER_HEADER), mail.getInitialSender());
				msg.addHeader(getPropertiesService().getValue(PostfixConnector.RECIPIENT_HEADER, PostfixConnector.DEFAULT_RECIPIENT_HEADER), mail.getRecipient());
			}
			
			msg.saveChanges();
			
		    Transport t = session.getTransport("smtp");
		    t.connect();
		    log.debug(mail.getRecipient());
		    t.sendMessage(msg, new InternetAddress[] { new InternetAddress(mail.getRecipient()) });
		    t.close();
		    log.debug("Message sent:"+mail);
		    getLogStat().log(mail, getPropertiesService().getValue(PostfixConnector.OUT_TIME_STAT), getFormat().format(Calendar.getInstance().getTime()));
		} catch (MessagingException e) {
			getLogStat().log(mail, getPropertiesService().getValue(PostfixConnector.DELIVER_ERROR_STAT), e.getMessage());
			log.error("Couldnt send the mail:"+mail,e);
			LogMail.saveErrorMail(msg, 
					getPropertiesService().getValue(PostfixConnector.ERROR_PREFIX),
					getPropertiesService().getValue(PostfixConnector.ERROR_SUFFIX));
		}	
	}

	/**
	 * @return the logStat
	 */
	public LogStat getLogStat() {
		return logStat;
	}

	/**
	 * @param logStat the logStat to set
	 */
	public void setLogStat(LogStat logStat) {
		this.logStat = logStat;
	}

	/**
	 * @return the propertiesService
	 */
	public PropertiesService getPropertiesService() {
		return propertiesService;
	}

	/**
	 * @param propertiesService the propertiesService to set
	 */
	public void setPropertiesService(PropertiesService propertiesService) {
		this.propertiesService = propertiesService;
	}

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

}

