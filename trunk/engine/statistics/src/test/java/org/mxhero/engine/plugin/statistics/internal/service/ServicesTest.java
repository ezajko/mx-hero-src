package org.mxhero.engine.plugin.statistics.internal.service;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.statistic.LogRecord;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.statistics.internal.dao.RecordDao;
import org.mxhero.engine.plugin.statistics.internal.dao.StatDao;
import org.mxhero.engine.plugin.statistics.internal.entity.RecordPk;
import org.mxhero.engine.plugin.statistics.internal.entity.StatPk;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class ServicesTest {

	@Autowired
	private ApplicationContext ctx = null;
	
	private MimeMail mail = null;
	
	@Before
	public void before() throws AddressException, MessagingException{
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()));
		message.setSender(new InternetAddress("e@w.c"));
		message.setFrom(new InternetAddress("e@w.c"));
		message.setRecipient(RecipientType.TO, new InternetAddress("x@s.c"));
		message.setText("some text");
		message.setSubject("some subject");
		message.saveChanges();
		mail = new MimeMail("e@w.c","x@s.c", message, "");	
		mail.setSenderDomainId("w.c");
		mail.setSenderId("e@w.c");
	}
	
	@Test
	@DirtiesContext
	public void save() throws AddressException, MessagingException{

		RecordDao recordDao = ctx.getBean(RecordDao.class);
		StatDao statDao = ctx.getBean(StatDao.class);
		LogRecord recordService = ctx.getBean(LogRecord.class);
		recordService.log(mail);
		
		RecordPk recordPk = Utils.createRecord(mail).getId();
		Assert.assertTrue(recordDao.exists(recordPk));
		
		LogStat service = ctx.getBean(LogStat.class);
		service.log(mail, "somekey", "somevalue");
		StatPk statPk = Utils.createStat(mail, "somekey", "somevalue").getId();
		statPk.setInsertDate(recordPk.getInsertDate());
		statPk.setSequence(recordPk.getSequence());
		
		Assert.assertTrue(statDao.exists(statPk));

	}
	
	@Test
	@DirtiesContext
	public void saveOrUpdate(){
		LogRecord recordService = ctx.getBean(LogRecord.class);
		RecordDao recordDao = ctx.getBean(RecordDao.class);
		
		recordService.log(mail);
		recordService.log(mail);
		recordService.log(mail);
		recordService.log(mail);
		
		Assert.assertTrue(recordDao.exists(Utils.createRecord(mail).getId()));
	}

	@Test
	public void wrongParams(){
		LogRecord recordService = ctx.getBean(LogRecord.class);
		recordService.log(null);
		LogStat service = ctx.getBean(LogStat.class);
		service.log(null, null, null);
	}
}
