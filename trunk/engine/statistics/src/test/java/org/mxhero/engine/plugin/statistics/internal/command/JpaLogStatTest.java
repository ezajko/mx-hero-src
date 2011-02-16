package org.mxhero.engine.plugin.statistics.internal.command;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.command.Command;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class JpaLogStatTest {

	@Autowired
	private ApplicationContext ctx = null;
	
	@Test
	public void worngParameters(){
		Command command = (Command)ctx.getBean("jpaLogStatCommand");
		command.exec(null);
		command.exec(null,new String[]{null});
		command.exec(null,null,null);
		command.exec(null,"",null);
		command.exec(null,null,"");
	}
	
	@Test
	public void testSeters(){
		JpaLogStat command = new JpaLogStat();
		command.setDao(null);
		Assert.assertNull(command.getDao());
		command.setRecordDao(null);
		Assert.assertNull(command.getRecordDao());
	}
	
	@Test
	@DirtiesContext
	public void save() throws AddressException, MessagingException{
			
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("e@w.c"));
		message.setFrom(new InternetAddress("e@w.c"));
		message.setRecipient(RecipientType.TO, new InternetAddress("x@s.c"));
		message.setText("some text");
		message.setSubject("some subject");
		message.saveChanges();

		MimeMail mail = new MimeMail("e@w.c","x@s.c", message, "");

		LogRecord recordService = ctx.getBean(LogRecord.class);
		recordService.log(mail);
		
		Command command = (Command)ctx.getBean("jpaLogStatCommand");
		command.exec(mail, "some other key","and his value");
		
	}
}
