package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.springframework.http.ResponseEntity;

/**
 * The Class FileUploadResponseHandler.
 */
public class ApiBoxResponseHandler extends ResponseHandler<FileUploadResponse>{

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.dataaccess.rest.connector.response.ResponseHandler#handleConcreteResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	protected CodeResponse handleConcreteResponse(
			ResponseEntity<? extends AbstractResponse> response) {
		int value = response.getStatusCode().value();
		if(value>=300){
			return CodeResponse.BAD_AUTHENTICATION;
		}else{
			return CodeResponse.OK;
		}
	}

}
