package org.mxhero.engine.plugin.attachmentlink.alcommand.service;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TransactionAttachmentTest {
	
	TransactionAttachment target;

	@Before
	public void setUp() throws Exception {
		target = new TransactionAttachment();
	}

	@Test
	public void testGetFolderName() {
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.YEAR, 2012);
		instance.set(Calendar.MONTH, 0);
		instance.set(Calendar.DATE, 12);
		target.setEmailDate(new Timestamp(instance.getTimeInMillis()));
		target.setIdMessageAttach(123l);
		String folderName = target.getFolderName();
		assertNotNull(folderName);
		assertEquals("2012_1_12_123", folderName);
	}

}
