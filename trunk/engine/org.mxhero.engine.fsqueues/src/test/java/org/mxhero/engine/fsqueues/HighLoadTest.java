package org.mxhero.engine.fsqueues;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighLoadTest {

	private static Logger log = LoggerFactory.getLogger(HighLoadTest.class);
	
	private FSQueueService srv;

	private boolean work = true;
	private int workersCount = 10;
	private Collection<Thread> workers = new ArrayList<Thread>();
	
	private int capacity = 500;
	private AtomicInteger sendCount = new AtomicInteger(0);
	private AtomicInteger receiveCount = new AtomicInteger(0);
	private AtomicInteger outCount = new AtomicInteger(0);

	
	@Test
	public void test() throws IOException {
		FSConfig config = new FSConfig("C:\\temp\\store", "C:\\temp\\tmp");
		config.setCapacity(capacity);
		srv = new FSQueueService(config);
		
		for(File tmpFile : config.getTmpPath().listFiles()){
			tmpFile.delete();
		}
		for(File storeFile : config.getStorePath().listFiles()){
			storeFile.delete();
		}
		
		srv.init();
		
		for(int i=0;i<capacity;i++){
			MimeMail mail=null;
			try {
				mail = new MimeMail("sender@example.com", "recipient@example.com", createMessage(), "service");
				try {
					srv.store("send", mail, 1000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		srv.logState();
		
		for(int i=0;i<workersCount;i++){
			workers.add(new Thread(new SendWorker()));
			workers.add(new Thread(new ReceiveWorker()));
			workers.add(new Thread(new OutWorker()));
		}
		for(Thread thread : workers){
			thread.start();
		}
		
		while(srv.size()>0){
			try {
				Thread.sleep(1000);
				srv.logState();
				log.info("sendCount="+sendCount.get()+",receiveCount="+receiveCount.get()+",outCount="+outCount.get());

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.gc();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
			
		
		work=false;
		
		for(Thread worker : workers){
			try {
				worker.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		srv.stop();
	}
	
	private class SendWorker implements Runnable {
		public void run() {
			while(work){
				try {
					MimeMail mail= srv.poll("send", 1000, TimeUnit.MILLISECONDS);
					if(mail!=null){
						mail.getMessage().saveChanges();
						if(srv.offer("receive", mail, 1000, TimeUnit.MILLISECONDS)){
							sendCount.addAndGet(1);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ReceiveWorker implements Runnable {
		public void run() {
			while(work){
				try {
					MimeMail mail= srv.poll("receive", 1000, TimeUnit.MILLISECONDS);
					if(mail!=null){
						mail.getMessage().saveChanges();
						if(srv.offer("out", mail, 1000, TimeUnit.MILLISECONDS)){
							receiveCount.addAndGet(1);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class OutWorker implements Runnable {
		public void run() {
			while(work){
				try {
					MimeMail mail= srv.poll("out", 1000, TimeUnit.MILLISECONDS);
					if(mail!=null){
						srv.unstore(mail);
						outCount.addAndGet(1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private InputStream createMessage() throws AddressException,
			MessagingException, URISyntaxException, IOException {
		return new FileInputStream(new File(this.getClass().getClassLoader().getResource("pfxc5300455480221297252.eml").toURI()));
	}
}
