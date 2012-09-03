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
