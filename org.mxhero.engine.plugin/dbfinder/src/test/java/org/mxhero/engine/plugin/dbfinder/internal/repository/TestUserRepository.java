package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class TestUserRepository {

	@Autowired
	private JdbcUserRepository repository;

	@Test
	public void test(){
		Map<String, Domain> domainMap = repository.getDomains();
		Assert.assertNotNull(domainMap);
		Map<String, User> usersMap = repository.getUsers();
		Assert.assertNotNull(usersMap);
	}
	
	public JdbcUserRepository getRepository() {
		return repository;
	}

	public void setRepository(JdbcUserRepository repository) {
		this.repository = repository;
	}
	

}
