/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
