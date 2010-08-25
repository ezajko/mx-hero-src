package org.mxhero.engine.plugin.spamd.internal.command;

import javax.mail.MessagingException;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.spamd.command.SpamScan;
import org.mxhero.engine.plugin.spamd.internal.scanner.SpamdScanner;
import org.mxhero.engine.plugin.spamd.internal.service.Spamd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the SpamScan interface. This command send the mail to a spam
 * assassin daemon to scan for spam. First paramameter value is a boolean, if
 * true add headers to the mail if false it doesn't. Second parameter is the
 * Flag param name. Third parameter is the Status param name.
 * 
 * @author mmarmol
 */
public class SpamAssassimScan implements SpamScan {

	private static Logger log = LoggerFactory.getLogger(SpamAssassimScan.class);

	private static final int ADD_HEADERS_PARAM_NUMBER = 0;
	private static final int FLAG_HEADER_PARAM_NUMBER = 1;
	private static final int STATUS_HEADER_PARAM_NUMBER = 2;

	private PropertiesService spamdProperties;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		boolean addHeaders = true;
		String statusHeaderName = SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME;
		String flagHeaderName = SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME;

		if (!spamdProperties.getValue(Spamd.FLAG_MAIL_ATTRIBUTE_NAME,
				flagHeaderName).isEmpty()) {
			flagHeaderName = spamdProperties.getValue(
					Spamd.FLAG_MAIL_ATTRIBUTE_NAME, flagHeaderName);
		}
		if (!spamdProperties.getValue(Spamd.STATUS_MAIL_ATTRIBUTE_NAME,
				statusHeaderName).isEmpty()) {
			statusHeaderName = spamdProperties.getValue(
					Spamd.STATUS_MAIL_ATTRIBUTE_NAME, statusHeaderName);
		}

		if (args != null) {
			if (args.length > ADD_HEADERS_PARAM_NUMBER
					&& args[ADD_HEADERS_PARAM_NUMBER] != null
					&& !args[ADD_HEADERS_PARAM_NUMBER].isEmpty()
					&& (args[ADD_HEADERS_PARAM_NUMBER]
							.equalsIgnoreCase(Boolean.TRUE.toString()) || args[ADD_HEADERS_PARAM_NUMBER]
							.equalsIgnoreCase(Boolean.FALSE.toString()))) {
				addHeaders = Boolean
						.parseBoolean(args[ADD_HEADERS_PARAM_NUMBER]);
			}
			if (args.length > FLAG_HEADER_PARAM_NUMBER
					&& args[FLAG_HEADER_PARAM_NUMBER] != null
					&& !args[FLAG_HEADER_PARAM_NUMBER].isEmpty()) {
				flagHeaderName = args[FLAG_HEADER_PARAM_NUMBER];
			}
			if (args.length > STATUS_HEADER_PARAM_NUMBER
					&& args[STATUS_HEADER_PARAM_NUMBER] != null
					&& !args[STATUS_HEADER_PARAM_NUMBER].isEmpty()) {
				statusHeaderName = args[STATUS_HEADER_PARAM_NUMBER];
			}
		}

		String hostName = spamdProperties.getValue(Spamd.HOSTNAME);

		int port = -1;
		try {
			port = Integer.parseInt(spamdProperties.getValue(Spamd.PORT));
		} catch (NumberFormatException e) {
			log.warn("wrong port format");
		}

		SpamdScanner scanner = new SpamdScanner(hostName, port);
		double hits = -1;
		try {
			log.debug("ready for scann:" + scanner.toString());
			result.setResult(scanner.scan(mail.getMessage()));
			log.debug("scanned for spam:" + scanner.toString());
			try {
				hits = Double.parseDouble(scanner.getHits());
			} catch (NumberFormatException e) {
				log.warn("wrong hits format");
			}
			result.setLongField((long) hits);
			result.setDoubleField(hits);
			result.setText(scanner.getHeadersAsAttribute().get(
					SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME));
			if (addHeaders) {
				log.debug("adding headers:" + scanner.getHeadersAsAttribute());
				mail.getMessage().setHeader(
						statusHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME));
				mail.getMessage().setHeader(
						flagHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME));
			}
			mail.getMessage().saveChanges();

		} catch (MessagingException e) {
			log.warn("error while scanning for spam");
		}
		return result;
	}

	/**
	 * @return the spamdProperties
	 */
	public PropertiesService getSpamdProperties() {
		return spamdProperties;
	}

	/**
	 * @param spamdProperties
	 *            the spamdProperties to set
	 */
	public void setSpamdProperties(PropertiesService spamdProperties) {
		this.spamdProperties = spamdProperties;
	}

}
