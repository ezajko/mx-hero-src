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

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Result;
import org.mxhero.engine.plugin.basecommands.command.RemoveByExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the RemoveByExtension interface. Remove attachments and inlines by
 * extension. Takes any amount of extensions passed.
 * 
 * @author mmarmol
 */
public class RemoveByExtensionImpl implements RemoveByExtension {

	private static Logger log = LoggerFactory
			.getLogger(RemoveByExtensionImpl.class);

	private static final String MULTIPART_TYPE = "multipart/*";
	private static final String MULTIPART_MESSAGE = "message/rfc822";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.
	 * domain.mail.MimeMail, java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, String... args) {
		Result result = new Result();
		Set<String> extensions = new HashSet<String>();
		Collection<String> filesRemoved = new ArrayList<String>();
		result.setResult(false);
		if (args != null && args.length > 0) {
			for (String extension : args) {
				if (extension != null && !extension.trim().isEmpty()) {
					extensions.add(extension.toLowerCase(Locale.ENGLISH));
				}
			}
			if (extensions.size() > 0) {
				try {
					remove(mail.getMessage(), null, extensions, filesRemoved);
					mail.getMessage().saveChanges();
					result.setResult(filesRemoved.size() > 0);
					result.setLongField(filesRemoved.size());
					result.setText(Arrays.toString(filesRemoved.toArray()));
				} catch (MessagingException e) {
					log.error("error while removing", e);
				} catch (IOException e) {
					log.error("error while removing", e);
				}
			} else {
				log.warn("params passed dont have a valid extension");
			}
		} else {
			log.warn("params passed dont have a valid extension");
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
	 * @param extensions
	 *            a collection with all the extension to be removed.
	 * @param filesRemoved
	 *            a collection that holds the names of the files removed.
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void remove(Part part, Multipart parent, Set<String> extensions,
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
				remove(child, mp, extensions, filesRemoved);
			}
			if (mp.getParent() instanceof Message) {
				((Message) mp.getParent()).setContent(mp);
				((Message) mp.getParent()).saveChanges();
			} else if (part instanceof Message) {
				((Message) part).setContent(mp);
				((Message) part).saveChanges();
			}
		} else if (part.isMimeType(MULTIPART_MESSAGE)) {
			remove((Part) part.getContent(), null, extensions, filesRemoved);
		}

	}

}
