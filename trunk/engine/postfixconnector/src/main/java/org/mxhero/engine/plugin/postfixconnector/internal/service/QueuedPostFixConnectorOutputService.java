package org.mxhero.engine.plugin.postfixconnector.internal.service;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the OutputService that just adds the mail
 * to the output queue.
 * @author mmarmol
 */
public final class QueuedPostFixConnectorOutputService implements PostFixConnectorOutputService {


	private static Logger log = LoggerFactory.getLogger(QueuedPostFixConnectorOutputService.class);

	private Properties props;
	
	private PropertiesService properties;
	
	private LogStat logStat;
	
	/**
	 * Implementation that adds mails to the OutputQueue.
	 * @see
	 * org.mxhero.engine.domain.connector.OutputService#addOutMail(org.mxhero
	 * .engine.domain.mail.Mail)
	 */
	public void addOutMail(MimeMail mail) {
		props = new Properties();
	    props.put("mail.smtp.host", properties.getValue(PostfixConnector.SMTP_HOST_NAME, "localhost"));
	    props.put("mail.smtp.port", properties.getValue(PostfixConnector.SMTP_HOST_PORT, "5556"));
	    props.put("mail.mime.address.strict","false");
		
		log.debug("Email received:" + mail);

		String from = (mail.getInitialSender()!=null && mail.getInitialSender().trim().length()>0)?mail.getInitialSender():"<>";
		props.put("mail.smtp.from", from);
		Session session = Session.getInstance(props);
	    MimeMessage msg = null;
		try {
			msg = mail.getMessage();
			if(from!="<>"){
				msg.setSender(new InternetAddress(from));
			}else{
				msg.removeHeader("Sender");
			}
			msg.removeHeader(getProperties().getValue(PostfixConnector.SENDER_HEADER, PostfixConnector.DEFAULT_SENDER_HEADER));
			msg.removeHeader(getProperties().getValue(PostfixConnector.RECIPIENT_HEADER, PostfixConnector.DEFAULT_RECIPIENT_HEADER));
			if(getProperties()!=null 
					&& getProperties().getValue(PostfixConnector.ADD_HEADERS)!=null 
					&& Boolean.parseBoolean(getProperties().getValue(PostfixConnector.ADD_HEADERS))){
				msg.addHeader(getProperties().getValue(PostfixConnector.SENDER_HEADER, PostfixConnector.DEFAULT_SENDER_HEADER), from);
				msg.addHeader(getProperties().getValue(PostfixConnector.RECIPIENT_HEADER, PostfixConnector.DEFAULT_RECIPIENT_HEADER), mail.getRecipient());
			}
			
			try{
				msg.saveChanges();
			}catch (Exception e){
				LogMail.saveErrorMail(msg, 
						getProperties().getValue(PostfixConnector.ERROR_PREFIX),
						getProperties().getValue(PostfixConnector.ERROR_SUFFIX),
						getProperties().getValue(PostfixConnector.ERROR_DIRECTORY));
				throw new RuntimeException(e);
			}
			
		    Transport t = session.getTransport("smtp");
		    t.connect();
		    InternetAddress recipient=null;
		    try{
		    	recipient = new InternetAddress(mail.getRecipient());
		    }catch(AddressException e){
		    	recipient = new InternetAddress(mail.getRecipient(),null,"utf-8");
		    }
		    t.sendMessage(msg, new InternetAddress[] { recipient });
		    t.close();
		    if(log.isDebugEnabled()){
		    	log.debug("Message sent:"+mail);
		    }

		} catch (Exception e) {
			log.error("Couldnt send the mail:"+mail,e);
			if(getLogStat()!=null){
				getLogStat().log(mail, getProperties().getValue(PostfixConnector.DELIVER_ERROR_STAT), e.getMessage());
			}
			throw new RuntimeException(e);
		}	

	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public PropertiesService getProperties() {
		return properties;
	}

	public void setProperties(PropertiesService properties) {
		this.properties = properties;
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
}
