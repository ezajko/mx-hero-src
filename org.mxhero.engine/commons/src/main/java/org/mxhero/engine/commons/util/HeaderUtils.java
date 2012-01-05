package org.mxhero.engine.commons.util;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

public abstract class HeaderUtils {

	public static String[] parseParameters(String parameters, String key){		
		String value = null;
		if(parameters==null || key == null){
			return null;
		}
		if(!parameters.matches("\\s*;.*")){
			parameters=";"+parameters;
		}
		try{
			value = new ParameterList(parameters).get(key);
		}catch (ParseException e){}
		if(value==null){
			return null;
		}
		String regex = "[, ]+(and|or)*[, ]*";
		return value.split(regex);
	}

	
}
