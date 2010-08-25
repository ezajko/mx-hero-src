package org.mxhero.engine.plugin.postfixconnector.internal.queue;

import java.util.concurrent.BlockingQueue;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.plugin.postfixconnector.queue.InputQueue;
import org.mxhero.engine.plugin.postfixconnector.queue.OutputQueue;

public class QueuesTest {

	@Test
	public void testQueues(){
		BlockingQueue<MimeMail> input=InputQueue.getInstance();
		Assert.assertEquals(input, InputQueue.getInstance());
		
		BlockingQueue<MimeMail> output=OutputQueue.getInstance();
		Assert.assertEquals(output, OutputQueue.getInstance());
	}
}
