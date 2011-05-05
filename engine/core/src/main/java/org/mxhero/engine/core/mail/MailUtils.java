package org.mxhero.engine.core.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

/**
 * Util class to work with mimeMessages
 * @author mmarmol
 */
public abstract class MailUtils {

	public static final String TEXT_TYPE = "text/*";
	public static final String TEXT_PLAIN_TYPE = "text/plain";
	public static final String TEXT_HTML_TYPE = "text/html";
	public static final String MULTIPART_ALTERNATIVE_TYPE = "multipart/alternative";
	public static final String MULTIPART_TYPE = "multipart/*";
	public static final String APPLICATION_TYPE = "application/*";
	public static final String MULTIPART_MESSAGE = "message/rfc822";

	/**
	 * can not be instantiated
	 */
	private MailUtils() {
	}

	/**
	 * Takes an Array of address and return a collection of strings
	 * @param address
	 * @return
	 */
	public static Collection<String> toStringCollection(Address[] address) {
		Collection<String> addressCollection = new ArrayList<String>();
		if (address != null) {
			for (Address a : address) {
				if(a instanceof InternetAddress)
				addressCollection.add(((InternetAddress) a).getAddress());
			}
		}
		return addressCollection;
	}


	/**
	 * Search mail for attachment or inline parts, also chech for application type messages.
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean hasAttachments(Part part) throws MessagingException, IOException{
		if((part.getDisposition()!=null && 
				(part.getDisposition().equals(Part.ATTACHMENT) || part.getDisposition().equals(Part.INLINE)))
			||(part.getFileName()!=null && !part.getFileName().isEmpty())
			||part.isMimeType(APPLICATION_TYPE)){
			return true;
		}
		else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				if(hasAttachments(mp.getBodyPart(i))){
					return true;
				}
			}
		}else if (part.isMimeType(MULTIPART_MESSAGE)){
			hasAttachments((Part)part.getContent());
		} 
		return false;
	}

	
	/**
	 * Search a part for file names and add those names into the collection passed.
	 * @param part
	 * @param names
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void addFileNames(Part part, Collection<String> names) throws MessagingException, IOException{
		if(part.getFileName()!=null && !part.getFileName().isEmpty()){
			names.add(part.getFileName());
		}
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addFileNames(mp.getBodyPart(i),names);
			}
		}else if (part.isMimeType(MULTIPART_MESSAGE)){
			addFileNames((Part)part.getContent(),names);
		} 
	}

	/**
	 * Search a part for types and add those types into the collection passed.
	 * @param part
	 * @param types
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void addTypes(Part part, Collection<String> types) throws MessagingException, IOException{
		if(!part.isMimeType(MULTIPART_TYPE) && !part.isMimeType(MULTIPART_MESSAGE)){
			types.add(part.getContentType());
		} else if(part.isMimeType(MULTIPART_TYPE)){
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addTypes(mp.getBodyPart(i),types);
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)){
			addTypes((Part)part.getContent(),types);
		}
	}
	
	

	/**
	 * Search
	 * @param part
	 * @param type
	 * @param messages
	 * @return
	 */
	public static String getText(Part part, String type, boolean messages) {
		StringBuilder text = new StringBuilder();
		try {
			getText(part, type, text, messages);
		} catch (MessagingException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
		return text.toString();
	}

	/**
	 * @param p
	 * @param type
	 * @param text
	 * @param messages
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void getText(Part p, String type, StringBuilder text, boolean messages)
			throws MessagingException, IOException {

		if (p.isMimeType(TEXT_TYPE)) {
			if (p.isMimeType(type) && (p.getFileName()==null || p.getFileName().isEmpty())) {
				text.append((String) p.getContent());
			} 
		}else {

				if (p.isMimeType(MULTIPART_ALTERNATIVE_TYPE)) {
					Multipart mp = (Multipart) p.getContent();
					for (int i = 0; i < mp.getCount(); i++) {
						Part bp = mp.getBodyPart(i);
						getText(bp, type, text,messages);
					}
				} else if (p.isMimeType(MULTIPART_TYPE)) {
					Multipart mp = (Multipart) p.getContent();
					for (int i = 0; i < mp.getCount(); i++) {
						getText(mp.getBodyPart(i), type, text, messages);
					}
				} else if(p.isMimeType(MULTIPART_MESSAGE)){
					getText((Part)p.getContent(), type, text, messages);
				}
			}
	}
}
