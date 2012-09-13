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

package org.mxhero.engine.commons.mail.api;

import java.util.Collection;

/**
 * Represents the headers of an email so it can be used in rules.
 * @author mmarmol
 */
public interface Headers {

	/**
	 * @return
	 */
	public Collection<String> getAllHeaderLines();
	
	/**
	 * @param header
	 * @param value
	 */
	public boolean addHeader(String header, String value);

	/**
	 * @param headerLine
	 * @return
	 */
	public boolean addHeaderLine(String headerLine);
	
	/**
	 * @param header
	 * @param Value
	 */
	public boolean setHeaderValue(String header, String value);
	
	/**
	 * @param header
	 * @return
	 */
	public String getHeaderValue(String header);

	/**
	 * @param header
	 * @return
	 */
	public Collection<String> getHeaderValues(String header);
	
	/**
	 * @param header
	 */
	public boolean removeHeader(String header);
	
	/**
	 * @param header
	 * @return
	 */
	public Boolean hasHeader(String header);
	
}
