package org.mxhero.engine.core.internal.pool;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.junit.Test;
import org.mxhero.engine.core.internal.pool.filler.PhaseSessionFiller;
import org.mxhero.engine.core.internal.pool.processor.DefaultRulesProcessor;
import org.mxhero.engine.core.internal.queue.InputQueue;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;

public class SenderRuleTaskTest {

	@Test
	public void testTask() throws AddressException, MessagingException{
		String userMail = "xxxx@mxhero.com";
		String domainName = "mxhero.com";
		
		Core core = new Core();
		
		final User user = new User();
		user.setMail(userMail);
		user.setAliases(Arrays.asList(userMail.split(",")));
		
		final Domain domain = new Domain();
		domain.setAliases(Arrays.asList(domainName.split(",")));
		domain.setId(domainName);
		
		PhaseSessionFiller filler = new PhaseSessionFiller();
		UserFinder userFinder = new UserFinder() {
			
			@Override
			public User getUser(String mailAdress, String domainId) {
				return user;
			}
		};
		
		DomainFinder domainFinder = new DomainFinder() {
			
			@Override
			public Domain getDomain(String mailAdress) {
				return domain;
			}
		};
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSubject("test");
		message.setText("test");
		message.setSender(new InternetAddress("xxxx@mxhero.com"));
		message.setFrom(new InternetAddress("xxxx@mxhero.com"));
		Address[] address = new Address[1];
		address[0]= new InternetAddress("xxxx@mxhero.com");
		message.setRecipients(RecipientType.TO,address);
		MimeMail mail = new MimeMail("xxxx@mxhero.com",  "xxxx@mxhero.com", message, "service");

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		DefaultRulesProcessor processor = new DefaultRulesProcessor();
		processor.setProperties(core);
		
		SenderRuleTask task = new SenderRuleTask(kbase, mail, domainFinder, userFinder);
		task.setFiller(filler);
		task.setProcessor(processor);
		task.setProperties(core);
		task.setLogRecordService(new LogRecord() {
			
			@Override
			public void log(MimeMail mail) {
				System.out.println("log record:"+mail.toString());
				
			}
		});
		task.setLogStatService(new LogStat() {
			
			@Override
			public void log(MimeMail mail, String key, String value) {
				System.out.println("log stat:"+mail.toString());
				
			}
		});
	
		Thread thread = new Thread(task);
		thread.start();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		Assert.assertFalse(InputQueue.getInstance().isEmpty());
	}
}
