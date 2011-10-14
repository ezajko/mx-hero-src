package org.mxhero.engine.plugin.adsync.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class DomainsSynchronizerTest {

	@Autowired
	private DomainsSynchronizer service;

	@Test
	public void processDomain(){
		service.synchronize("conbras.com");
	}
	
	public DomainsSynchronizer getService() {
		return service;
	}

	public void setService(DomainsSynchronizer service) {
		this.service = service;
	}
	
}
