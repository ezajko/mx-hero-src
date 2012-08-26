package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response;

import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CodeResponse;
import org.springframework.http.ResponseEntity;

/**
 * The Class ResponseHandler.
 *
 * @param <T> the generic type
 */
public abstract class ResponseHandler<T extends AbstractResponse> {
	
	/**
	 * Handle reponse.
	 *
	 * @param response the response
	 * @return the abstract response
	 */
	public AbstractResponse handleReponse(ResponseEntity<? extends AbstractResponse> response){
		AbstractResponse body = response.getBody();
		body.setResponse(handleConcreteResponse(response));
		return body;
	}

	/**
	 * Handle concrete response.
	 *
	 * @param response the response
	 * @return the code response
	 */
	protected abstract CodeResponse handleConcreteResponse(ResponseEntity<? extends AbstractResponse> response);
	
}
