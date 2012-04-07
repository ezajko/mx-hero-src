package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
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
public class CloneImpl implements Clone {

	private static Logger log = LoggerFactory.getLogger(CloneImpl.class);
	
	private static final String TMP_FILE_SUFFIX = ".eml";
	private static final String TMP_FILE_PREFIX = "clone";
	private static final int DEFERRED_SIZE = 1*1024*1024;

	private InputService service;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		MimeMail clonedMail = null;
		InternetAddress sender = null;
		InternetAddress recipient = null;
		CloneParameters cloneParameters = new CloneParameters(parameters);
		
		String outputService = cloneParameters.getOutputService();
		Mail.Phase phase = cloneParameters.getPhase();
		String override= cloneParameters.getOverride();
		Boolean generateNewMessageId = cloneParameters.getGenerateId();

		if(cloneParameters.getRecipient()==null){
			log.warn("wrong ammount of params.");
			result.setMessage("wrong ammount of params.");
			return result;
		}

			try {
				String senderEmail = cloneParameters.getSender();
				if(senderEmail!=null){
					sender = new InternetAddress(senderEmail,false);
				}else{
					sender = new InternetAddress(mail.getSender(),false);
				}
				String recipientEmail = cloneParameters.getRecipient();
				recipient = new InternetAddress(recipientEmail,false);
				if(outputService==null){
					outputService = mail.getResponseServiceId();
				}
				if(phase==null){
					phase=mail.getPhase();
				}
				if(override == null || 
						(!override.equalsIgnoreCase("in")&&
						!override.equalsIgnoreCase("out")&&
						!override.equalsIgnoreCase("both"))){
					override = "none";
				}
				if(generateNewMessageId==null){
					generateNewMessageId = false;
				}	
			} catch (Exception e) {
				log.warn("wrong sender address");
				result.setMessage("wrong paramters: "+e.getMessage());
				result.setAnError(true);
				return result;
			}
			
			log.debug("override:"+override);
			if(!mail.getProperties().containsKey(Reply.class.getName())){
				InputStream is = null;
				OutputStream os = null;
				try {
					if(mail.getInitialSize()>DEFERRED_SIZE){
						File tmpFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
						os = new FileOutputStream(tmpFile);
						mail.getMessage().writeTo(os);
						is = new SharedTmpFileInputStream(tmpFile);
						
					}else{
						os = new ByteArrayOutputStream();
						mail.getMessage().writeTo(os);
						is = new ByteArrayInputStream(((ByteArrayOutputStream)os).toByteArray());
					}

					clonedMail = new MimeMail((sender!=null)?sender.getAddress():"<>", recipient.getAddress(),is, outputService);
					if(override.equalsIgnoreCase("in")){
						clonedMail.getMessage().setFrom(sender);
						clonedMail.getMessage().setReplyTo(new Address[]{sender});
						log.debug("overriding from and replyTo:"+sender.getAddress());
					}else if(override.equalsIgnoreCase("out")){
						clonedMail.getMessage().setRecipient(Message.RecipientType.TO, recipient);
						log.debug("overriding To:"+recipient.getAddress());
					}else if(override.equalsIgnoreCase("both")){
						clonedMail.getMessage().setFrom(sender);
						clonedMail.getMessage().setReplyTo(new Address[]{sender});
						clonedMail.getMessage().setRecipients(Message.RecipientType.TO, new Address[]{recipient});
						log.debug("overriding To:"+recipient.getAddress());
						log.debug("overriding from and replyTo:"+sender.getAddress());
					}
					if(generateNewMessageId){
						clonedMail.setMessageId(null);
					}
					clonedMail.getMessage().saveChanges();
					clonedMail.getProperties().putAll(mail.getProperties());
					clonedMail.getProperties().put(Reply.class.getName(), recipient.getAddress());
				} catch (Exception e) {
					log.warn("error while creating cloned message");
					result.setAnError(true);
					result.setConditionTrue(false);
					result.setMessage(e.getMessage());
					return result;
				} finally{
					if(os!=null){
						try{os.flush();os.close();}catch(Exception e){}
					}
					if(is!=null){
						try{is.close();}catch(Exception e){}
					}
				}
	
				if (service == null) {
					log.warn("core input service is not online");
					return result;
				}
				
				try {
					service.addMail(clonedMail);
				} catch (QueueFullException e) {
					log.warn("queue is full",e);
					return result;
				}
			}
			result.setConditionTrue(true);
			return result;
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

}
