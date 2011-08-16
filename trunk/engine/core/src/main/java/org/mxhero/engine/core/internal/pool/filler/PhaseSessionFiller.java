package org.mxhero.engine.core.internal.pool.filler;

import java.text.SimpleDateFormat;
import java.util.HashSet;

import javax.mail.internet.InternetAddress;

import org.drools.runtime.StatefulKnowledgeSession;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.core.mail.AttachmentsVO;
import org.mxhero.engine.core.mail.BodyVO;
import org.mxhero.engine.core.mail.HeadersVO;
import org.mxhero.engine.core.mail.InitialDataVO;
import org.mxhero.engine.core.mail.MailVO;
import org.mxhero.engine.core.mail.RecipientsVO;
import org.mxhero.engine.core.mail.SubjectVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.MailFlow;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class add the business objects to the session.
 * 
 * @author mmarmol
 */
public class PhaseSessionFiller implements SessionFiller {

	private static Logger log = LoggerFactory
			.getLogger(PhaseSessionFiller.class);

	private static final char DIV_CHAR = '@';
	
	private LogRecord logRecordService;
	
	private LogStat logStatService;
	
	private PropertiesService properties;

	private SimpleDateFormat format;
	
	/**
	 * @see org.mxhero.engine.core.internal.pool.filler.SessionFiller#fill(org.drools.runtime.StatefulKnowledgeSession, org.mxhero.engine.domain.mail.finders.UserFinder, org.mxhero.engine.domain.mail.finders.DomainFinder, org.mxhero.engine.domain.mail.MimeMail)
	 */
	@Override
	public String fill(StatefulKnowledgeSession ksession,
			UserFinder userFinder, MimeMail mail) {

		String domainAgendaGroup = null;
		InitialDataVO initialData = null;

		initialData = getInitialData(userFinder, mail);
		
		if(mail.getPhase().equals(RulePhase.SEND)){
			domainAgendaGroup = mail.getSenderDomainId();
		} else if (mail.getPhase().equals(RulePhase.RECEIVE)){
			domainAgendaGroup = mail.getRecipientDomainId();
		}
		
		if(getLogRecordService()!=null){
			getLogRecordService().log(mail);
		}
		
		ksession.insert(new MailVO(mail));
		ksession.insert(initialData);
		ksession.insert(new HeadersVO(mail));
		ksession.insert(new SubjectVO(mail));
		ksession.insert(new RecipientsVO(mail));
		ksession.insert(new BodyVO(mail));
		ksession.insert(new AttachmentsVO(mail));
		
		return domainAgendaGroup;
	}

	
	/**
	 * @param userFinder
	 * @param domainFinder
	 * @param mail
	 * @return
	 */
	private InitialDataVO getInitialData(UserFinder userFinder, MimeMail mail){
		InitialDataVO initialData = null;
		String fromId = null;
		String fromDomainId = null;
		User sender = null;
		Domain senderDomain = null;
		User from = null;
		Domain fromDomain = null;
		User recipient = null;
		Domain recipientDomain = null;
		
		if (mail != null) {
			mail.setSenderId(mail.getInitialSender().trim());
			mail.setRecipientId(mail.getRecipient().trim());
			mail.setSenderDomainId(mail.getSenderId().substring(mail.getSenderId().indexOf(DIV_CHAR) + 1).trim());
			mail.setRecipientDomainId(mail.getRecipientId().substring(mail.getRecipientId().indexOf(DIV_CHAR) + 1).trim());
			
			if (userFinder != null) {
				sender = userFinder.getUser(mail.getSenderId());
				if(sender!=null){
					senderDomain=sender.getDomain();
					mail.setSenderId(sender.getMail());
					mail.setSenderDomainId(senderDomain.getId());
				}
			}

			try {
				if(mail.getMessage().getFrom()!=null && mail.getMessage().getFrom().length>0){
					fromId = new InternetAddress(mail.getMessage().getFrom()[0].toString()).getAddress().trim();
				}else{
					fromId = mail.getSenderId();
				}
			} catch (Exception e) {
				fromId = mail.getSenderId();
			} 
			
			fromDomainId = fromId.substring(fromId.indexOf(DIV_CHAR) + 1).trim();
			
			if(!fromId.equals(mail.getSenderId())){
				if (userFinder != null) {
					from = userFinder.getUser(fromId);
					if(from!=null){
						fromDomain = from.getDomain();
					}
				}
			}else{
				fromDomain = senderDomain;
				from = sender;
			}

			
			if (userFinder != null) {
				recipient = userFinder.getUser(mail.getRecipientId());
				if(recipient!=null){
					recipientDomain=recipient.getDomain();
					mail.setRecipientDomainId(recipientDomain.getId());
					mail.setRecipientId(recipient.getMail());
				}
			}
		
			if (senderDomain == null){
				senderDomain = new Domain();
				senderDomain.setId(mail.getSenderDomainId());
				senderDomain.setManaged(false);
				senderDomain.setAliases(new HashSet<String>());
				senderDomain.getAliases().add(senderDomain.getId());
			}
			
			if (fromDomain == null){
				fromDomain = new Domain();
				fromDomain.setId(fromDomainId);
				fromDomain.setManaged(false);
				fromDomain.setAliases(new HashSet<String>());
				fromDomain.getAliases().add(fromDomain.getId());
			}
			
			if(recipientDomain==null){
				recipientDomain = new Domain();
				recipientDomain.setId(mail.getRecipientDomainId());
				recipientDomain.setManaged(false);
				recipientDomain.setAliases(new HashSet<String>());
				recipientDomain.getAliases().add(recipientDomain.getId());
			}
			
			if (sender == null){
				sender = new User();
				sender.setMail(mail.getSenderId());
				sender.setManaged(false);
				sender.setDomain(senderDomain);
				sender.setAliases(new HashSet<String>());
				sender.getAliases().add(sender.getMail());
			}
			
			if (from == null){
				from = new User();
				from.setMail(fromId);
				from.setManaged(false);
				from.setDomain(fromDomain);
				from.setAliases(new HashSet<String>());
				from.getAliases().add(from.getMail());
			}
			
			if (recipient == null){
				recipient = new User();
				recipient.setMail(mail.getRecipientId());
				recipient.setManaged(false);
				recipient.setDomain(recipientDomain);
				recipient.setAliases(new HashSet<String>());
				recipient.getAliases().add(recipient.getMail());
			}
			
			if(sender.getDomain().getManaged() && recipient.getDomain().getManaged()){
				mail.setFlow(MailFlow.BOTH);
			}else if(sender.getDomain().getManaged()){
				mail.setFlow(MailFlow.OUT);
			}else if(recipient.getDomain().getManaged()){
				mail.setFlow(MailFlow.IN);
			}else {
				mail.setFlow(MailFlow.NONE);
			}
			
			initialData = new InitialDataVO(mail, sender, recipient, from);
		}
		
		return initialData;
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
			format = new SimpleDateFormat(getProperties().getValue(Core.STATS_TIME_FORMAT));
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

