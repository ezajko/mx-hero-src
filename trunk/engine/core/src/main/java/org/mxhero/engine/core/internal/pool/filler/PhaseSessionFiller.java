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
import org.mxhero.engine.core.mail.SenderVO;
import org.mxhero.engine.core.mail.SubjectVO;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.DomainList;
import org.mxhero.engine.domain.mail.business.Group;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.business.UserList;
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
		Domain domain = null;
		User user = null;
		String userMail = null;
		String domainAgendaGroup = null;

		if (mail != null) {
			if (mail.getPhase().equals(RulePhase.SEND)) {
				userMail = mail.getInitialSender();
				mail.setSenderId(userMail);
			} else if (mail.getPhase().equals(RulePhase.RECEIVE)) {
				userMail = mail.getRecipient();
				mail.setRecipientId(userMail);
			}
			log.debug("filling session for user:"+userMail);
		}

		if (userMail != null) {
			if (domainFinder != null) {

				String domainId = userMail
						.substring(userMail.indexOf(DIV_CHAR) + 1);
				
				if (mail.getPhase().equals(RulePhase.SEND)) {
					mail.setSenderDomainId(domainId);
				} else if (mail.getPhase().equals(RulePhase.RECEIVE)) {
					mail.setRecipientDomainId(domainId);
				}
				
				domain = domainFinder.getDomain(domainId);
				if (domain != null) {
					log.debug("domain found " + domain);
					if (mail.getPhase().equals(RulePhase.SEND)) {
						mail.setSenderDomainId(domain.getId());
					} else if (mail.getPhase().equals(RulePhase.RECEIVE)) {
						mail.setRecipientDomainId(domain.getId());
					}
					ksession.insert(domain);
					if(domain.getGroups()!=null){
						for (Group group : domain.getGroups()) {
							ksession.insert(group);
						}
					}
					if(domain.getLists()!=null){
						for (DomainList domainList : domain.getLists()) {
							ksession.insert(domainList);
						}
					}
					if (userFinder != null) {
						user = userFinder.getUser(userMail, domainId);
						if (user != null) {
							log.debug("user found " + user);
							if (mail.getPhase().equals(RulePhase.SEND)) {
								mail.setSenderId(user.getMail());
							} else if (mail.getPhase().equals(RulePhase.RECEIVE)) {
								mail.setRecipientId(user.getMail());
							}
							ksession.insert(user);
							if(user.getLists()!=null){
								for (UserList userList : user.getLists()) {
									ksession.insert(userList);
								}
							}
						}
					}
					/* add domain of the recipient agenda group */
					domainAgendaGroup = domain.getId();
				}
			}

			if (domainAgendaGroup == null) {
				domainAgendaGroup = userMail.substring(userMail
						.indexOf(DIV_CHAR) + 1);
			}
		}

		if(getLogRecordService()!=null){
			getLogRecordService().log(mail);
		}
		
		if (getLogStatService() != null) {
			if (mail.getPhase().equals(RulePhase.SEND)){
				getLogStatService().log(mail, getProperties().getValue(Core.IN_TIME_STAT), getFormat().format(Calendar.getInstance().getTime()));
			}
		}
		
		ksession.insert(new MailVO(mail));
		ksession.insert(new InitialDataVO(mail));
		ksession.insert(new HeadersVO(mail));
		ksession.insert(new SenderVO(mail));
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

