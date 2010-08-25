package org.mxhero.engine.core.internal.service;

import junit.framework.Assert;

import org.junit.Test;

public class CorePropertiesTest {

	@Test
	public void testService(){
		Core properties= new Core();
		Assert.assertNotNull(properties.getValue(Core.INPUTPOOL_COREPOOLSIZE));
	}
	
}
