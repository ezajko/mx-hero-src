package org.mxhero.engine.core.internal.pool.filler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import org.mxhero.engine.domain.mail.finders.DomainFinder;
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
			UserFinder userFinder, DomainFinder domainFinder, MimeMail mail) {

		String domainAgendaGroup = null;
		InitialDataVO initialData = null;

		
		initialData = getInitialData(userFinder, domainFinder, mail);
		
		if(mail.getPhase().equals(RulePhase.SEND)){
			domainAgendaGroup = mail.getSenderDomainId();
		} else if (mail.getPhase().equals(RulePhase.RECEIVE)){
			domainAgendaGroup = mail.getRecipientDomainId();
		}
		
		if(getLogRecordService()!=null){
			getLogRecordService().log(mail);
		}
		
		if (getLogStatService() != null && mail.getPhase().equals(RulePhase.SEND)) {
			getLogStatService().log(mail, getProperties().getValue(Core.IN_TIME_STAT), getFormat().format(Calendar.getInstance().getTime()));
		}
		
		ksession.insert(new MailVO(mail));
		ksession.insert(initialData);
		ksession.insert(new HeadersVO(mail));
		ksession.insert(new SubjectVO(mail));
		ksession.insert(new RecipientsVO(mail));
		ksession.insert(new BodyVO(mail));
		ksession.insert(new AttachmentsVO(mail));

		if (log.isDebugEnabled()) {
			log.debug("All facts are:");
			for (Object obj : ksession.getObjects()) {
				log.debug(obj.toString());
			}
		}
		
		return domainAgendaGroup;
	}

	
	/**
	 * @param userFinder
	 * @param domainFinder
	 * @param mail
	 * @return
	 */
	private InitialDataVO getInitialData(UserFinder userFinder, DomainFinder domainFinder, MimeMail mail){
		InitialDataVO initialData = null;
		User sender = null;
		Domain senderDomain = null;
		User recipient = null;
		Domain recipientDomain = null;
		
		if (mail != null) {
			mail.setSenderId(mail.getInitialSender());
			mail.setRecipientId(mail.getRecipient());
			mail.setSenderDomainId(mail.getSenderId().substring(mail.getSenderId().indexOf(DIV_CHAR) + 1));
			mail.setRecipientDomainId(mail.getRecipientId().substring(mail.getRecipientId().indexOf(DIV_CHAR) + 1));
			
			if (domainFinder != null) {
				senderDomain = domainFinder.getDomain(mail.getSenderDomainId());
				if(senderDomain!=null){
					log.debug("found senderDomain:"+senderDomain.getId());
					mail.setSenderDomainId(senderDomain.getId());
					if (userFinder != null) {
						sender = userFinder.getUser(mail.getSenderId(), mail.getSenderDomainId());
						if(sender!=null){
							log.debug("found sender:"+sender.getMail());
							mail.setSenderId(sender.getMail());
						}
					}
				}
				
				recipientDomain = domainFinder.getDomain(mail.getRecipientDomainId());	
				if(recipientDomain!=null){
					mail.setRecipientDomainId(recipientDomain.getId());
					log.debug("found recipientDomain:"+recipientDomain.getId());
					if (userFinder != null) {
						recipient = userFinder.getUser(mail.getRecipientId(), mail.getRecipientDomainId());
						if(recipient!=null){
							log.debug("found recipient:"+recipient.getMail());
							mail.setRecipientId(recipient.getMail());
						}
					}
				}
			}			
			
			if (senderDomain == null){
				senderDomain = new Domain();
				senderDomain.setId(mail.getSenderDomainId());
				senderDomain.setManaged(false);
			}
			if(recipientDomain==null){
				recipientDomain = new Domain();
				recipientDomain.setId(mail.getRecipientDomainId());
				recipientDomain.setManaged(false);
			}
			if (sender == null){
				sender = new User();
				sender.setMail(mail.getSenderId());
				sender.setManaged(false);
				sender.setDomain(senderDomain);
			}
			if (recipient == null){
				recipient = new User();
				recipient.setMail(mail.getRecipientId());
				recipient.setManaged(false);
				recipient.setDomain(recipientDomain);
			}
			
			if(sender.getManaged() && recipient.getManaged()){
				mail.setFlow(MailFlow.BOTH);
			}else if(sender.getManaged()){
				mail.setFlow(MailFlow.OUT);
			}else if(recipient.getManaged()){
				mail.setFlow(MailFlow.IN);
			}else {
				mail.setFlow(MailFlow.NONE);
			}
			
			initialData = new InitialDataVO(mail, sender, recipient);
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

