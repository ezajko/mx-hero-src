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

package org.mxhero.engine.commons.util;

import java.util.ArrayList;
import java.util.Collection;

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
	
	public static final ParameterList getParametersList(String[] valuesArray, String action, String parameter){
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
	
	public static final Collection<ParameterList> getParametersListLike(String[] valuesArray, String like, String parameter){
		Collection<ParameterList> parametersLike = new ArrayList<ParameterList>();
		if(valuesArray!=null && valuesArray.length>0 && like!=null){
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
								&& parameterList.get(parameter).startsWith(like)){
							log.debug("found:"+parameterList.toString());
							parametersLike.add(parameterList);
						}
					} catch (ParseException e) {
						log.warn(e.getMessage());
					}
				}
			}
		}
		return parametersLike;
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
