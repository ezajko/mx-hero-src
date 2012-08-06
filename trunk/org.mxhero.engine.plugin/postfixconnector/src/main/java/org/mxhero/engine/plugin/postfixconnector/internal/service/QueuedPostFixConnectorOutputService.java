package org.mxhero.engine.plugin.postfixconnector.internal.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mailster.smtp.command.impl.MailCommand;
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
		boolean hasDsn = false;
		props = new Properties();
	    props.put("mail.smtp.host", getProperties().getMailSmtpHost());
	    props.put("mail.smtp.port", getProperties().getMailSmtpPort().toString());
	    props.put("mail.mime.address.strict","false");
		
		log.debug("Email received:" + mail);

		String from = (mail.getSender()!=null && mail.getSender().trim().length()>0)?mail.getSender():"<>";
		props.put("mail.smtp.from", from);
	    MimeMessage msg = null;
		try {
			msg = mail.getMessage();
			if(from!="<>"){
				msg.setSender(new InternetAddress(from));
			}else{
				msg.removeHeader("Sender");
			}
			String[] notifyHeaders = msg.getHeader(ConnectorProperties.NOTIFY_HEADER);
			String[] id = msg.getHeader(ConnectorProperties.ID_HEADER);

			String retValue = null;
			String notifyValue = null;

			if(id!=null 
					&& id.length>0 
					&& id[0]!=null 
					&& !id[0].trim().isEmpty()
					&& notifyHeaders!=null 
					&& notifyHeaders.length>0 
					&& notifyHeaders[0]!=null 
					&& !notifyHeaders[0].trim().isEmpty()){
				String mailId = mail.getTime().getTime()+"-"+mail.getSequence();
				if(mailId.trim().equalsIgnoreCase(id[0].trim())){
					notifyValue=notifyHeaders[0].trim();
					if(!Pattern.compile("^\\s*DELAY", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()
							&& !Pattern.compile("^\\s*FALIURE", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()
							&& !Pattern.compile("^\\s*SUCCESS", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()
							&& !Pattern.compile("^\\s*NEVER", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()){
						notifyValue= "NEVER "+notifyValue;
					}
					notifyValue=notifyValue+" ORCPT=rfc822;"+mail.getRecipient();
					
					String[] retHeaders = msg.getHeader(ConnectorProperties.RET_HEADER);
					if(retHeaders!=null && retHeaders.length>0 && retHeaders[0]!=null && !retHeaders[0].trim().isEmpty()){
						retValue = retHeaders[0].trim();
					}else{
						retValue = MailCommand.RET_HDRS;
					}
					hasDsn=true;
					props.put("mail.smtp.dsn.ret", retValue);
					props.put("mail.smtp.dsn.notify", notifyValue);
				}
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
			try{
				send(mail,msg,props);
			}catch(Exception e){
				if(hasDsn){
					log.warn("DSN error for ret="+props.get("mail.smtp.dsn.ret")
							+" notify="+props.get("mail.smtp.dsn.notify"));
					props.remove("mail.smtp.dsn.ret");
					props.remove("mail.smtp.dsn.notify");
					log.debug("send without DSN");
					send(mail,msg,props);
				}
			}
		    if(log.isDebugEnabled()){
		    	log.debug("Message sent:"+mail);
		    }

		} catch (Exception e) {
			log.error("Couldnt send the mail:"+mail,e);
			throw new RuntimeException(e);
		}	

	}

	public void send(MimeMail mail, MimeMessage msg, Properties props) throws MessagingException, UnsupportedEncodingException{
		Session session = Session.getInstance(props);
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
