package org.mxhero.engine.plugin.dbfinder.internal.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class JpaUserFinderTest {

	@Autowired
	private UserFinder finder;
	
	@Test
	public void testFind() throws InterruptedException{
		User user = finder.getUser("mmarmol@mxhero.com","mxhero.com");
		System.out.println(user.toString());
	}
	
}
