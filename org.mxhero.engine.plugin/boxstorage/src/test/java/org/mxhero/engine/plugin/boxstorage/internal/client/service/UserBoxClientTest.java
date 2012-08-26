package org.mxhero.engine.plugin.boxstorage.internal.client.service;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemCollection;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ErrorResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.ApplicationKey;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.mxhero.engine.plugin.storageapi.UserResult;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UserBoxClientTest {
	
	UserBoxClient target;
	@Mock
	private BoxApi connector;
	@Mock
	private ClientStoragePersistence persistence;

	@Before
	public void setUp() throws Exception {
		target = new UserBoxClient();
		target.setConnector(connector);
		target.setPersistence(persistence);
	}

	@Test
	public void testStore() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_get_folder_mx_hero(true);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(1)).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).shareFile(any(UserBoxClient.class));
	}

	@Test
	public void testStore_get_account_error() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(true);
		mock_get_folder_mx_hero(true);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, never()).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).shareFile(any(UserBoxClient.class));
	}

	@Test
	public void testStore_get_folder_mxhero_not_exists() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_get_folder_mx_hero(false);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(1)).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, times(1)).createMxHeroFolder(any(UserBoxClient.class));
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).shareFile(any(UserBoxClient.class));
	}

	@Test
	public void testStore_get_folder_mxhero_exception() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_get_folder_mx_hero_exception();
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(1)).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, times(1)).createMxHeroFolder(any(UserBoxClient.class));
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).shareFile(any(UserBoxClient.class));
	}

	@Test
	public void testStore_store_exception() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_get_folder_mx_hero(true);
		mock_store_call(true);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(1)).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).shareFile(any(UserBoxClient.class));
	}

	private void mock_get_folder_mx_hero_exception() {
		ItemResponse value2 = new ItemResponse();
		ItemCollection item_collection = new ItemCollection();
		Item item = new Item();
		item.setName("other");
		item_collection.setEntries(Lists.newArrayList(item));
		value2.setItem_collection(item_collection);
		when(connector.getFolderMxHero(any(UserBoxClient.class))).thenReturn(value2);
		Item folder = new Item();
		folder.setName("mxhero");
		when(connector.createMxHeroFolder(any(UserBoxClient.class))).thenReturn(folder );
	}

	@Test
	public void testStore_has_been_proccessed_ok() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(true);
		target.setEmail("pepe");
		StorageResult store = target.store("algo.txt");
		assertNotNull(store);
		assertTrue(store.isAlreadyProccessed());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, never()).getAccountFromStorage(anyString());
		verify(connector, never()).getFolderMxHero(any(UserBoxClient.class));
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).shareFile(any(UserBoxClient.class));
	}

	private void mock_account_from_storage(boolean exception) {
		CreateTokenResponse value = new CreateTokenResponse();
		if(exception){
			when(persistence.getAccountFromStorage(anyString())).thenThrow(RuntimeException.class);
		}else{
			when(persistence.getAccountFromStorage(anyString())).thenReturn(value);
		}
	}

	private void mock_store_call(boolean exception) {
		FileUploadResponse value2 = new FileUploadResponse();
		value2.setResponse(CodeResponse.OK);
		value2.setResponseSharedLink(CodeResponse.OK);
		if(exception){
			when(connector.store(any(UserBoxClient.class), anyString())).thenThrow(RuntimeException.class);
		}else{
			when(connector.store(any(UserBoxClient.class), anyString())).thenReturn(value2);
		}
	}

	private void mock_get_folder_mx_hero(boolean b) {
		ItemResponse value2 = new ItemResponse();
		ItemCollection item_collection = new ItemCollection();
		Item item = new Item();
		item.setId("idasdfafds");
		if(b){
			item.setName("MXHERO");
		}else{
			item.setName("other");
		}
		item_collection.setEntries(Lists.newArrayList(item));
		value2.setItem_collection(item_collection);
		when(connector.getFolderMxHero(any(UserBoxClient.class))).thenReturn(value2);
		if(!b){
			Item folder = new Item();
			folder.setId("fdsafasdf");
			folder.setName("mxhero");
			when(connector.createMxHeroFolder(any(UserBoxClient.class))).thenReturn(folder );
		}
	}

	@Test
	public void testHasPreviousAccount() {
		CreateTokenResponse mock = mock(CreateTokenResponse.class);
		target.setAccount(mock);
		when(mock.alreadyExist()).thenReturn(true);
		assertTrue(target.hasPreviousAccount());
	}

	@Test
	public void testWasCreatedOk() {
		CreateTokenResponse mock = mock(CreateTokenResponse.class);
		target.setAccount(mock);
		when(mock.wasResponseOk()).thenReturn(true);
		assertTrue(target.wasCreatedOk());
	}

	@Test
	public void testProcess() {
		target.setEmail("pepe@pepe.com");
		target.setRequest(new UserRequest("pepe@pepe.com",true));
		target.setBodyUrl("fdsjfdsfsf  %s");
		when(connector.getAppKey(anyString())).thenReturn("app_key");
		CreateTokenResponse value = new CreateTokenResponse();
		value.setPreviousAccount(true);
		value.setError(new ErrorResponse(CodeResponse.USER_ALREADY_EXIST.getMessage()));
		when(connector.createAccount(anyString(), anyString())).thenReturn(value);
		UserResult process = target.process();
		assertNotNull(process);
		assertTrue(process.isSender());
		verify(connector,times(1)).getAppKey(anyString());
		verify(connector,times(1)).createAccount(anyString(), anyString());
		verify(persistence, never()).storeToken(any(UserBoxClient.class));
	}

	@Test
	public void testProcess_app_key_exists() {
		target.setEmail("pepe@pepe.com");
		target.setRequest(new UserRequest("pepe@pepe.com",true));
		ApplicationKey.setKey("sasfdsfadsf");
		CreateTokenResponse value = new CreateTokenResponse();
		value.setPreviousAccount(true);
		when(connector.createAccount(anyString(), anyString())).thenReturn(value);
		UserResult process = target.process();
		assertNotNull(process);
		assertTrue(process.isSender());
		verify(connector,never()).getAppKey(anyString());
		verify(connector,times(1)).createAccount(anyString(), anyString());
		verify(persistence, times(1)).storeToken(any(UserBoxClient.class));
	}
}
