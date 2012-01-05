package org.mxhero.feature.attachmenttrack.provider.internal;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestRegex {

	@Test
	public void testRegex() throws ParseException, UnsupportedEncodingException{
		System.out.println(Arrays.deepToString(parseParameters("\";attachmentLink=\"1,2\";attachmentTrack=\"1,3\";\"","attachmentTrack")));
	}

	public String[] parseParameters(String parameters, String key) throws ParseException{		
		String formatedParameters = StringUtils.trim(parameters);
		if(StringUtils.startsWith(formatedParameters, "\"")
			 && StringUtils.endsWith(formatedParameters,"\"")){
			formatedParameters=formatedParameters.substring(formatedParameters.indexOf("\"")+1,formatedParameters.lastIndexOf("\""));
		}
		String value = new ParameterList(formatedParameters).get(key);
		if(value==null){
			return null;
		}
		String regex = "[, ]+(and|or)*[, ]*";
		return value.split(regex);
	}
	
}
