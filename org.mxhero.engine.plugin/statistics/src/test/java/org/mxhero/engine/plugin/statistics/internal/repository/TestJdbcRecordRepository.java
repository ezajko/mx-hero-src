package org.mxhero.engine.plugin.statistics.internal.repository;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class TestJdbcRecordRepository {

	@Autowired
	private JdbcRecordRepository repository;


	@Test
	public void test(){
		Record record = new Record();
		record.setBytesSize(10l);
		record.setFlow(Mail.Flow.both.name());
		record.setFrom("Y@Y");
		record.setInsertDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		record.setMessageId("12345@123123");
		record.setPhase(Mail.Phase.send.name());
		record.setRecipient("R@RR");
		record.setRecipientDomainId("RR");
		record.setRecipientId("RID@RR");
		record.setState(Mail.Status.deliver.name());
		record.setSubject("SUB");
		record.setStateReason("UPS!");
		record.setSender("S@SS");
		record.setSenderDomainId("SS");
		record.setSenderId("S");
		record.setSequence(1l);
		record.setServerName("MXHERO");
		repository.saveRecord(record);
		record.setSubject("UPDATED00");
		repository.saveRecord(record);
		
		Stat stat = new Stat();
		stat.setInsertDate(record.getInsertDate());
		stat.setKey("KEY");
		stat.setPhase(record.getPhase());
		stat.setSequence(record.getSequence());
		stat.setValue("VL");
		stat.setServerName("MXHERO");
		repository.saveStat(stat);
		stat.setValue("VL UPDATED");
		repository.saveStat(stat);
	}
	
	public JdbcRecordRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcRecordRepository repository) {
		this.repository = repository;
	}

}
