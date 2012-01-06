package org.mxhero.engine.commons.util;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HeaderUtils {

	private static Logger log = LoggerFactory.getLogger(HeaderUtils.class);
	
	public static String[] parseParameters(String parameters, String key){		
		String value = null;
		log.debug("parameters:"+parameters+" ,key:"+key);
		if(parameters==null || key == null){
			return null;
		}
		String formatedParameters = StringUtils.trim(parameters);
		if(StringUtils.startsWith(formatedParameters, "\"")
			 && StringUtils.endsWith(formatedParameters,"\"")){
			formatedParameters=formatedParameters.substring(formatedParameters.indexOf("\"")+1,formatedParameters.lastIndexOf("\""));
		}
		if(!StringUtils.startsWith(formatedParameters,";")){
			formatedParameters=";"+formatedParameters;
		}
		formatedParameters=formatedParameters.replace("\\\"", "\"");
		
		log.debug("formated parameters="+formatedParameters);
		try {
			value = new ParameterList(formatedParameters).get(key);
		} catch (ParseException e) {
			log.warn(e.getMessage());
		}
		if(value==null){
			return null;
		}
		log.debug("value:"+value);
		String regex = "[, ]+(and|or)*[, ]*";
		return value.split(regex);
	}

	
}
