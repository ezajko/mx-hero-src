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
