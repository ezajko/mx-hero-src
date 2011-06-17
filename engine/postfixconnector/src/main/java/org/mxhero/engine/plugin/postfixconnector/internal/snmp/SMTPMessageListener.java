package org.mxhero.engine.plugin.postfixconnector.internal.snmp;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.mailster.smtp.api.SessionContext;
import org.mailster.smtp.api.listener.MessageListener;
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.connector.QueueFullException;
import org.mxhero.engine.domain.filter.PreFilter;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.mail.log.LogMail;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.fixer.Fixer;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol This class just takes the mails and added it to the queue
 *         creating a copy of the Stream.
 */
public final class SMTPMessageListener implements MessageListener {

	private static Logger log = LoggerFactory
			.getLogger(SMTPMessageListener.class);

	private static final char DIV_CHAR = '@';

	private LogRecord logRecordService;

	private LogStat logStatService;

	private PropertiesService properties;

	private DomainFinder domainFinderService;

	private UserFinder userFinderService;

	private SimpleDateFormat format;

	private Collection<Fixer> fixers;

	private InputService service;

	@SuppressWarnings("rawtypes")
	private List preFiltersList;

	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#accept(org.mailster.smtp.api.SessionContext,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public boolean accept(SessionContext ctx, String from, String recipient) {
		/* if any preFilter returns true, just cancel this */
		User fromUser = null;
		User recipientUser = null;
		if (preFiltersList != null && preFiltersList.size() > 0) {
			fromUser = getUser(from);
			recipientUser = getUser(recipient);
			for (Object preFilter : preFiltersList) {
				log.debug(preFilter.getClass().getName());
				if (((PreFilter) preFilter).filter(fromUser, recipientUser)) {
					return false;
				}
			}
		}
		return true;
	}

	private User getUser(String email) {
		User user = null;
		Domain userDomain = null;
		String userId = email.trim();
		String userDomianId = userId.substring(userId.indexOf(DIV_CHAR) + 1)
				.trim();

		if (domainFinderService != null) {
			userDomain = domainFinderService.getDomain(userDomianId);
			if (userDomain != null) {
				if (userFinderService != null) {
					user = userFinderService
							.getUser(userId, userDomain.getId());
				}
			}
		}

		if (userDomain == null) {
			userDomain = new Domain();
			userDomain.setId(userDomianId);
			userDomain.setManaged(false);
		}

		if (user == null) {
			user = new User();
			user.setMail(userId);
			user.setManaged(false);
			user.setDomain(userDomain);
		}

		return user;
	}

	/**
	 * @see org.mailster.smtp.api.listener.MessageListener#deliver(org.mailster.smtp.api.SessionContext,
	 *      java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public void deliver(SessionContext ctx, String from, String recipient,
			InputStream data) throws IOException {

		MimeMail mail;
		MimeMessage message = null;
		try {
			message = new MimeMessage(
					Session.getDefaultInstance(new Properties()), data);
			/* fix content type */
			if (fixers != null && fixers.size() > 0) {
				for (Fixer fixer : fixers) {
					fixer.fixit(message);
				}
			}
			message.removeHeader(getProperties().getValue(
					PostfixConnector.SENDER_HEADER,
					PostfixConnector.DEFAULT_SENDER_HEADER));
			message.removeHeader(getProperties().getValue(
					PostfixConnector.RECIPIENT_HEADER,
					PostfixConnector.DEFAULT_RECIPIENT_HEADER));
			if (getProperties() != null
					&& getProperties().getValue(PostfixConnector.ADD_HEADERS) != null
					&& Boolean.parseBoolean(getProperties().getValue(
							PostfixConnector.ADD_HEADERS))) {
				message.addHeader(
						getProperties().getValue(
								PostfixConnector.SENDER_HEADER,
								PostfixConnector.DEFAULT_SENDER_HEADER), from);
				message.addHeader(
						getProperties().getValue(
								PostfixConnector.RECIPIENT_HEADER,
								PostfixConnector.DEFAULT_RECIPIENT_HEADER),
						recipient);
			}
			message.saveChanges();
			mail = new MimeMail(from, recipient, message,
					PostFixConnectorOutputService.class.getName(), false);
		} catch (Exception e1) {
			log.error("error while receiving email", e1);
			LogMail.saveErrorMail(message,
					getProperties().getValue(PostfixConnector.ERROR_PREFIX),
					getProperties().getValue(PostfixConnector.ERROR_SUFFIX),
					getProperties().getValue(PostfixConnector.ERROR_DIRECTORY));
			throw new IOException(e1);
		}
		log.debug("Mail received:" + mail);

		if (getLogRecordService() != null) {
			getLogRecordService().log(mail);
		}
		if (getLogStatService() != null) {
			getLogStatService().log(mail,
					getProperties().getValue(PostfixConnector.IN_TIME_STAT),
					getFormat().format(Calendar.getInstance().getTime()));
		}
		
		try {
			service.addMail(mail);
		} catch (QueueFullException e) {
			log.error("queue is full, rejecting email "+mail,e);
		}
		log.debug("Mail added:" + mail);

		log.debug("Mail added to queue:" + mail);

	}

	/**
	 * @return the logRecordService
	 */
	public LogRecord getLogRecordService() {
		return logRecordService;
	}

	/**
	 * @param logRecordService
	 *            the logRecordService to set
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
	 * @param logStatService
	 *            the logStatService to set
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
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		if (format == null) {
			format = new SimpleDateFormat(getProperties().getValue(
					PostfixConnector.STATS_TIME_FORMAT));
		}
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}

	public DomainFinder getDomainFinderService() {
		return domainFinderService;
	}

	public void setDomainFinderService(DomainFinder domainFinderService) {
		this.domainFinderService = domainFinderService;
	}

	public UserFinder getUserFinderService() {
		return userFinderService;
	}

	public void setUserFinderService(UserFinder userFinderService) {
		this.userFinderService = userFinderService;
	}

	@SuppressWarnings("rawtypes")
	public List getPreFiltersList() {
		return preFiltersList;
	}

	@SuppressWarnings("rawtypes")
	public void setPreFiltersList(List preFiltersList) {
		this.preFiltersList = preFiltersList;
	}

	public Collection<Fixer> getFixers() {
		return fixers;
	}

	public void setFixers(Collection<Fixer> fixers) {
		this.fixers = fixers;
	}

	public InputService getService() {
		return service;
	}

	public void setService(InputService service) {
		this.service = service;
	}

}
