package org.mxhero.engine.commons.util;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public abstract class HeaderUtils {

	private static Logger log = LoggerFactory.getLogger(HeaderUtils.class);
	public static final String ACTION_PARAMETER="action";
	
	private HeaderUtils() {
	}
	
	
	public static final ParameterList getParametersList(String[] valuesArray,String action, String parameter){
		if(valuesArray!=null && valuesArray.length>0 && action!=null){
			log.debug("has values");
			for(String value : valuesArray){
				log.debug("checking value:"+value);
				if(value!=null){
					value = value.trim();
					if(value.startsWith("\"") && value.endsWith("\"")){
						value=value.substring(1,value.length()-1).trim();
					}
					if(!value.startsWith(";")){
						value=";"+value;
					}
					log.debug("formated value:"+value);
					try {
						ParameterList parameterList = new ParameterList(value);
						if(parameterList.get(parameter)!=null
								&& parameterList.get(parameter).equalsIgnoreCase(action)){
							log.debug("found:"+parameterList.toString());
							return parameterList;
						}
					} catch (ParseException e) {
						log.debug(e.getMessage());
						return null;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @param valuesArray
	 * @param action
	 * @return
	 */
	public static final ParameterList getParametersList(String[] valuesArray,String action){
		return getParametersList(valuesArray, action, ACTION_PARAMETER);
	}
	
}
