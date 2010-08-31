package org.mxhero.engine.plugin.statistics.internal.dao;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class RecordNamedQueriesTest {

	@Autowired
	private ApplicationContext ctx = null;
	
	@Test
	public void amountOfUserEmailsSince(){
		RecordDao recordDao = ctx.getBean(RecordDao.class);
		System.out.println("++++++++++++++"+recordDao.amountOfUserEmailsSince(new Timestamp(Calendar.getInstance().getTime().getTime()-1000000), "e@w.c", RulePhase.SEND)+"++++++++++++++");
		System.out.println("++++++++++++++"+recordDao.bytesOfUserEmailsSince(new Timestamp(Calendar.getInstance().getTime().getTime()-1000000), "e@w.c", RulePhase.SEND)+"++++++++++++++");
	}
}
