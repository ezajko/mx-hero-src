package org.mxhero.engine.plugin.boxstorage.internal.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.attachmentlink.alcommand.service.TransactionAttachment;
import org.mxhero.engine.plugin.boxstorage.internal.client.service.UserBoxClient;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.mxhero.engine.plugin.storageapi.UserResultMessage;
import org.springframework.beans.factory.BeanFactory;

@RunWith(MockitoJUnitRunner.class)
public class BoxCloudStorageClientTest {
	
	BoxCloudStorageClient target;
	@Mock
	private BeanFactory beanFactory;

	@Before
	public void setUp() throws Exception {
		target = new BoxCloudStorageClient();
		target.setBeanFactory(beanFactory);
	}

	@Test
	public void test_process() {
		Map mock = mock(Map.class);
		when(mock.get("senderStorage")).thenReturn("jproyo@mxhero.com");
		UserBoxClient client = mock(UserBoxClient.class);
		when(this.beanFactory.getBean(anyString(), any())).thenReturn(client);
		UserResult value = new UserResult("jproyo@mxhero.com");
		value.setMessage(UserResultMessage.SEND_HTML_WITH_LINK);
		value.setSender(true);
		when(client.process()).thenReturn(value);
		Map<UserResulType, UserResult> process = target.process(mock);
		assertNotNull(process);
		assertFalse(process.isEmpty());
		assertNotNull(process.get(UserResulType.SENDER));
	}

	@Test
	public void test_process_exception() {
		Map mock = mock(Map.class);
		when(mock.get("senderStorage")).thenReturn("jproyo@mxhero.com");
		UserBoxClient client = mock(UserBoxClient.class);
		when(this.beanFactory.getBean(anyString(), any())).thenReturn(client);
		UserResult value = new UserResult("jproyo@mxhero.com");
		value.setMessage(UserResultMessage.SEND_HTML_WITH_LINK);
		value.setSender(true);
		when(client.process()).thenThrow(new RuntimeException());
		Map<UserResulType, UserResult> process = target.process(mock);
		assertNotNull(process);
		assertFalse(process.isEmpty());
		assertNotNull(process.get(UserResulType.ERROR));
	}
	
	@Test
	public void test_store(){
		UserBoxClient client = mock(UserBoxClient.class);
		when(this.beanFactory.getBean(anyString(), any())).thenReturn(client);
		when(client.store(any(TransactionAttachment.class))).thenReturn(new StorageResult(false));
		TransactionAttachment tx = new TransactionAttachment();
		tx.setEmail("pepe@pepe.com");
		tx.setFilePath("/tmp/algo.txt");
		StorageResult store = target.store(tx);
		assertNotNull(store);
	}
}
