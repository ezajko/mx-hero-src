package org.mxhero.engine.plugin.statistics.internal.entity;

import java.sql.Timestamp;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatTest {

	private static Logger log = LoggerFactory.getLogger(StatTest.class);
	
	@Test
	public void testHashEquals(){
		long time = System.currentTimeMillis();

		RecordPk pk1 = new RecordPk();
		pk1.setInsertDate(new Timestamp(time));
		pk1.setSequence(0l);
		Stat stat1 = new Stat();
		stat1.setId(new StatPk());
		stat1.getId().setKey("one");
		stat1.getId().setRecordId(pk1);
		log.info(stat1.toString());
		
		RecordPk pk2 = new RecordPk();
		pk2.setInsertDate(new Timestamp(time));
		pk2.setSequence(1l);
		Stat stat2 = new Stat();
		stat2.setId(new StatPk());
		stat2.getId().setKey("two");
		stat2.getId().setRecordId(pk2);
		log.info(stat2.toString());
		
		Assert.assertTrue(stat1.getId().hashCode()==stat2.getId().hashCode());
		Assert.assertTrue(stat1.hashCode()==stat2.hashCode());
		
		stat2.getId().setRecordId(pk1);
		Assert.assertTrue(stat1.getId().hashCode()==stat2.getId().hashCode());
		Assert.assertTrue(stat1.hashCode()==stat2.hashCode());
		
		Assert.assertFalse(stat1.equals(stat2));
		Assert.assertFalse(stat1.getId().equals(stat2.getId()));
		Assert.assertFalse(stat1.getId().equals(null));
		Assert.assertFalse(stat1.getId().equals(""));
		Assert.assertFalse(stat1.equals(""));
		Assert.assertFalse(stat1.equals(null));
		
		stat2.getId().setKey(stat1.getId().getKey());
		Assert.assertTrue(stat1.equals(stat2));
		log.info(stat1.toString());
		log.info(stat2.toString());
	}
	
	
}
