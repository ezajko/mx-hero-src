package org.mxhero.engine.core.internal.pool.processor;

import java.util.ArrayList;
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
import org.mxhero.engine.core.internal.pool.filler.PhaseSessionFiller;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.Group;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.business.UserList;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.domain.mail.finders.UserFinder;

public class DefaultRulesProcessorTest {

	@Test
	public void testProcessor() throws MessagingException {
		PhaseSessionFiller filler = new PhaseSessionFiller();
		
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(null));
		message.setSubject("test");
		message.setText("test");
		message.setSender(new InternetAddress("xxxx@mxhero.com"));
		message.setFrom(new InternetAddress("xxxx@mxhero.com"));
		Address[] address = new Address[1];
		address[0]= new InternetAddress("xxxx@mxhero.com");
		message.setRecipients(RecipientType.TO,address);
		MimeMail mail = new MimeMail("xxxx@mxhero.com", "xxxx@mxhero.com", message, "service");

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		DefaultRulesProcessor processor = new DefaultRulesProcessor();
		processor.setProperties(new Core());
		try{
			processor.process(ksession, filler, null, null, mail);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testProcessorWithFinders() throws MessagingException {
		String userMail = "xxxx@mxhero.com";
		String domainName = "mxhero.com";
		
		final Domain domain = new Domain();
		domain.setAliases(Arrays.asList(domainName.split(",")));
		domain.setId(domainName);
		domain.setManaged(false);
		domain.setAliases(new ArrayList<String>());
		domain.setGroups(new ArrayList<Group>());
		
		final User user = new User();
		user.setMail(userMail);
		user.setAliases(Arrays.asList(userMail.split(",")));
		user.setDomain(domain);
		user.setLists(new ArrayList<UserList>());
		user.setManaged(false);

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
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		DefaultRulesProcessor processor = new DefaultRulesProcessor();
		processor.setProperties(new Core());
		try{
			processor.process(ksession, filler, userFinder, domainFinder, mail);
		} catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
}
