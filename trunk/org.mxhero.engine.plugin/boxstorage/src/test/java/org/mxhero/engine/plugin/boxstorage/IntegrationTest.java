package org.mxhero.engine.plugin.boxstorage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.boxstorage.internal.client.BoxCloudStorageClient;
import org.mxhero.engine.plugin.storageapi.StorageResult;
import org.mxhero.engine.plugin.storageapi.UserResulType;
import org.mxhero.engine.plugin.storageapi.UserResult;
import org.mxhero.engine.plugin.storageapi.UserResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:box_context_client_test.xml")
public class IntegrationTest {
	
	@Autowired
	BoxCloudStorageClient target;
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Ignore
	@Test
	public void test_proccess_attachments() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("EMAILS_TO_PROCCESS", "jproyo@mxhero.com,juanpablo.royo@gmail.com");
		params.put("jproyo@mxhero.com", "true");
		params.put("juanpablo.royo@gmail.com", "false");
		Map<UserResulType, UserResult> process = target.process(params);
		assertNotNull(process);
		assertFalse(process.isEmpty());
		UserResult userResultSender = process.get(UserResulType.SENDER);
		assertTrue(userResultSender.isSucess());
		assertTrue(userResultSender.isAlreadyExist());
		assertEquals(UserResultMessage.SEND_LINK, userResultSender.getMessage());
		UserResult userResultRecipient = process.get(UserResulType.RECIPIENT);
		assertTrue(userResultRecipient.isSucess());
		assertTrue(userResultRecipient.isAlreadyExist());
		assertEquals(UserResultMessage.SEND_LINK, userResultRecipient.getMessage());
		assertEquals(UserResultMessage.SEND_HTML_WITH_LINK, userResultRecipient.getMessage());
	}

	@Ignore
	@Test
	public void test_store_file() {
		File createTempFile = null;
		FileOutputStream fileOutputStream = null;
		try {
			createTempFile = File.createTempFile("algo_nuevo", ".txt");
			fileOutputStream = new FileOutputStream(createTempFile);
			fileOutputStream.write("Prueba mxhero con Box".getBytes());
			fileOutputStream.flush();
		} catch (IOException e) {
			fail();
		}finally{
			try {
				if(fileOutputStream!=null)fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StorageResult result = target.store("jproyo@mxhero.com", createTempFile.getAbsolutePath());
		assertNotNull(result);
		assertTrue(result.isSuccess());
	}


}
