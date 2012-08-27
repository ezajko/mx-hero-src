package org.mxhero.engine.plugin.boxstorage.internal.sinchro;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:box_context_client_test.xml"})
public class SynchronizerTest {
	
	@Autowired
	Synchronizer target;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSynchronize() {
		try {
			target.start();
			Thread.sleep(1000);
			target.stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
