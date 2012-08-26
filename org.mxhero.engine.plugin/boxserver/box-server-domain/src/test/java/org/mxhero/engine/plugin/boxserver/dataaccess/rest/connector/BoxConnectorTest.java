package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.BoxConnector;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.AbstractResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.response.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class BoxConnectorTest {
	
	BoxConnector target;
	
	@Mock
	RestTemplate template;
	
	@Mock
	Map<String, ResponseHandler<? extends AbstractResponse>> map;

	@Before
	public void setUp() throws Exception {
		target = new BoxConnector();
		target.setApiKey("asdfasfaf");
		target.setCreateTokenUrl("fdsafas");
		target.setUploadFilesUrl("fdsfasda");
		target.setHandler(map);
		target.setTemplate(template);
	}

	@Test
	public void testCreateAccount() {
		ResponseHandler mock = Mockito.mock(ResponseHandler.class);
		when(map.get(anyString())).thenReturn(mock);
		when(mock.handleReponse(any(ResponseEntity.class))).thenReturn(new CreateUserResponse());
		CreateUserResponse createAccount = target.createAccount("pepe");
		assertNotNull(createAccount);
		verify(map, only()).get(anyString());
		verify(mock, only()).handleReponse(any(ResponseEntity.class));
	}

}
