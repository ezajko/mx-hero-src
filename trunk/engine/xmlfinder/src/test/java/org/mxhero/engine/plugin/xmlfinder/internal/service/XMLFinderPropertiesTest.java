package org.mxhero.engine.plugin.xmlfinder.internal.service;

import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.xmlfinder.internal.service.XMLFinder;

public class XMLFinderPropertiesTest {

	@Test
	public void testCreationAndProperty(){
		PropertiesService service = new XMLFinder();
		Assert.assertNotNull(service.getValue("updatable.loader.check.time"));
	}
	
}
