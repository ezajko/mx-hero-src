package org.mxhero.engine.plugin.statistics.internal.entity;

import java.sql.Timestamp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

public class RecordTest {

	private static Logger log = LoggerFactory.getLogger(RecordTest.class);
	
	@Test
	public void testHashEquals(){
		long time = System.currentTimeMillis();

		Record record1 = new Record();
		RecordPk pk1 = new RecordPk();
		pk1.setInsertDate(new Timestamp(time));
		pk1.setSequence(0l);
		record1.setId(pk1);
		log.info(pk1.toString());
		log.info(record1.toString());
		
		Record record2 = new Record();
		RecordPk pk2 = new RecordPk();
		pk2.setInsertDate(new Timestamp(time));
		pk2.setSequence(1l);
		record2.setId(pk2);
		log.info(pk2.toString());
		log.info(record2.toString());
		Assert.assertFalse(pk1.equals(null));
		Assert.assertFalse(pk1.equals(""));
		Assert.assertTrue(pk1.hashCode()==pk2.hashCode());
		Assert.assertFalse(pk1.equals(pk2));
		Assert.assertFalse(record1.equals(null));
		Assert.assertFalse(record1.equals(""));
		Assert.assertTrue(record1.hashCode()==record2.hashCode());
		Assert.assertFalse(record1.equals(record2));
		
		pk2.setSequence(pk1.getSequence());
		log.info(pk1.toString());
		log.info(pk2.toString());
		
		Assert.assertTrue(record1.equals(record2));
		Assert.assertTrue(pk1.equals(pk2));
		
	}
	
}
