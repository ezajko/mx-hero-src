package org.mxhero.engine.domain.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Test;

public class QueueTaskPoolTest {

	private boolean cleanCalled = false;
	private boolean initCalled = false;
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private List<String> results = new ArrayList<String>();
	
	@Test
	public void testPool() throws InterruptedException{
		TestPool pool = new TestPool();
		pool.start();
		Thread.sleep(100);
		Assert.assertTrue(initCalled);
		
		queue.add("one");
		queue.add("two");
		queue.add("null");
		pool.setCorePoolsize(null);
		pool.setCorePoolsize("invalid");
		pool.setCorePoolsize("3");
		pool.setKeepAliveTime(null);
		pool.setKeepAliveTime("invalid");
		pool.setKeepAliveTime("3000");
		pool.setMaximumPoolSize(null);
		pool.setMaximumPoolSize("invalid");
		pool.setMaximumPoolSize("8");
		pool.setWaitTime(null);
		pool.setWaitTime("invalid");
		pool.setWaitTime("1000");
		pool.setWaitTime("-1000");
		Thread.sleep(6000);
		pool.stop();
		Assert.assertEquals(2,results.size());
		Assert.assertTrue(cleanCalled);
	}
	
	
	private class TestPool extends QueueTaskPool<String>{

		public TestPool() {
			super(queue);
		}

		@Override
		protected void clean() {
			cleanCalled=true;
			
		}

		@Override
		protected Runnable createTask(final String object) {
			if (object.equals("null")){
				return null;
			}
			return new Runnable() {
				
				@Override
				public void run() {
					results.add(object);
				}
			};
		}

		@Override
		protected void init() {
			initCalled=true;
			
		}
		
	}
	
}
