package org.mxhero.engine.plugin.spamd.internal.command;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.spamd.command.SpamScan;
import org.mxhero.engine.plugin.spamd.internal.scanner.SpamdScanner;
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

	private static final int CHANGE_SUBJECT_PARAM_NUMBER = 0;
	private static final int ADD_HEADERS_PARAM_NUMBER = 1;
	private static final int FLAG_HEADER_PARAM_NUMBER = 2;
	private static final int STATUS_HEADER_PARAM_NUMBER = 3;
	
	private String hostName = "localhost";
	private Integer port = 783;
    private String statusMailAttributeName = "X-Spam-Status";
    private String flagMailAttributeName = "X-Spam-Flag";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		String prefix = "";
		boolean addHeaders = true;
		String statusHeaderName = SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME;
		String flagHeaderName = SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME;

		if (getFlagMailAttributeName()!=null && !getFlagMailAttributeName().isEmpty()) {
			flagHeaderName = getFlagMailAttributeName();
		}
		if (getStatusMailAttributeName()!=null && getStatusMailAttributeName().isEmpty()) {
			statusHeaderName = getStatusMailAttributeName();
		}

		if (args != null) {
			if(args.length>CHANGE_SUBJECT_PARAM_NUMBER
					&& args[CHANGE_SUBJECT_PARAM_NUMBER]!=null
					&& !args[CHANGE_SUBJECT_PARAM_NUMBER].trim().isEmpty()){
				prefix = args[CHANGE_SUBJECT_PARAM_NUMBER];
				if(prefix!=null && prefix.length()>0){
					prefix=prefix+" ";
				}
			}
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


		SpamdScanner scanner = new SpamdScanner(getHostName(), getPort());
		double hits = -1;
		try {
			result.setResult(scanner.scan(mail.getMessage()));
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
				mail.getMessage().setHeader(
						statusHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME));
				mail.getMessage().setHeader(
						flagHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME));
			}
			if(result.isTrue()){
				mail.getMessage().setSubject(prefix+mail.getMessage().getSubject());
			}
			mail.getMessage().saveChanges();

		} catch (MessagingException e) {
			log.warn("error while scanning for spam");
		}
		return result;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getStatusMailAttributeName() {
		return statusMailAttributeName;
	}

	public void setStatusMailAttributeName(String statusMailAttributeName) {
		this.statusMailAttributeName = statusMailAttributeName;
	}

	public String getFlagMailAttributeName() {
		return flagMailAttributeName;
	}

	public void setFlagMailAttributeName(String flagMailAttributeName) {
		this.flagMailAttributeName = flagMailAttributeName;
	}

}
