package org.mxhero.engine.plugin.clamd.internal.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import net.taldius.clamav.ScannerException;
import net.taldius.clamav.impl.NetworkScanner;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.MailState;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.clamd.command.ClamavScan;
import org.mxhero.engine.plugin.clamd.internal.service.Clamd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the ClamavScan. Scan mail for virus using the demon from ClamAV.
 * First parameter is a boolean indicating if it should or not remove the
 * infected files. Second parameter is a boolean indicating if should or not add
 * a header. Third parameter is the name of the header. Values cant be changed.
 * 
 * @author mmarmol
 */
public class SingleClamavScan implements ClamavScan {

	private static Logger log = LoggerFactory.getLogger(SingleClamavScan.class);

	private static final int REMOVE_PARAM_NUMBER = 0;
	private static final int HEADER_PARAM_NUMBER = 1;
	private static final int HEADER_NAME_PARAM_NUMBER = 2;

	private static final String FOUND_STRING = "FOUND";
	private static final String STATUS_CLEAN = "clean";
	private static final String STATUS_INFECTED = "infected";
	private static final String STATUS_NOT_SCANNED = "not-scanned";

	private static final String DEFAULT_VIRUS_HEADER = "X-Virus-Status";

	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String MESSAGE_TYPE = "message/rfc822";
	private static final String APPLICATION_TYPE = "application/";

	private PropertiesService properties;

	private boolean remove = false;
	
	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		result.setResult(false);
		boolean addHeader = true;
		String headerName = DEFAULT_VIRUS_HEADER;
		if (!properties.getValue(Clamd.VIRUS_HEADER, DEFAULT_VIRUS_HEADER)
				.isEmpty()) {
			headerName = properties.getValue(Clamd.VIRUS_HEADER,
					DEFAULT_VIRUS_HEADER);
		}

		Collection<String> results = new ArrayList<String>();
		if (args == null || args[REMOVE_PARAM_NUMBER] == null) {
			log.warn("wrong ammount of params");
			return result;
		} else {
			if (!args[REMOVE_PARAM_NUMBER].equalsIgnoreCase(Boolean.TRUE
					.toString())
					&& !args[REMOVE_PARAM_NUMBER]
							.equalsIgnoreCase(Boolean.FALSE.toString())) {
				log.warn("invalid format");
				return result;
			} else {
				remove = Boolean.parseBoolean(args[REMOVE_PARAM_NUMBER]);
			}
		}
		if (args.length > 1
				&& args[HEADER_PARAM_NUMBER] != null
				&& (args[HEADER_PARAM_NUMBER].equalsIgnoreCase(Boolean.TRUE
						.toString()) || args[HEADER_PARAM_NUMBER]
						.equalsIgnoreCase(Boolean.FALSE.toString()))) {
			addHeader = Boolean.parseBoolean(args[HEADER_PARAM_NUMBER]);
		}
		if (args.length > 2 && args[HEADER_NAME_PARAM_NUMBER] != null
				&& !args[HEADER_NAME_PARAM_NUMBER].isEmpty()) {
			headerName = args[HEADER_NAME_PARAM_NUMBER];
		}

		try {
			if (addHeader) {
				mail.getMessage().setHeader(headerName, STATUS_NOT_SCANNED);
			}

			scanAndremove(mail.getMessage(), null, results);
			
			if (results.size() > 0 && !remove) {
				result.setResult(true);
				if (addHeader) {
					mail.getMessage().setHeader(headerName, STATUS_INFECTED);
					mail.setStatus(MailState.DROP);
					mail.setStatusReason(headerName+":"+STATUS_INFECTED);
				}
			} else {
				if (addHeader) {
					mail.getMessage().setHeader(headerName, STATUS_CLEAN);
				}
			}
			result.setLongField(results.size());
			result.setText(Arrays.toString(results.toArray()));
			mail.getMessage().saveChanges();

		} catch (MessagingException e) {
			log.error("error while scanning", e);
		} catch (IOException e) {
			log.error("error while scanning", e);
		} catch (ScannerException e) {
			log.error("error while scanning", e);
		}

		return result;
	}

	/**
	 * This method is used to scan individual attachments and remove them if they are infected.
	 * @param part part been analyzed for attachments.
	 * @param parent parent of the part that should be a multipart or null.
	 * @param results for each file analyzed we return the name, type and result of the analysis.
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ScannerException 
	 */
	private void scanAndremove(Part part, Multipart parent,
			Collection<String> results) throws MessagingException, IOException, ScannerException {
		log.debug("scanning part");
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			log.debug("scanning multipart");
			Collection<BodyPart> childs = new ArrayList<BodyPart>();
			for (int i = 0; i < mp.getCount(); i++) {
				childs.add(mp.getBodyPart(i));
			}
			for (BodyPart child : childs) {
				scanAndremove(child, mp, results);
			}
			if (mp.getParent() instanceof Message) {
				((Message) mp.getParent()).setContent(mp);
				((Message) mp.getParent()).saveChanges();
			} else if (part instanceof Message) {
				((Message) part).setContent(mp);
				((Message) part).saveChanges();
			}
		} else if (part.isMimeType(MESSAGE_TYPE)) {
			log.debug("scanning message " + part.getContent());
			scanAndremove((Part) part.getContent(), null, results);
		} else if ((part.getFileName() != null && !part.getFileName().trim()
				.isEmpty())
				|| (part.getDisposition() != null && (part.getDisposition()
						.equals(Part.INLINE) || part.getDisposition().equals(
						Part.ATTACHMENT)))
				|| (part.getContentType() != null && part.getContentType()
						.trim().startsWith(APPLICATION_TYPE))) {
			
			NetworkScanner  scanner = new NetworkScanner();
			scanner.setClamdHost(properties.getValue(Clamd.HOSTNAME));
			scanner.setClamdPort(Integer.parseInt(properties.getValue(Clamd.PORT)));
			scanner.setConnectionTimeout(Integer.parseInt(properties.getValue(Clamd.CONNECTION_TIMEOUT)));
			scanner.performScan(part.getInputStream());
			String scanResult = scanner.getMessage();
			
			if (scanResult.substring(
					scanResult.length() - FOUND_STRING.length()).equals(
					FOUND_STRING)) {
				if(remove){
					parent.removeBodyPart((BodyPart) part);
				}
				results.add("type:" + part.getContentType() + "; fileName:"
						+ part.getFileName() + "; " + scanResult);
			}
		}
	}


	/**
	 * @return
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}
