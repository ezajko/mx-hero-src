package org.mxhero.engine.plugin.postfixconnector.internal.service;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;

public class PostfixConnectorPropertiesTest {

	@Test
	public void testService(){
		PropertiesService service = new PostfixConnector();
		Assert.assertNotNull(service.getValue(PostfixConnector.INPUTPOOL_COREPOOLSIZE));	
	}
	
}
