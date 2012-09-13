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

package org.mxhero.console.backend.infrastructure;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

public class SendRedirectOverloadedResponse extends HttpServletResponseWrapper {

	private static Logger log = Logger.getLogger(SendRedirectOverloadedResponse.class);
	    private HttpServletRequest m_request;
	    private String prefix = null;

	    public SendRedirectOverloadedResponse(HttpServletRequest inRequest, HttpServletResponse response) {
	        super(response);
	        m_request = inRequest;
	        prefix = getPrefix(inRequest);
	    }

	    @Override
	    public void sendRedirect(String location) throws IOException {
	    	log.debug("Going originally to:" + location);
	        String finalurl = null;

	        if (isUrlAbsolute(location)) {
	        	log.debug("This url is absolute. No scheme changes will be attempted");
	            finalurl = location;
	        } else {
	            finalurl = fixForScheme(prefix + location);
	            log.debug("Going to absolute url:" + finalurl);
	        }
	        super.sendRedirect(finalurl);
	    }

	    public boolean isUrlAbsolute(String url) {
	        String lowercaseurl = url.toLowerCase();
	        if (lowercaseurl.startsWith("http") == true) {
	            return true;
	        } else {
	            return false;
	        }
	    }

	    public String fixForScheme(String url) {
	        //alter the url here if you were to change the scheme
	        return url;
	    }

	    public String getPrefix(HttpServletRequest request) {
	        StringBuffer str = request.getRequestURL();
	        String url = str.toString();
	        String uri = request.getRequestURI();
	        log.debug("requesturl:" + url);
	        log.debug("uri:" + uri);
	        int offset = url.indexOf(uri);
	        String prefix_t = url.substring(0, offset);
	        log.debug("prefix:" + prefix_t);
	        return prefix_t;
	    }
}
