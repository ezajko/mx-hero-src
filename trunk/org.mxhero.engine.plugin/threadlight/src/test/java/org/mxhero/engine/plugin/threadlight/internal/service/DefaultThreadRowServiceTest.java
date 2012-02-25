package org.mxhero.engine.plugin.threadlight.internal.service;

import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.plugin.threadlight.internal.repository.cached.CachedJdbcThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.mxhero.engine.plugin.threadlight.service.ThreadRowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class DefaultThreadRowServiceTest {

	@Autowired
	private ThreadRowService service;
	@Autowired
	private CachedJdbcThreadRowRepository cachedRepository;
	
	
	@Test
	public void test(){
		ThreadRow threadRow = new ThreadRow();
		threadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
		threadRow.setSubject("subject exampe");
		threadRow.setPk(new ThreadRowPk());
		threadRow.getPk().setMessageId("MESSAGE-ID-"+System.currentTimeMillis());
		threadRow.getPk().setRecipientMail("recipient@example.com");
		threadRow.getPk().setSenderMail("sender@example.com");
		service.follow(threadRow, new ThreadRowFollower(null,"test-follower"));
		
		service.reply(threadRow.getPk());
		
		cachedRepository.persist(false);
	}

	
	public ThreadRowService getService() {
		return service;
	}

	public void setService(ThreadRowService service) {
		this.service = service;
	}

	public CachedJdbcThreadRowRepository getCachedRepository() {
		return cachedRepository;
	}

	public void setCachedRepository(CachedJdbcThreadRowRepository cachedRepository) {
		this.cachedRepository = cachedRepository;
	}
	
}
