package org.mxhero.engine.core.internal.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mxhero.engine.commons.mail.api.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Util class to work with mimeMessages
 * 
 * @author mmarmol
 */
public abstract class MailUtils {

	private static Logger log = LoggerFactory.getLogger(MailUtils.class);
	
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
	 * Search mail for attachment or inline parts, also chech for application
	 * type messages.
	 * 
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean hasAttachments(Part part) throws MessagingException,
			IOException {
		if ((part.getDisposition() != null && (part.getDisposition().equals(
				Part.ATTACHMENT) || part.getDisposition().equals(Part.INLINE)))
				|| (part.getFileName() != null && !part.getFileName().isEmpty())
				|| part.isMimeType(APPLICATION_TYPE)) {
			return true;
		} else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				if (hasAttachments(mp.getBodyPart(i))) {
					return true;
				}
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			hasAttachments((Part) part.getContent());
		}
		return false;
	}

	/**
	 * Search a part for file names and add those names into the collection
	 * passed.
	 * 
	 * @param part
	 * @param names
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void addFileNames(Part part, Collection<String> names)
			throws MessagingException, IOException {
		if (part.getFileName() != null && !part.getFileName().isEmpty()) {
			names.add(part.getFileName());
		}
		if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addFileNames(mp.getBodyPart(i), names);
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			addFileNames((Part) part.getContent(), names);
		}
	}

	/**
	 * Search a part for types and add those types into the collection passed.
	 * 
	 * @param part
	 * @param types
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void addTypes(Part part, Collection<String> types)
			throws MessagingException, IOException {
		if (!part.isMimeType(MULTIPART_TYPE)
				&& !part.isMimeType(MULTIPART_MESSAGE)) {
			types.add(part.getContentType());
		} else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addTypes(mp.getBodyPart(i), types);
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			addTypes((Part) part.getContent(), types);
		}
	}

	/**
	 * Search
	 * 
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
	private static void getText(Part p, String type, StringBuilder text,
			boolean messages) throws MessagingException, IOException {
		if (p.isMimeType(TEXT_TYPE)) {
			if (p.isMimeType(type)
					&& (p.getFileName() == null || p.getFileName().isEmpty())) {
				
				Object content = p.getContent();
				if(content instanceof String){
					text.append((String) p.getContent());
				}else if(content instanceof InputStream){
					text.append(convertStreamToString((InputStream) content,new ContentType(p.getContentType()).getParameter("charset")));
				}
			}
		} else {

			if (p.isMimeType(MULTIPART_ALTERNATIVE_TYPE)) {
				Multipart mp = (Multipart) p.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					Part bp = mp.getBodyPart(i);
					getText(bp, type, text, messages);
				}
			} else if (p.isMimeType(MULTIPART_TYPE)) {
				Multipart mp = (Multipart) p.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					getText(mp.getBodyPart(i), type, text, messages);
				}
			} else if (p.isMimeType(MULTIPART_MESSAGE)) {
				getText((Part) p.getContent(), type, text, messages);
			}
		}
	}

	public static String convertStreamToString(InputStream is, String charset) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = null;
				try{
					reader = new BufferedReader(new InputStreamReader(is,
							charset));
				}catch(UnsupportedEncodingException e){
					reader = new BufferedReader(new InputStreamReader(is,
							"UTF-8"));
				}
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * Used internally to actually remove files by extension.
	 * 
	 * @param part
	 *            part that is been analyzed for files.
	 * @param parent
	 *            The parent of this part that is also a multipart.
	 * @param extensions
	 *            a collection with all the extension to be removed.
	 * @param filesRemoved
	 *            a collection that holds the names of the files removed.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void removeByExtensions(Part part, Multipart parent, Set<String> extensions,
			Collection<String> filesRemoved) throws MessagingException,
			IOException {
		if (part.getFileName() != null && !part.getFileName().trim().isEmpty()) {
			int i = part.getFileName().trim().lastIndexOf('.');
			if (i > 0 && i < part.getFileName().trim().length() - 1) {
				if (parent != null
						&& extensions.contains(part.getFileName().trim()
								.substring(i + 1).toLowerCase(Locale.ENGLISH))) {
					parent.removeBodyPart((BodyPart) part);
					filesRemoved.add(part.getFileName().trim());
				}
			}
		} else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			Collection<BodyPart> childs = new ArrayList<BodyPart>();
			for (int i = 0; i < mp.getCount(); i++) {
				childs.add(mp.getBodyPart(i));
			}
			for (BodyPart child : childs) {
				removeByExtensions(child, mp, extensions, filesRemoved);
			}
			if (mp.getParent() instanceof Message) {
				((Message) mp.getParent()).setContent(mp);
				((Message) mp.getParent()).saveChanges();
			} else if (part instanceof Message) {
				((Message) part).setContent(mp);
				((Message) part).saveChanges();
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			removeByExtensions((Part) part.getContent(), null, extensions, filesRemoved);
		}

	}
	
	/**
	 * Used internally to actually remove files by extension.
	 * 
	 * @param part
	 *            part that is been analyzed for files.
	 * @param parent
	 *            The parent of this part that is also a multipart.
	 * @param types
	 *            a collection with all the types to be removed.
	 * @param typesRemoved
	 *            a collection that holds the names of the files removed and
	 *            types.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void removeByTypes(Part part, Multipart parent, Set<String> types,
			Collection<String> typesRemoved) throws MessagingException,
			IOException {
		if (!part.isMimeType(MULTIPART_TYPE)
				&& !part.isMimeType(MULTIPART_MESSAGE)
				&& !(part.isMimeType(TEXT_TYPE) && (part.getDisposition() == null || part
						.getDisposition().equals(Part.INLINE)))) {
			if (parent != null && part.getContentType() != null) {
				for (String typeToRemove : types) {
					if (part.getContentType().trim().contains(typeToRemove)) {
						parent.removeBodyPart((BodyPart) part);
						typesRemoved.add(part.getContentType());
						break;
					}
				}
			}

		} else if (part.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) part.getContent();
			Collection<BodyPart> childs = new ArrayList<BodyPart>();
			for (int i = 0; i < mp.getCount(); i++) {
				childs.add(mp.getBodyPart(i));
			}
			for (BodyPart child : childs) {
				removeByTypes(child, mp, types, typesRemoved);
			}
			if (mp.getParent() instanceof Message) {
				((Message) mp.getParent()).setContent(mp);
				((Message) mp.getParent()).saveChanges();
			} else if (part instanceof Message) {
				((Message) part).setContent(mp);
				((Message) part).saveChanges();
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			removeByTypes((Part) part.getContent(), null, types, typesRemoved);
		}

	}
	
	
	public static void addText(Part p, String text, Body.AddTextPosition position, Body.AddTextPartType type)
			throws MessagingException, IOException {
		log.debug("addText=[contentType="+p.getContentType()+", positio="+position.toString()+", type="+type.toString()+"]");
		if (p.isMimeType(TEXT_TYPE)
				&& (p.getDisposition() == null || (!p.getDisposition().equals(Part.INLINE))
				&& !p.getDisposition().equals(Part.ATTACHMENT))) {
			if (position.equals(Body.AddTextPosition.top)) {
				if((type.equals(Body.AddTextPartType.both) || type.equals(Body.AddTextPartType.plain)) && p.isMimeType(TEXT_PLAIN_TYPE)){
					String contentType =  p.getContentType();
					p.setContent(text + ((String) p.getContent()),contentType);
					p.setHeader("Content-Type", contentType);
					log.debug("setContent=[contentType="+p.getContentType()+", text="+text + ((String) p.getContent())+"]");
				}  
				if ((type.equals(Body.AddTextPartType.both) || type.equals(Body.AddTextPartType.html)) && p.isMimeType(TEXT_HTML_TYPE)){
					String contentType =  p.getContentType();
					Document doc = Jsoup.parse((String) p.getContent());
					doc.body().prepend(text);
					p.setContent(doc.outerHtml(), p.getContentType());
					p.setHeader("Content-Type", contentType);
					log.debug("setContent=[contentType="+p.getContentType()+", text="+doc.outerHtml()+"]");
				}
			} else if (position.equals(Body.AddTextPosition.botton)) {
				log.debug("setContent=[contentType="+p.getContentType()+"]");
				if((type.equals(Body.AddTextPartType.both) || type.equals(Body.AddTextPartType.plain)) && p.isMimeType(TEXT_PLAIN_TYPE)){
					String contentType =  p.getContentType();
					log.debug("setContent=[beforeContentType="+p.getContentType()+"]");
					p.setContent(((String) p.getContent()) + text, p.getContentType());
					p.setHeader("Content-Type", contentType);
					log.debug("setContent=[contentType="+p.getContentType()+", text="+text + ((String) p.getContent())+"]");
				}
				if ((type.equals(Body.AddTextPartType.both) || type.equals(Body.AddTextPartType.html)) && p.isMimeType(TEXT_HTML_TYPE)){
					String contentType =  p.getContentType();
					Document doc = Jsoup.parse((String) p.getContent());
					doc.body().append(text);
					log.debug("setContent=[beforeContentType="+p.getContentType()+"]");
					p.setContent(doc.outerHtml(), p.getContentType());
					p.setHeader("Content-Type", contentType);
					log.debug("setContent=[contentType="+p.getContentType()+", text="+doc.outerHtml()+"]");
				}
			}
		} else if (p.isMimeType(MULTIPART_TYPE)) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				addText(mp.getBodyPart(i), text, position, type);
			}
		}
	}
}
