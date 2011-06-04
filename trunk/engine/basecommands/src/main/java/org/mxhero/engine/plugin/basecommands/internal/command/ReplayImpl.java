package org.mxhero.engine.plugin.basecommands.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.Replay;
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
public class ReplayImpl implements Replay {

	private static Logger log = LoggerFactory.getLogger(ReplayImpl.class);

	private static final int MIM_PARAMANS = 3;
	private static final int SENDER_PARAM_NUMBER = 0;
	private static final int TEXT_PARAM_NUMBER = 1;
	private static final int PHASE_PARAM_NUMBER = 2;
	private static final int RECIPIENT_PARAM_NUMBER = 3;
	private static final int OUTPUTSERVICE_PARAM_NUMBER = 4;
	
	private static final String SENDER_KEY = "mxsender";
	private static final String RECIPIENT_KEY = "mxrecipient";

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
		MimeMail replayMail = null;
		result.setResult(false);

		if (args == null || args.length < MIM_PARAMANS) {
			log.debug("wrong ammount of params.");
			return result;
		} else if (args[TEXT_PARAM_NUMBER] == null
				|| args[TEXT_PARAM_NUMBER].isEmpty()) {
			log.debug("wrong params.");
			return result;
		} else if (args[PHASE_PARAM_NUMBER] == null
				|| !(args[PHASE_PARAM_NUMBER].equalsIgnoreCase(RulePhase.SEND) || args[PHASE_PARAM_NUMBER]
						.equalsIgnoreCase(RulePhase.RECEIVE))) {
			log.debug("wrong params.");
			return result;
		} else if (args[SENDER_PARAM_NUMBER] == null
				|| args[SENDER_PARAM_NUMBER].isEmpty()) {
			log.debug("wrong params.");
			return result;
		} else if (args[TEXT_PARAM_NUMBER] == null
				|| args[TEXT_PARAM_NUMBER].isEmpty()) {
			log.debug("wrong params.");
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

			if(!mail.getProperties().containsKey(Replay.class.getName())){
				try {
					
					String content = args[TEXT_PARAM_NUMBER];
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
					MimeMessage replayMessage = (MimeMessage)mail.getMessage().reply(false);
					replayMessage.setSender(sender);
					replayMessage.setFrom(sender);
					replayMessage.setReplyTo(new InternetAddress[]{sender});
					replayMessage.setText(sb.toString());
					replayMail = new MimeMail(sender.getAddress(), recipient.getAddress(),
							replayMessage, outputService);
					replayMail.setPhase(args[PHASE_PARAM_NUMBER]);
					replayMail.getProperties().put(Replay.class.getName(), recipient.getAddress());
					if (args[0].equals(RulePhase.RECEIVE)) {
						replayMail.setRecipient(recipient.getAddress());
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
				service.addMail(replayMail);
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
