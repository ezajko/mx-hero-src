package org.mxhero.engine.commons.util;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

/**
 * @author mmarmol
 *
 */
public abstract class HeaderUtils {

	public static final String ACTION_PARAMETER="action";
	
	private HeaderUtils() {
	}
	
	
	public static final ParameterList getParametersList(String[] valuesArray,String action, String parameter){
		if(valuesArray!=null && valuesArray.length>0 && action!=null){
			for(String value : valuesArray){
				if(value!=null){
					value = value.trim();
					if(value.startsWith("\"") && value.endsWith("\"")){
						value=value.substring(1,value.length()-1).trim();
					}
					if(!value.startsWith(";")){
						value=";"+value;
					}
					try {
						ParameterList parameterList = new ParameterList(value);
						if(parameterList.get(parameter)!=null
								&& parameterList.get(parameter).equalsIgnoreCase(action)){
							return parameterList;
						}
					} catch (ParseException e) {
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
