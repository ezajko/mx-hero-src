package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.commons.connector.QueueFullException;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.business.RulePhase;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.Clone;
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
public class CloneImpl implements Clone {

	private static Logger log = LoggerFactory.getLogger(CloneImpl.class);

	private static final int MIM_PARAMANS = 3;
	private static final int PHASE_PARAM_NUMBER = 0;
	private static final int SENDER_PARAM_NUMBER = 1;
	private static final int RECIPIENT_PARAM_NUMBER = 2;
	private static final int OUTPUTSERVICE_PARAM_NUMBER = 3;
	
	private static final String TMP_FILE_SUFFIX = ".eml";
	private static final String TMP_FILE_PREFIX = "clone";
	private static final int DEFERRED_SIZE = 1*1024*1024;

	private InputService service;

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
		MimeMail clonedMail = null;
		result.setResult(false);

		if (args == null || args.length < MIM_PARAMANS) {
			log.warn("wrong ammount of params.");
			return result;
		} else if (args[PHASE_PARAM_NUMBER] == null
				|| !(args[PHASE_PARAM_NUMBER].equalsIgnoreCase(RulePhase.SEND) || args[PHASE_PARAM_NUMBER]
						.equalsIgnoreCase(RulePhase.RECEIVE))) {
			log.warn("wrong params.");
			return result;
		} else if (args[RECIPIENT_PARAM_NUMBER] == null
				|| args[RECIPIENT_PARAM_NUMBER].isEmpty()) {
			log.warn("wrong params.");
			return result;
		} else {
			try {
				if(args[SENDER_PARAM_NUMBER]!=null && !args[SENDER_PARAM_NUMBER].isEmpty()){
					sender = new InternetAddress(args[SENDER_PARAM_NUMBER]);
				}
			} catch (AddressException e) {
				log.warn("wrong sender address");
				return result;
			}
			try {
				recipient = new InternetAddress(args[RECIPIENT_PARAM_NUMBER]);
			} catch (AddressException e) {
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

					clonedMail.setPhase(RulePhase.SEND);
					clonedMail.getProperties().putAll(mail.getProperties());
					clonedMail.getProperties().put(Reply.class.getName(), recipient.getAddress());
				} catch (Exception e) {
					log.warn("error while creating cloned message");
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
			result.setResult(true);
		}

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
