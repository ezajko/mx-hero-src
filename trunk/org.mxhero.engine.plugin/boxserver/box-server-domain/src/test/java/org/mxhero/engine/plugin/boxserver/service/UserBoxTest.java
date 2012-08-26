package org.mxhero.engine.plugin.boxserver.service;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxserver.dataaccess.persistence.StoragePersistence;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.UserAccountAccess;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.CreateUserResponse;
import org.mxhero.engine.plugin.boxserver.service.UserBox;

@RunWith(MockitoJUnitRunner.class)
public class UserBoxTest {
	
	UserBox target;
	@Mock
	private UserAccountAccess connector;
	@Mock
	private StoragePersistence persistence;

	@Before
	public void setUp() throws Exception {
		target = new UserBox();
		target.setConnector(connector);
		target.setPersistence(persistence);
	}

	@Test
	public void testCreateAccount() {
		CreateUserResponse value = new CreateUserResponse();
		value.setResponse(CodeResponse.OK);
		when(connector.createAccount(anyString())).thenReturn(value);
		target.createAccount();
		inOrder(connector,persistence);
		verify(connector, only()).createAccount(anyString());
		verify(persistence, only()).storeAuthToken(any(UserBox.class));
	}

	@Test
	public void testCreateAccount_user_already_exists() {
		CreateUserResponse value = new CreateUserResponse();
		value.setResponse(CodeResponse.USER_ALREADY_EXIST);
		when(connector.createAccount(anyString())).thenReturn(value);
		target.createAccount();
		inOrder(connector);
		verify(connector, only()).createAccount(anyString());
		verify(persistence, never()).storeAuthToken(any(UserBox.class));
	}

	@Test
	public void testCreateAccount_exception_box() {
		when(connector.createAccount(anyString())).thenThrow(RuntimeException.class);
		target.createAccount();
		verify(connector, only()).createAccount(anyString());
		verify(persistence, never()).storeAuthToken(any(UserBox.class));
	}

	@Test
	public void testHasPreviousAccount_true() {
		target.setAccount(new CreateUserResponse());
		target.getAccount().setResponse(CodeResponse.USER_ALREADY_EXIST);
		boolean hasPreviousAccount = target.hasPreviousAccount();
		assertTrue(hasPreviousAccount);
	}

	@Test
	public void testHasPreviousAccount_false() {
		target.setAccount(new CreateUserResponse());
		target.getAccount().setResponse(CodeResponse.FILES_COULDNT_BE_UPLOADED);
		boolean hasPreviousAccount = target.hasPreviousAccount();
		assertFalse(hasPreviousAccount);
	}
	
	@Test
	public void testRetrieveToken_ok(){
		CreateUserResponse value = new CreateUserResponse();
		value.setResponse(CodeResponse.OK);
		value.setToken("fasfddsafs");
		when(persistence.getAccountFromStorage(anyString())).thenReturn(value);
		target.retrieveToken();
		assertNotNull(target.getAccount());
		assertTrue(target.getAccount().wasResponseOk());
	}


	@Test
	public void testRetrieveToken_fail(){
		when(persistence.getAccountFromStorage(anyString())).thenReturn(null);
		target.retrieveToken();
		assertNotNull(target.getAccount());
		assertFalse(target.getAccount().wasResponseOk());
	}
}
