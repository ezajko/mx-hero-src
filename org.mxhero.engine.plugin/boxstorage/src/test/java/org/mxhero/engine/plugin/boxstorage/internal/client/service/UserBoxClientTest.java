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

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.boxstorage.internal.client.StorageResult;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.persistence.ClientStoragePersistence;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.BoxApi;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.CodeResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Entry;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.FileUploadResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemCollection;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.ItemResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain.SharedLink;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.response.ErrorResponse;
import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;
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
	public void testStore_with_sender_and_creations_of_folders() {
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmailDate(new Timestamp(System.currentTimeMillis()));
		tx.setIdMessageAttach(123l);
		tx.setSender(true);
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_load_account(tx, true);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store(tx);
		assertNotNull(store);
		assertTrue(store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(4)).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).updateFileInfo(any(UserBoxClient.class),anyString());
	}

	@Test
	public void testStore_with_recipient_and_creations_of_folders() {
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmailDate(new Timestamp(System.currentTimeMillis()));
		tx.setIdMessageAttach(123l);
		tx.setRecipient(true);
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_load_account(tx, true);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store(tx);
		assertNotNull(store);
		assertTrue(store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(4)).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).updateFileInfo(any(UserBoxClient.class),anyString());
	}

	@Test
	public void testStore_get_account_error() {
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmailDate(new Timestamp(System.currentTimeMillis()));
		tx.setIdMessageAttach(123l);
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(true);
		mock_load_account(tx, true);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store(tx);
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, never()).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).updateFileInfo(any(UserBoxClient.class), anyString());
	}

	@Test
	public void testStore_get_folder_mxhero_not_exists() {
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmailDate(new Timestamp(System.currentTimeMillis()));
		tx.setIdMessageAttach(123l);
		tx.setSender(true);
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_load_account(tx, false);
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store(tx);
		assertNotNull(store);
		assertTrue(store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(4)).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, times(4)).createFolder(any(UserBoxClient.class), anyString(), anyString());
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).updateFileInfo(any(UserBoxClient.class), anyString());
	}

	@Test
	public void testStore_get_folder_mxhero_exception() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_get_folder_mx_hero_exception();
		mock_store_call(false);
		target.setEmail("pepe");
		StorageResult store = target.store(new TransactionAttachment());
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(1)).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).createFolder(any(UserBoxClient.class), anyString(), anyString());
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).updateFileInfo(any(UserBoxClient.class), anyString());
	}

	@Test
	public void testStore_store_exception() {
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmailDate(new Timestamp(System.currentTimeMillis()));
		tx.setIdMessageAttach(123l);
		tx.setSender(true);
		when(persistence.hasBeenProccessed(anyString())).thenReturn(false);
		mock_account_from_storage(false);
		mock_load_account(tx,true);
		mock_store_call(true);
		target.setEmail("pepe");
		StorageResult store = target.store(tx);
		assertNotNull(store);
		assertTrue(!store.isSuccess());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, times(1)).getAccountFromStorage(anyString());
		verify(connector, times(4)).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, times(1)).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).updateFileInfo(any(UserBoxClient.class), anyString());
	}

	private void mock_get_folder_mx_hero_exception() {
		ItemResponse value2 = new ItemResponse();
		ItemCollection item_collection = new ItemCollection();
		Item item = new Item();
		item.setName("other");
		item_collection.setEntries(Lists.newArrayList(item));
		value2.setItem_collection(item_collection);
		when(connector.getFolder(any(UserBoxClient.class), anyString())).thenReturn(value2);
		Item folder = new Item();
		folder.setName("mxhero");
		when(connector.createFolder(any(UserBoxClient.class), anyString(), anyString())).thenReturn(folder );
	}

	@Test
	public void testStore_has_been_proccessed_ok() {
		when(persistence.hasBeenProccessed(anyString())).thenReturn(true);
		target.setEmail("pepe");
		StorageResult store = target.store(new TransactionAttachment());
		assertNotNull(store);
		assertTrue(store.isAlreadyProccessed());
		verify(persistence, times(1)).hasBeenProccessed(anyString());
		verify(persistence, never()).getAccountFromStorage(anyString());
		verify(connector, never()).getFolder(any(UserBoxClient.class), anyString());
		verify(connector, never()).store(any(UserBoxClient.class), anyString());
		verify(connector, never()).updateFileInfo(any(UserBoxClient.class), anyString());
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
		Entry entry = new Entry();
		entry.setId("fdsfasdf");
		entry.setShared_link(new SharedLink());
		entry.getShared_link().setDownload_url("fadsfasdf");
		entry.getShared_link().setUrl("fasdfdsaf");
		entry.setType("fadsfads");
		value2.setEntries(Lists.newArrayList(entry));
		if(exception){
			when(connector.store(any(UserBoxClient.class), anyString())).thenThrow(RuntimeException.class);
		}else{
			when(connector.store(any(UserBoxClient.class), anyString())).thenReturn(value2);
		}
	}

	private void mock_load_account(TransactionAttachment tx, boolean isfoldercreatedOk) {
		mock_create_folder("mxHero", "1", "0", isfoldercreatedOk);
		mock_create_folder_inbox_and_sent("1", isfoldercreatedOk);
		String folderParent = "2";
		if(tx.isSender()){
			folderParent = "3";
		}
		mock_create_folder(tx.getFolderName(), "4", folderParent, isfoldercreatedOk);
	}

	private void mock_create_folder_inbox_and_sent(String folderParentId, boolean b) {
		ItemResponse value2 = new ItemResponse();
		ItemCollection item_collection = new ItemCollection();
		Item item = new Item();
		item.setId("2");
		if(b){
			item.setName("Inbox");
		}else{
			item.setName("other");
		}
		Item item2 = new Item();
		item2.setId("3");
		if(b){
			item2.setName("Sent");
		}else{
			item2.setName("other");
		}
		item_collection.setEntries(Lists.newArrayList(item, item2));
		value2.setItem_collection(item_collection);
		when(connector.getFolder(any(UserBoxClient.class), Mockito.eq(folderParentId))).thenReturn(value2);
		if(!b){
			Item folder = new Item();
			folder.setId("2");
			folder.setName("Inbox");
			when(connector.createFolder(any(UserBoxClient.class), Mockito.eq("Inbox"), Mockito.eq("1"))).thenReturn(folder );
			Item folder2 = new Item();
			folder2.setId("3");
			folder2.setName("Sent");
			when(connector.createFolder(any(UserBoxClient.class), Mockito.eq("Sent"), Mockito.eq("1"))).thenReturn(folder2 );
		}
	}

	private void mock_create_folder(String folderName, String folderId, String folderParentId, boolean b) {
		ItemResponse value2 = new ItemResponse();
		ItemCollection item_collection = new ItemCollection();
		Item item = new Item();
		item.setId(folderId);
		if(b){
			item.setName(folderName);
		}else{
			item.setName("other");
		}
		item_collection.setEntries(Lists.newArrayList(item));
		value2.setItem_collection(item_collection);
		when(connector.getFolder(any(UserBoxClient.class), Mockito.eq(folderParentId))).thenReturn(value2);
		if(!b){
			Item folder = new Item();
			folder.setId(folderId);
			folder.setName(folderName);
			when(connector.createFolder(any(UserBoxClient.class), Mockito.eq(folderName), Mockito.eq(folderParentId))).thenReturn(folder);
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
		target.setUrlToRetrieveToken("http://localhost");
		target.setHostMxheroServer("https://mxhero.com");
		CreateTokenResponse value = new CreateTokenResponse();
		value.setPreviousAccount(true);
		value.setError(new ErrorResponse(CodeResponse.USER_ALREADY_EXIST.getMessage()));
		when(connector.createAccount(anyString())).thenReturn(value);
		UserResult process = target.process();
		assertNotNull(process);
		assertTrue(process.isSender());
		verify(connector,times(1)).createAccount(anyString());
		verify(persistence, never()).storeToken(any(UserBoxClient.class));
	}

	@Test
	public void testProcess_app_key_exists() {
		target.setEmail("pepe@pepe.com");
		target.setRequest(new UserRequest("pepe@pepe.com",true));
		target.setUrlToRetrieveToken("http://localhost");
		target.setHostMxheroServer("https://mxhero.com");
		ApplicationKey.setKey("sasfdsfadsf");
		CreateTokenResponse value = new CreateTokenResponse();
		value.setPreviousAccount(true);
		when(connector.createAccount(anyString())).thenReturn(value);
		UserResult process = target.process();
		assertNotNull(process);
		assertTrue(process.isSender());
		verify(connector,times(1)).createAccount(anyString());
		verify(persistence, times(1)).storeToken(any(UserBoxClient.class));
	}
}
