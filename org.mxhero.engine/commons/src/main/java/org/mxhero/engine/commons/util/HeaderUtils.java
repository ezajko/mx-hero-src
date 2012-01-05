package org.mxhero.engine.commons.util;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.apache.commons.lang.StringUtils;

public abstract class HeaderUtils {

	public static String[] parseParameters(String parameters, String key){		
		String value = null;
		if(parameters==null || key == null){
			return null;
		}
		String formatedParameters = StringUtils.trim(parameters);
		if(StringUtils.startsWith(formatedParameters, "\"")
			 && StringUtils.endsWith(formatedParameters,"\"")){
			formatedParameters=formatedParameters.substring(formatedParameters.indexOf("\"")+1,formatedParameters.lastIndexOf("\""));
		}
		try {
			value = new ParameterList(formatedParameters).get(key);
		} catch (ParseException e) {}
		if(value==null){
			return null;
		}
		String regex = "[, ]+(and|or)*[, ]*";
		return value.split(regex);
	}

	
}
