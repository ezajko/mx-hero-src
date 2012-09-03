package org.mxhero.engine.plugin.body.internal.search;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class SearchMailBodyParts {

	public static final String TEXT_TYPE = "text/*";
	public static final String TEXT_PLAIN_TYPE = "text/plain";
	public static final String TEXT_HTML_TYPE = "text/html";
	public static final String MULTIPART_ALTERNATIVE_TYPE = "multipart/alternative";
	public static final String MULTIPART_TYPE = "multipart/*";
	public static final String APPLICATION_TYPE = "application/*";
	public static final String MULTIPART_MESSAGE = "message/rfc822";


	/**
	 * Search
	 * @param part
	 * @param type
	 * @param messages
	 * @return
	 * @throws IOException 
	 * @throws MessagingException 
	 */
	public static MailBodyParts search(Part part) throws MessagingException, IOException {
		MailBodyParts bodyParts = new MailBodyParts();
		getText(part, bodyParts);
		return bodyParts;
	}

	/**
	 * @param p
	 * @param type
	 * @param text
	 * @param messages
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void getText(Part p,  MailBodyParts bodyParts) throws MessagingException, IOException {
		if (p.isMimeType(TEXT_TYPE)) {
			if (p.getFileName() == null || p.getFileName().isEmpty()) {
				if(p.isMimeType(TEXT_PLAIN_TYPE) && bodyParts.getPlain()==null){
					bodyParts.setPlain(new BodyPart(p));
				}
				if(p.isMimeType(TEXT_HTML_TYPE) && bodyParts.getHtml()==null){
					bodyParts.setHtml(new BodyPart(p));
				}
			}
		} else {

			if (p.isMimeType(MULTIPART_ALTERNATIVE_TYPE)) {
				Multipart mp = (Multipart) p.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					Part bp = mp.getBodyPart(i);
					getText(bp,  bodyParts);
				}
			} else if (p.isMimeType(MULTIPART_TYPE)) {
				Multipart mp = (Multipart) p.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					getText(mp.getBodyPart(i), bodyParts);
				}
			} else if (p.isMimeType(MULTIPART_MESSAGE)) {
				getText((Part) p.getContent(), bodyParts);
			}
		}
	}
}
