package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response.CreateAccountResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreateAccountResponseHandlerTest extends ResponseHandlerTest{

	@Before
	public void setUp() throws Exception {
		target = new CreateAccountResponseHandler();
	}


	@Test
	public void testHandleConcreteResponse_user_already_exists() {
		ResponseEntity<AbstractResponse> mock = Mockito.mock(ResponseEntity.class);
		Mockito.when(mock.getStatusCode()).thenReturn(HttpStatus.CONFLICT);
		CodeResponse response = target.handleConcreteResponse(mock);
		assertEquals(CodeResponse.USER_ALREADY_EXIST, response);
	}

}
