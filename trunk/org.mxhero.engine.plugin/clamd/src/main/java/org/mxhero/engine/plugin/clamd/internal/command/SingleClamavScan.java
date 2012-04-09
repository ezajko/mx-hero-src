package org.mxhero.engine.plugin.clamd.internal.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import net.taldius.clamav.ScannerException;
import net.taldius.clamav.impl.NetworkScanner;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.clamd.command.ClamavScan;
import org.mxhero.engine.plugin.clamd.command.ClamavScanParameters;
import org.mxhero.engine.plugin.clamd.command.ClamavScanResult;
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

	private static final String FOUND_STRING = "FOUND";
	private static final String STATUS_CLEAN = "clean";
	private static final String STATUS_INFECTED = "infected";
	private static final String STATUS_NOT_SCANNED = "not-scanned";

	private static final String DEFAULT_VIRUS_HEADER = "X-Virus-Status";

	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String MESSAGE_TYPE = "message/rfc822";
	private static final String APPLICATION_TYPE = "application/";

	private Integer connectionTimeOut = 30000;
	private String hostName = "localhost";
	private Integer port = 6665;
	private String virusHeader = DEFAULT_VIRUS_HEADER;

	private Boolean remove = false;

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		ClamavScanResult result = new ClamavScanResult();
		Boolean addHeader = true;
		String headerName = DEFAULT_VIRUS_HEADER;
		
		if (getVirusHeader() != null && !getVirusHeader().isEmpty()) {
			headerName = getVirusHeader();
		}

		Collection<String> results = new ArrayList<String>();
		if (parameters == null) {
			log.warn("wrong ammount of params");
			result.setAnError(true);
			result.setMessage("wrong ammount of params");
			return result;
		}
		ClamavScanParameters csParameters = new ClamavScanParameters(parameters);

		remove = csParameters.getRemoveInfected();
		if (remove == null) {
			remove = false;
		}
		addHeader = csParameters.getAddHeader();
		if (addHeader == null) {
			addHeader = true;
		}
		headerName = csParameters.getHeaderName();
		if (headerName == null) {
			headerName = getVirusHeader();
		}

		try {
			if (addHeader) {
				mail.getMessage().setHeader(headerName, STATUS_NOT_SCANNED);
			}
			scanAndremove(mail.getMessage(), null, results);
			if (results.size() > 0 && !remove) {
				result.setConditionTrue(true);
				if (addHeader) {
					mail.getMessage().setHeader(headerName, STATUS_INFECTED);
					result.setMessage(STATUS_INFECTED);
				}
			} else {
				if (addHeader) {
					mail.getMessage().setHeader(headerName, STATUS_CLEAN);
					result.setMessage(STATUS_CLEAN);
				}
			}
			result.setScanResults(results);
			mail.getMessage().saveChanges();
		} catch (Exception e) {
			log.warn("error while scanning:"+ e.getMessage());
			result.setAnError(true);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * This method is used to scan individual attachments and remove them if
	 * they are infected.
	 * 
	 * @param part
	 *            part been analyzed for attachments.
	 * @param parent
	 *            parent of the part that should be a multipart or null.
	 * @param results
	 *            for each file analyzed we return the name, type and result of
	 *            the analysis.
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ScannerException
	 */
	private void scanAndremove(Part part, Multipart parent,
			Collection<String> results) throws MessagingException, IOException,
			ScannerException {
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
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
			scanAndremove((Part) part.getContent(), null, results);
		} else if ((part.getFileName() != null && !part.getFileName().trim()
				.isEmpty())
				|| (part.getDisposition() != null && (part.getDisposition()
						.equals(Part.INLINE) || part.getDisposition().equals(
						Part.ATTACHMENT)))
				|| (part.getContentType() != null && part.getContentType()
						.trim().startsWith(APPLICATION_TYPE))) {

			NetworkScanner scanner = new NetworkScanner();
			scanner.setClamdHost(getHostName());
			scanner.setClamdPort(getPort());
			scanner.setConnectionTimeout(getConnectionTimeOut());
			scanner.performScan(part.getInputStream());
			String scanResult = scanner.getMessage();

			if (scanResult.substring(
					scanResult.length() - FOUND_STRING.length()).equals(
					FOUND_STRING)) {
				if (remove) {
					parent.removeBodyPart((BodyPart) part);
				}
				results.add("type:" + part.getContentType() + "; fileName:"
						+ part.getFileName() + "; " + scanResult);
			}
		}
	}

	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
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

	public String getVirusHeader() {
		return virusHeader;
	}

	public void setVirusHeader(String virusHeader) {
		this.virusHeader = virusHeader;
	}

}
