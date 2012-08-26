package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResponseHandlerTest {
	
	protected ResponseHandler<? extends AbstractResponse> target;


	@Test
	public void testHandleConcreteResponse_ok_status() {
		ResponseEntity<AbstractResponse> mock = Mockito.mock(ResponseEntity.class);
		Mockito.when(mock.getStatusCode()).thenReturn(HttpStatus.OK);
		CodeResponse response = target.handleConcreteResponse(mock);
		assertEquals(CodeResponse.OK, response);
	
		ResponseEntity<AbstractResponse> mock1 = Mockito.mock(ResponseEntity.class);
		Mockito.when(mock1.getStatusCode()).thenReturn(HttpStatus.CREATED);
		CodeResponse response1 = target.handleConcreteResponse(mock1);
		assertEquals(CodeResponse.OK, response1);
	}

	@Test
	public void testHandleConcreteResponse_bad_request() {
		ResponseEntity<AbstractResponse> mock = Mockito.mock(ResponseEntity.class);
		Mockito.when(mock.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
		CodeResponse response = target.handleConcreteResponse(mock);
		assertEquals(CodeResponse.BAD_PARAMETERS, response);
	}

	@Test
	public void testHandleConcreteResponse_unknown() {
		ResponseEntity<AbstractResponse> mock = Mockito.mock(ResponseEntity.class);
		Mockito.when(mock.getStatusCode()).thenReturn(HttpStatus.EXPECTATION_FAILED);
		CodeResponse response = target.handleConcreteResponse(mock);
		assertEquals(CodeResponse.UNKNOWN, response);
	}

}