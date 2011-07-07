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
@ContextConfiguration(locations = { "/full-test-bundle-context.xml" })
public class TestQueueFull {

	private static Logger log = LoggerFactory.getLogger(TestQueueFull.class);

	private int workThread = 4;
	private boolean fullDebug = false;

	@Autowired
	private MimeMailQueueService service;

	private boolean keepWroking = true;
	private long waitTime = 1000;

	private MimeMessage message = null;

	@Test
	public void testStart() throws AddressException, InterruptedException,
			MessagingException, URISyntaxException {

		message = createMessage();

		Collection<Thread> workers = new ArrayList<Thread>();

		for (int i = 0; i < workThread; i++) {
			workers.add(new ConnectorInWorker());
		}

		for (Thread thread : workers) {
			thread.start();
		}
		
		while (keepWroking) {
			Thread.sleep(10);
			MimeMail mail = new MimeMail("sender@example.com",
					"recipient@example.com", message, "service");
			mail.setPhase(RulePhase.SEND);
			if (!service.offer("connector", "in", mail)){
				break;
			}
			log.debug(service.getQueuesCount().toString());
		}

		for(int i=0;i<10;i++){
			Thread.sleep(500);
			log.debug("Queues are full");
			log.debug(service.getQueuesCount().toString());
		}
		MimeMail dequeuedMail = null;
		do{
			dequeuedMail = service.poll("core", "inSend",1000,TimeUnit.MILLISECONDS);
			if(dequeuedMail!=null){
				service.remove("core", "inSend", dequeuedMail);
			}
			log.debug(service.getQueuesCount().toString());
		}while(dequeuedMail!=null);

		
		log.debug("Queues are empty");
		
		keepWroking = false;
		
		for (Thread thread : workers) {
			thread.join();
		}
		
		log.debug(service.getQueuesCount().toString());
	}

	public MimeMailQueueService getService() {
		return service;
	}

	public void setService(MimeMailQueueService service) {
		this.service = service;
	}

	private MimeMessage createMessage() throws AddressException,
			MessagingException, URISyntaxException {
		MimeMessage message = new MimeMessage(
				Session.getDefaultInstance(new Properties()));
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

	class ConnectorInWorker extends Thread {
		@Override
		public void run() {
			MimeMail mail = null;
			try {
				while (keepWroking) {
					mail = service.poll("connector", "in", waitTime,
							TimeUnit.MILLISECONDS);
					log.debug("try to take and move");
					if (mail != null) {
						if (fullDebug) {
							log.debug("******Polled mail from connector-in*******");
							service.logState();
						}

						try{
							MimeMail newmail = new MimeMail("sender@example.com",
									"recipient@example.com", message, "service");
							newmail.setPhase(RulePhase.RECEIVE);
							service.removeAddTo("connector", "in", mail, newmail, "core", "inSend");
							log.debug("MOVED");							if (fullDebug) {
							log.debug("******mOVED from connector-in and added to core-in*******");
								service.logState();
							}
						}catch (InterruptedException e){
								e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}



}