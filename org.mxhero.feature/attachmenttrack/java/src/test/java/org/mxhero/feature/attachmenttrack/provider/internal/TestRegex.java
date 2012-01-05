package org.mxhero.feature.attachmenttrack.provider.internal;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.junit.Test;

public class TestRegex {

	@Test
	public void testRegex() throws ParseException, UnsupportedEncodingException{
		System.out.println(Arrays.deepToString(parseParameters("attachmentTrack=\"\\\"one\\,true\\\",two\";","attachmentTrack")));
		System.out.println(Arrays.deepToString(parseParameters(";attachmentTrack=\"\";","attachmentTrack")));
	}

	public String[] parseParameters(String parameters, String key) throws ParseException{		
		if(!parameters.matches("\\s*;.*")){
			parameters=";"+parameters;
		}
		String value = new ParameterList(parameters).get(key);
		if(value==null){
			return null;
		}
		String regex = "[, ]+(and|or)*[, ]*";
		return value.split(regex);
	}
	
}
