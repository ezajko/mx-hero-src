package org.mxhero.javax.mail.handler;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

abstract public class ContentTypeHandler {

	public static String cleanContentType(MimePart mp, String contentType) {
		if (contentType == null)
			return null;
		try {
			new ContentType(contentType);
		} catch (ParseException e) {
			ParameterList parameters  = new ParameterList();
			String[] rawParams = contentType.split(";");
			String type = rawParams[0];
			for(int i = 1;i<rawParams.length;i++){
				String[] paramparsed = rawParams[i].split("=",2);
				String key = paramparsed[0];
				String value = paramparsed[1];
				if(value.startsWith("\"") && !value.endsWith("\"")){
					value=value+"\"";
				}
				parameters.set(key,value);
			}
			try {
				contentType = new ContentType(type+parameters.toString()).toString();
				
			} catch (ParseException e1) {
				try {
					contentType = new ContentType(type).toString();
				} catch (ParseException e2) {
					//we can do anything else at this point
				}
			}
		}
		return contentType;
	}
}
