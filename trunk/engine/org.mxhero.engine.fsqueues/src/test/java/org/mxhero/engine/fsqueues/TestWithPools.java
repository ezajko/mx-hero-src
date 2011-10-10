package org.mxhero.engine.fsqueues;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.queue.QueueTaskPool;
import org.mxhero.engine.fsqueues.integration.OutPool;
import org.mxhero.engine.fsqueues.integration.ReceivePool;
import org.mxhero.engine.fsqueues.integration.SendPool;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;

public class TestWithPools {

	private FSQueueService srv;
	private QueueTaskPool sendPool = null;
	private QueueTaskPool receivePool = null;
	private QueueTaskPool outPool = null;
	private Collection<Thread> workers = new ArrayList<Thread>();

	public void init() {
		FSConfig config = new FSConfig("C:\\temp\\store", "C:\\temp\\tmp");
		config.setCapacity(10000);
		srv = new FSQueueService(config);

		for (File tmpFile : config.getTmpPath().listFiles()) {
			tmpFile.delete();
		}
		for (File storeFile : config.getStorePath().listFiles()) {
			storeFile.delete();
		}

		srv.init();
		
		sendPool = new SendPool(srv);
		receivePool = new ReceivePool(srv);
		outPool = new OutPool(srv);
		sendPool.start();
		receivePool.start();
		outPool.start();
	}

	@Test
	public void test() {
		init();
		for(int i=0;i<20;i++){
			workers.add(new Thread(new MessageSender()));
		}
		for(Thread thread : workers){
			thread.start();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(srv.size()>0){
			try {
				srv.logState();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(Thread worker : workers){
			try {
				worker.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		srv.logState();
		finish();
	}

	private InputStream createMessage() throws AddressException,
			MessagingException, URISyntaxException, IOException {
		return new FileInputStream(new File(this.getClass().getClassLoader()
				.getResource("pfxc5300455480221297252.eml").toURI()));
	}

	public void finish() {
		sendPool.stop();
		receivePool.stop();
		outPool.stop();
		srv.stop();
	}

	private class MessageSender implements Runnable{

		public void run() {
			for(int i=0;i<20;i++){
				try {
					srv.store(RulePhase.SEND, new MimeMail("sender@sender.com", "recipient@recipient.com", createMessage(), "service"), 1000, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
