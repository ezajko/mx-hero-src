package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.attachmentlink.alcommand.internal.cleaner.DaysPeriodCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:cleaner-bundle-context.xml")
public class CleanerTest {

	@Autowired
	private DaysPeriodCleaner cleaner;

	public DaysPeriodCleaner getCleaner() {
		return cleaner;
	}

	public void setCleaner(DaysPeriodCleaner cleaner) {
		this.cleaner = cleaner;
	}
	
	@Test
	public void test() throws InterruptedException{
		Thread.sleep(1000);
		cleaner.stop();
		Thread.sleep(1000);
	}
}
