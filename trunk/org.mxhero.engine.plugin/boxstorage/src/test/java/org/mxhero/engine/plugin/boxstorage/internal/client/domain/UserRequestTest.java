package org.mxhero.engine.plugin.boxstorage.internal.client.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mxhero.engine.plugin.boxstorage.internal.client.domain.UserRequest;

public class UserRequestTest {
	
	UserRequest target;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUserRequest_success() {
		String config = "true";
		target = new UserRequest("email@email.com",true);
		assertNotNull(target);
		assertTrue(target.isSender());
	}

}
