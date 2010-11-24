package org.mxhero.engine.plugin.dbfinder.internal.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bundle-context.xml"})
public class JpaDomainFinderTest {

	@Autowired
	private DomainFinder finder;
	
	@Test
	public void testFind() throws InterruptedException{
		Domain domain = finder.getDomain("mxhero.com");
		System.out.println(domain.toString());
	}

	/**
	 * @return the finder
	 */
	public DomainFinder getFinder() {
		return finder;
	}

	/**
	 * @param finder the finder to set
	 */
	public void setFinder(DomainFinder finder) {
		this.finder = finder;
	}
	
}
