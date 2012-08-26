package org.mxhero.engine.plugin.boxserver;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mxhero.engine.plugin.boxserver.BoxCloudStorage;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.springframework.beans.factory.BeanFactory;

@RunWith(MockitoJUnitRunner.class)
public class BoxCloudStorageTest {
	
	private static final String PEPE_PEPE_COM = "pepe@pepe.com";

	BoxCloudStorage target;
	
	@Mock
	BeanFactory factory;
	
	@Mock
	UserBox user;

	@Before
	public void setUp() throws Exception {
		target = new BoxCloudStorage();
		target.setBeanFactory(factory);
		when(factory.getBean(anyString(), anyString())).thenReturn(user);
	}

	@Test
	public void testCreateAccount() {
		UserBox createAccount = target.createAccount(PEPE_PEPE_COM);
		assertNotNull(createAccount);
		verify(user, atLeastOnce()).createAccount();
	}

}
