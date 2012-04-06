package org.mxhero.engine.plugin.dbfinder.internal.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.commons.domain.User;
import org.mxhero.engine.commons.finders.UserFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class TestMappedUserFinder {

	@Autowired
	private UserFinder finder;

	@Test
	public void testFind(){
		User user = finder.getUser("XXXXX.XXXX.XXXXX@XXXXX.XXX");
		Assert.assertNotNull(user);
		Assert.assertFalse(user.getManaged());
	}
	
	public UserFinder getFinder() {
		return finder;
	}

	public void setFinder(UserFinder finder) {
		this.finder = finder;
	}

}
