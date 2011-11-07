package org.mxhero.engine.plugin.postfixconnector.internal.service;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.util.LogMail;
import org.mxhero.engine.plugin.postfixconnector.internal.ConnectorProperties;
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
	
	private ConnectorProperties properties;
	
	/**
	 * Implementation that adds mails to the OutputQueue.
	 * @see
	 * org.mxhero.engine.domain.connector.OutputService#addOutMail(org.mxhero
	 * .engine.domain.mail.Mail)
	 */
	public void addOutMail(MimeMail mail) {
		props = new Properties();
	    props.put("mail.smtp.host", getProperties().getMailSmtpHost());
	    props.put("mail.smtp.port", getProperties().getMailSmtpPort().toString());
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
			
			try{
				msg.saveChanges();
			}catch (Exception e){
				if(log.isDebugEnabled()){
					LogMail.saveErrorMail(msg, 
							getProperties().getErrorPrefix(),
							getProperties().getErrorSuffix(),
							getProperties().getErrorFolder());
				}
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
			throw new RuntimeException(e);
		}	

	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public ConnectorProperties getProperties() {
		return properties;
	}

	public void setProperties(ConnectorProperties properties) {
		this.properties = properties;
	}
	
}