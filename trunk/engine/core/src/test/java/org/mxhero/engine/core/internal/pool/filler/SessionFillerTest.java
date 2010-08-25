package org.mxhero.engine.core.internal.pool.filler;

import java.util.Arrays;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;

public class SessionFillerTest {

	@Test
	public void testFillerCompleate() throws MessagingException{
		String userMail = "xxxx@mxhero.com";
		String domainName = "mxhero.com";
		
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
		MimeMail mail = new MimeMail("xxxx@mxhero.com",  Arrays.asList("xxxx@mxhero.com".split(",")), message, "service");

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Assert.assertTrue(filler.fill(ksession, userFinder, domainFinder, mail).equalsIgnoreCase("mxhero.com"));
		
		Assert.assertTrue(ksession.getObjects().size()>1);

	}
	
	@Test
	public void testFillerNoFinders() throws MessagingException{
		PhaseSessionFiller filler = new PhaseSessionFiller();
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSubject("test");
		message.setText("test");
		message.setSender(new InternetAddress("xxxx@mxhero.com"));
		message.setFrom(new InternetAddress("xxxx@mxhero.com"));
		Address[] address = new Address[1];
		address[0]= new InternetAddress("xxxx@mxhero.com");
		message.setRecipients(RecipientType.TO,address);
		MimeMail mail = new MimeMail("xxxx@mxhero.com",  Arrays.asList("xxxx@mxhero.com".split(",")), message, "service");

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		Assert.assertTrue(filler.fill(ksession, null, null, mail).equalsIgnoreCase("mxhero.com"));
		
		Assert.assertTrue(ksession.getObjects().size()>1);

	}
	
}
