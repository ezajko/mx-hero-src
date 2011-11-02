package org.mxhero.engine.plugin.basecommands.internal.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.RemoveByType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the RemoveByType interface. Remove attachments and inlines by
 * type. Takes any amount of types passed.
 * 
 * @author mmarmol
 */
public class RemoveByTypeImpl implements RemoveByType {

	private static Logger log = LoggerFactory.getLogger(RemoveByTypeImpl.class);

	private static final String TEXT_TYPE = "text/*";
	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String MULTIPART_MESSAGE = "message/rfc822";

	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		Set<String> types = new HashSet<String>();
		Collection<String> typesRemoved = new ArrayList<String>();
		result.setResult(false);
		if (args != null && args.length > 0) {
			for (String extension : args) {
				if (extension != null && !extension.trim().isEmpty()) {
					types.add(extension.toLowerCase(Locale.ENGLISH));
				} else {
					log.warn("wont add type " + extension);
				}
			}
			if (types.size() > 0) {
				try {
					remove(mail.getMessage(), null, types, typesRemoved);
					result.setResult(typesRemoved.size() > 0);
					result.setLongField(typesRemoved.size());
					result.setText(Arrays.toString(typesRemoved.toArray()));
				} catch (MessagingException e) {
					log.error("error while removing", e);
				} catch (IOException e) {
					log.error("error while removing", e);
				}
			} else {
				log.warn("params passed dont have a valid type");
			}
		} else {
			log.warn("params passed dont have a valid type");
		}

		return result;
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
	private void remove(Part part, Multipart parent, Set<String> types,
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
				remove(child, mp, types, typesRemoved);
			}
			if (mp.getParent() instanceof Message) {
				((Message) mp.getParent()).setContent(mp);
				((Message) mp.getParent()).saveChanges();
			} else if (part instanceof Message) {
				((Message) part).setContent(mp);
				((Message) part).saveChanges();
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			remove((Part) part.getContent(), null, types, typesRemoved);
		}

	}

}
