package org.mxhero.engine.core.internal.service;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.core.internal.queue.InputQueue;
import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreInputServiceTest {

	private static Logger log = LoggerFactory.getLogger(CoreInputServiceTest.class);
	
	@Test
	public void addMailTest() throws MessagingException{
		InputService service = new CoreInputService();
		MimeMail mail = new MimeMail("from@mail.com","to@mail.com","mail data".getBytes(),"service");
		try{
			service.addMail(null);
			Assert.fail("We cant allow any null param");
		} catch (IllegalArgumentException e){
			log.info("We dont allow null values");
		}

		service.addMail(mail);
		Assert.assertNotNull(InputQueue.getInstance().element());
	}
	
}
