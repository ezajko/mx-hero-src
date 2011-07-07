package org.mxhero.engine.mqueues;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.h2.util.Profiler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class TestPerformance {

	private static Logger log = LoggerFactory.getLogger(TestPerformance.class);
	
	private int testSize = 500;
	private Integer sentSize = 0;
	private int workThread = 16;
	private boolean fullDebug= false;
	
	@Autowired
	private MimeMailQueueService service;

	private boolean keepWroking = true;
	private long waitTime = 1000;
	
	private MimeMessage message = null;
	@Test
	public void testStart() throws AddressException, InterruptedException, MessagingException, URISyntaxException{

		

		message = createMessage();
		
		Collection<Thread> workers = new ArrayList<Thread>();
		
		for(int i=0;i<workThread;i++){
			workers.add(new SendWorker());
			workers.add(new ReceiveWorker());
			workers.add(new OutWorker());

		}
		Profiler prof = new Profiler();
		prof.startCollecting();

		for(int i=0;i<testSize;i++){
			MimeMail mail = new MimeMail("sender@example.com", "recipient@example.com", message, "service");
			mail.setPhase(RulePhase.SEND);
			mail.getProperties().put("one", "one");
			mail.getProperties().put("one", "one");
			mail.getProperties().put("two", "two");
			Assert.assertTrue(service.offer("core", "send",mail));
		}
		
		for(Thread thread : workers){
			thread.start();
		}
		long startTime = System.currentTimeMillis();
		while(keepWroking){
			Thread.sleep(waitTime);
			if(!(sentSize<testSize)){
					keepWroking=false;
			}
			log.debug("Test Size:"+testSize+" Sent:"+sentSize);
			log.debug(service.getQueuesCount().toString());
		}
		
		for(Thread thread : workers){
			thread.join();
		}
		
		// .... some long running process, at least a few seconds
		prof.stopCollecting();
		System.out.println(prof.getTop(5));
		log.debug("Total Time in seconds:"+(int)((System.currentTimeMillis()-startTime)/1000));
		log.debug("Test Size:"+testSize+" Sent:"+sentSize);
		log.debug(service.getQueuesCount().toString());
	}
	
	public MimeMailQueueService getService() {
		return service;
	}

	public void setService(MimeMailQueueService service) {
		this.service = service;
	}
	
	private MimeMessage createMessage() throws AddressException, MessagingException, URISyntaxException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("sender@example.com"));
		message.setFrom(new InternetAddress("sender@example.com"));
		message.setRecipient(RecipientType.TO, new InternetAddress(
				"recipient@example.com"));
		message.setSubject("subject for you");

		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText("text goes here");
		MimeBodyPart mbp2 = new MimeBodyPart();
		FileDataSource fds2 = new FileDataSource(new File(this.getClass()
				.getClassLoader().getResource("log4j.properties").toURI()));
		mbp2.setDataHandler(new DataHandler(fds2));
		mbp2.setFileName(fds2.getName());
		mbp2.setDisposition(Part.ATTACHMENT);
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);
		message.setContent(mp);
		message.setSentDate(Calendar.getInstance().getTime());
		message.saveChanges();

		return message;
	}
	
	
	class SendWorker extends Thread{
		@Override
		public void run() {
			MimeMail mail=null;
			try {
				while(keepWroking){
					mail = service.poll("core", "send",waitTime, TimeUnit.MILLISECONDS);
					if(mail!=null){
						if(fullDebug){
							log.debug("******Polled mail from core-send*******");
							service.logState();
						}
						MimeMail newMail = new MimeMail("sender@example.com", "recipient@example.com", message, "service");
						mail.setPhase(RulePhase.RECEIVE);	
						mail.getProperties().put("one", "one");
						mail.getProperties().put("one", "one");
						mail.getProperties().put("two", "two");
						service.removeAddTo("core", "send", mail, newMail, "core", "receive");
							if(fullDebug){
								log.debug("******remove from core-send and added to core-receive*******");
								service.logState();
							}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Assert.fail();
			} catch (MessagingException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}
	class ReceiveWorker extends Thread{
		@Override
		public void run() {
			MimeMail mail = null;
			while(keepWroking){
				try {
					mail = service.poll("core", "receive",waitTime, TimeUnit.MILLISECONDS);
					if(mail!=null){
						if(fullDebug){
							log.debug("******Polled mail from core-receive*******");
							service.logState();
						}
						service.removeAddTo("core", "receive",mail, mail, "core", "out");
						if(fullDebug){
							log.debug("******Removed from core-receive and added to core-out*******");
							service.logState();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	class OutWorker extends Thread{
		@Override
		public void run() {
			MimeMail mail = null;
			while(keepWroking){
				try {
					mail = service.poll("core", "out",waitTime, TimeUnit.MILLISECONDS);
					if(mail!=null){
						if(fullDebug){
							log.debug("******Polled mail from core-inReceive*******");
							service.logState();
						}

							service.remove("core", "out",mail);
							if(fullDebug){
								log.debug("******Removed from core-out*******");
								service.logState();
							}
							synchronized (sentSize) {
								++sentSize;
							}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		}
	}
}
