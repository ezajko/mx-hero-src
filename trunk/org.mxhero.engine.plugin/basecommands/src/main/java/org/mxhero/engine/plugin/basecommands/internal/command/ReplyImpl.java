package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.Reply;
import org.mxhero.engine.plugin.basecommands.internal.util.SharedTmpFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the Clone interface. Clones an existing mail. Accepts 4 parameters
 * and the 4th is optional. First parameter is the phase for the email
 * RulePhase.SEND or RulePhase.RECEIVE. Second parameter is the sender mail.
 * Third parameter is the recipient mail. And the fourth and optional parameter
 * is the output service id, if not specified the original output service is
 * used.
 * 
 * @author mmarmol
 */
public class ReplyImpl implements Reply {

	private static Logger log = LoggerFactory.getLogger(ReplyImpl.class);

	private static final int MIM_PARAMANS = 4;
	private static final int SENDER_PARAM_NUMBER = 0;
	private static final int RECIPIENT_PARAM_NUMBER = 1;
	private static final int TEXT_PARAM_NUMBER = 2;
	private static final int HTML_PARAM_NUMBER = 3;
	private static final int INCLUDE_MESSAGE_PARAM_NUMBER = 4;
	private static final int OUTPUTSERVICE_PARAM_NUMBER = 5;
	
	private static final String TMP_FILE_SUFFIX = ".eml";
	private static final String TMP_FILE_PREFIX = "reply";
	private static final int DEFERRED_SIZE = 1*1024*1024;
	
	private static final String SENDER_KEY = "mxsender";
	private static final String RECIPIENT_KEY = "mxrecipient";

	private InputService service;
	
	private String noReplySignature = "";
	private String noReplyHTLMSignature = "";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		InternetAddress sender = null;
		InternetAddress recipient = null;
		String outputService = null;
		MimeMail replayMail = null;
		result.setResult(false);

		if (args == null || args.length < MIM_PARAMANS) {
			log.warn("wrong ammount of params.");
			return result;
		} else {

			try {
				sender = new InternetAddress(args[SENDER_PARAM_NUMBER]);
			} catch (AddressException e) {
				log.warn("wrong sender address");
				return result;
			}

			try {
				String recipientString = null;
				if(args.length<=RECIPIENT_PARAM_NUMBER || args[RECIPIENT_PARAM_NUMBER]==null || args[RECIPIENT_PARAM_NUMBER].isEmpty()){
					if(mail.getMessage().getReplyTo()!=null
							&& mail.getMessage().getReplyTo()[0]!=null
							&& !mail.getMessage().getReplyTo()[0].toString().isEmpty()){
						recipientString = mail.getMessage().getReplyTo()[0].toString();
					} else {
						recipientString = mail.getRecipient();
					}
				}else{
					recipientString = args[RECIPIENT_PARAM_NUMBER];
				}
				recipient = new InternetAddress(recipientString);
			} catch (AddressException e) {
				log.warn("wrong recipient address");
				return result;
			} catch (MessagingException e) {
				log.warn("wrong recipient address");
				return result;
			}

			if (args.length > OUTPUTSERVICE_PARAM_NUMBER
					&& args[OUTPUTSERVICE_PARAM_NUMBER] != null
					&& !args[OUTPUTSERVICE_PARAM_NUMBER].isEmpty()) {
				outputService = args[OUTPUTSERVICE_PARAM_NUMBER];
			} else {
				outputService = mail.getResponseServiceId();
			}

			if(!mail.getProperties().containsKey(Reply.class.getName())){
				try {
					MimeMessage replayMessage = (MimeMessage)mail.getMessage().reply(false);
					replayMessage.setSender(sender);
					replayMessage.setFrom(sender);
					replayMessage.setReplyTo(new InternetAddress[]{sender});
					MimeMultipart mixed = new MimeMultipart();
					MimeMultipart multipartText = new MimeMultipart("alternative");
					if(args.length>HTML_PARAM_NUMBER 
							&& args[HTML_PARAM_NUMBER]!=null
							&& !args[HTML_PARAM_NUMBER].isEmpty()){

						BodyPart htmlBodyPart = new MimeBodyPart();
						Document doc = Jsoup.parse(replaceTextVars(mail,args[HTML_PARAM_NUMBER]));
						if((noReplyHTLMSignature!=null) && !noReplyHTLMSignature.trim().isEmpty()){
							doc.body().append(noReplyHTLMSignature);
						}
						htmlBodyPart.setContent(doc.outerHtml(),"text/html");
						multipartText.addBodyPart(htmlBodyPart);
					}
					if(args.length>TEXT_PARAM_NUMBER 
							&& args[TEXT_PARAM_NUMBER]!=null
							&& !args[TEXT_PARAM_NUMBER].isEmpty()){

						BodyPart textBodyPart = new MimeBodyPart();
						textBodyPart.setText(replaceTextVars(mail,args[TEXT_PARAM_NUMBER])+((noReplySignature!=null)?noReplySignature:""));
						multipartText.addBodyPart(textBodyPart);
					}
					
					MimeBodyPart wrap = new MimeBodyPart();
					wrap.setContent(multipartText); 
					mixed.addBodyPart(wrap);
					
					if(args.length>INCLUDE_MESSAGE_PARAM_NUMBER 
							&& args[INCLUDE_MESSAGE_PARAM_NUMBER]!=null
							&& Boolean.parseBoolean(args[INCLUDE_MESSAGE_PARAM_NUMBER])){
						BodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setContent(mail.getMessage(), "message/rfc822");
						mixed.addBodyPart(messageBodyPart);
					}
					
					replayMessage.setContent(mixed);
					replayMessage.saveChanges();
					
					InputStream is = null;
					OutputStream os = null;
					try {
						if(mail.getInitialSize()>DEFERRED_SIZE){
							File tmpFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
							os = new FileOutputStream(tmpFile);
							replayMessage.writeTo(os);
							is = new SharedTmpFileInputStream(tmpFile);
							
						}else{
							os = new ByteArrayOutputStream();
							replayMessage.writeTo(os);
							is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
						}
						
						replayMail = new MimeMail(sender.getAddress(), recipient.getAddress(), is, outputService);
						replayMail.setPhase(RulePhase.OUT);
						replayMail.getProperties().put(Reply.class.getName(), recipient.getAddress());

					} catch (Exception e) {
						log.warn("error while creating cloned message",e);
						return result;
					} finally{
						if(os!=null){
							try{os.flush();os.close();}catch(Exception e){}
						}
						if(is!=null){
							try{is.close();}catch(Exception e){}
						}
					}
				} catch (MessagingException e) {
					log.warn("error while creating replay message");
					return result;
				} catch (Exception e) {
					log.warn("error while creating replay message");
					return result;
				}
	
				if (service == null) {
					log.warn("core input service is not online");
					return result;
				}
				try {
					service.addMail(replayMail);
				} catch (QueueFullException e) {
					log.error("queue is full", e);
					return result;
				}
			}
			result.setResult(true);
		}

		return result;
	}

	
	private String replaceTextVars(MimeMail mail, String content){
		String tagregex = "\\$\\{[^\\{]*\\}";
		Pattern p2 = Pattern.compile(tagregex);
		StringBuffer sb = new StringBuffer();
		Matcher m2 = p2.matcher(content);
		int lastIndex = 0;
		
		while (m2.find()) {
		lastIndex=m2.end();
		  String key =content.substring(m2.start()+2,m2.end()-1);
		  
		  if(key.equalsIgnoreCase(SENDER_KEY)){
			  m2.appendReplacement(sb, mail.getInitialSender());
		  }else if(key.equalsIgnoreCase(RECIPIENT_KEY)){
			  m2.appendReplacement(sb, mail.getRecipient());
		  }else{
			  //this should be a header
			  try{
				  String[] headers = mail.getMessage().getHeader(key);
				  if(headers!=null){
					  m2.appendReplacement(sb, headers[0]); 
				  }
			  }catch (MessagingException e){
				  //do nothing, just ignore this header
			  }
		  }

		}
		sb.append(content.substring(lastIndex));
		return sb.toString();
	}
	
	/**
	 * @return
	 */
	public InputService getService() {
		return service;
	}

	/**
	 * @param service
	 */
	public void setService(InputService service) {
		this.service = service;
	}

	public String getNoReplySignature() {
		return noReplySignature;
	}

	public void setNoReplySignature(String noReplySignature) {
		this.noReplySignature = noReplySignature;
	}


	public String getNoReplyHTLMSignature() {
		return noReplyHTLMSignature;
	}


	public void setNoReplyHTLMSignature(String noReplyHTLMSignature) {
		this.noReplyHTLMSignature = noReplyHTLMSignature;
	}

}
