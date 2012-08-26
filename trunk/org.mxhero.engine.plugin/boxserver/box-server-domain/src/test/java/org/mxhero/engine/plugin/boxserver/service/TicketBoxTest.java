package org.mxhero.engine.plugin.boxserver.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.AuthorizationAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.TicketResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.User;
import org.mxhero.engine.plugin.boxserver.service.TicketBox;
import org.mxhero.engine.plugin.boxserver.service.UserBox;

@RunWith(MockitoJUnitRunner.class)
public class TicketBoxTest {
	
	TicketBox target;
	@Mock
	private AuthorizationAccess access;
	@Mock
	private StoragePersistence persistence;
	@Mock
	private PBEStringEncryptor encryptor;
	

	@Before
	public void setUp() throws Exception {
		target = new TicketBox();
		target.setAccess(access);
		target.setPersistence(persistence);
		target.setEncryptor(encryptor);
		when(encryptor.decrypt(anyString())).thenReturn("pepe");
	}

	@Test
	public void test_register_token() {
		TicketResponse value = new TicketResponse();
		value.setAuth_token("asdfafsdf");
		value.setUser(new User());
		value.getUser().setEmail("fasdfasdf");
		when(access.getUserToken(anyString())).thenReturn(value);
		target.registerToken("addfsadf", "sdafasdfas", "fadsfasd");
		inOrder(access, persistence);
		verify(access,only()).getUserToken(anyString());
		verify(persistence,only()).storeAuthToken(any(UserBox.class));
	}

	@Test
	public void test_create_new_ticket() {
		when(access.createNewTicket()).thenReturn("fdsafasd");
		String newTicket = target.getNewTicket();
		assertNotNull(newTicket);
		inOrder(access);
		verify(access,only()).createNewTicket();
	}
}
