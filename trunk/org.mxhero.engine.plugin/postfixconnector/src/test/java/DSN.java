import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.mailster.smtp.command.impl.MailCommand;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.plugin.postfixconnector.internal.ConnectorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DSN {

	private static Logger log = LoggerFactory.getLogger(DSN.class);
	
	@Test
	public void test() throws MessagingException{
		addOutMail(readMail());
		
	}
	
	private MimeMail readMail() throws MessagingException {
		MimeMail mail = new MimeMail("sender@example.com", "recipient@example.com", this
				.getClass().getClassLoader()
				.getResourceAsStream("mail.eml"), "service");
		mail.getMessage().addHeader(ConnectorProperties.NOTIFY_HEADER, "FAILURE,DELAY ORCPT=rfc822;support@about.com");
		mail.getMessage().addHeader(ConnectorProperties.RET_HEADER, "HDRS");
		mail.getMessage().addHeader(ConnectorProperties.ID_HEADER, mail.getTime().getTime()+"-"+mail.getSequence());
		return mail;
	}
	
	public void addOutMail(MimeMail mail) {
		boolean hasDsn = false;
		Properties props = new Properties();
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
							&& !Pattern.compile("^\\s*FAILURE", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()
							&& !Pattern.compile("^\\s*SUCCESS", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()
							&& !Pattern.compile("^\\s*NEVER", Pattern.CASE_INSENSITIVE).matcher(notifyValue).find()){
						notifyValue= "NEVER ";
					}
					if(notifyValue.contains("ORCPT")){
						notifyValue=notifyValue.split("ORCPT")[0];
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
				
				throw new RuntimeException(e);
			}
			try{
				send(mail,msg,props);
			}catch(Exception e){
				if(hasDsn){
					log.warn("DSN error for ret="+props.get("mail.smtp.dsn.ret")
							+" notify="+props.get("mail.smtp.dsn.notify")
							+" from:"+mail.getSender()+" to:"+mail.getRecipient());
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
		
    	String mailSender = ((mail.getMessage().getHeader("Sender")!=null)?"Sender:"+mail.getMessage().getHeader("Sender")[0]+" ":"")
				+((mail.getMessage().getHeader("From")!=null)?"From:"+mail.getMessage().getHeader("From")[0]:"");
		log.debug("SENT using DSN \n\tDSN [mxHeroSender= "+((mail.getSender()!=null && mail.getSender().trim().length()>0)?mail.getSender():"<>")
				+"]\n\tDSN [mxHeroRecipient= "+mail.getRecipient()
				+"]\n\tDSN [mailSender= "+mailSender
				+"]\n\tDSN [properties= "+props.toString()+"]");
	}
	
	
}
