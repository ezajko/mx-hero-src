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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.ContentType;

public class BodyPart {

	private Part part;
	
	public BodyPart(Part part) {
		this.part = part;
	}

	public String getText() throws IOException, MessagingException{
		Object content = part.getContent();
		if(content instanceof String){
			return (String) part.getContent();
		}else if(content instanceof InputStream){
			return convertStreamToString((InputStream) content,new ContentType(part.getContentType()).getParameter("charset"));
		}else{
			return content.toString();
		}
	}
	
	public void setText(String bodyText) throws MessagingException{
		String contentType =  part.getContentType();
		part.setContent(bodyText, part.getContentType());
		part.setHeader("Content-Type", contentType);
	}
	
	public static String convertStreamToString(InputStream is, String charset) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = null;
				try{
					reader = new BufferedReader(new InputStreamReader(is,charset));
				}catch(UnsupportedEncodingException e){
					reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
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
}
