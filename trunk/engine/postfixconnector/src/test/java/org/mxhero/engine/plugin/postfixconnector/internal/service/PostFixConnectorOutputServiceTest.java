package org.mxhero.engine.plugin.postfixconnector.internal.service;

import java.util.Arrays;

import javax.mail.MessagingException;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.connector.OutputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.postfixconnector.internal.service.QueuedPostFixConnectorOutputService;
import org.mxhero.engine.plugin.postfixconnector.queue.OutputQueue;

public class PostFixConnectorOutputServiceTest {

	@Test
	public void testService() throws MessagingException{
		OutputQueue.getInstance().clear();
		OutputService service = new QueuedPostFixConnectorOutputService();
		try{
			service.addOutMail(null);
			Assert.fail();
		} catch (NullPointerException e){
		}
		Assert.assertEquals(0, OutputQueue.getInstance().size());
		service.addOutMail(new MimeMail("f",Arrays.asList("r".split(";")),"c".getBytes(),"s"));
		Assert.assertEquals(1, OutputQueue.getInstance().size());
	}
	
}
