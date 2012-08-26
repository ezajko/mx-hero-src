package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response;

import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.springframework.http.ResponseEntity;

/**
 * The Class FileUploadResponseHandler.
 */
public class FileUploadResponseHandler extends ResponseHandler<FileUploadResponse>{

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.boxstorage.internal.service.internal.box.dataaccess.rest.connector.response.ResponseHandler#handleConcreteResponse(org.springframework.http.ResponseEntity)
	 */
	@Override
	protected CodeResponse handleConcreteResponse(
			ResponseEntity<? extends AbstractResponse> response) {
		CodeResponse result = CodeResponse.UNKNOWN;
		switch (response.getStatusCode()) {
		case BAD_REQUEST:
			result = CodeResponse.BAD_PARAMETERS;
			break;
		case CONFLICT:
			result = CodeResponse.FILES_COULDNT_BE_UPLOADED;
			break;
		case OK:
			result = CodeResponse.OK;
			break;
		case CREATED:
			result = CodeResponse.OK;
			break;
		default:
			break;
		}
		return result;
	}

}
