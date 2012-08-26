package org.mxhero.engine.plugin.boxserver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.boxserver.BoxCloudStorage;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:box_context_service.xml")
public class IntegrationTest {
	
	@Autowired
	BoxCloudStorage target;

	@Before
	public void setUp() throws Exception {
	}

	@Ignore
	@Test
	public void test_create_account() {
		UserBox result = target.createAccount("mxHeroTest10@mxhero.com");
		assertNotNull(result);
		assertFalse(result.hasPreviousAccount());
	}

}
