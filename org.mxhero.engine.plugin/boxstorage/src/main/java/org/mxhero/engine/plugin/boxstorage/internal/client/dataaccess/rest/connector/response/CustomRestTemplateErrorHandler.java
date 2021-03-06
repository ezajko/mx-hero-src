package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * The Class CustomRestTemplateErrorHandler is to trap exception and not let them pass.
 */
public class CustomRestTemplateErrorHandler implements ResponseErrorHandler{
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(CustomRestTemplateErrorHandler.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.client.ResponseErrorHandler#hasError(org.springframework.http.client.ClientHttpResponse)
	 */
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.client.ResponseErrorHandler#handleError(org.springframework.http.client.ClientHttpResponse)
	 */
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		logger.debug("Handler http error {}", response.getStatusText());
	}

}
