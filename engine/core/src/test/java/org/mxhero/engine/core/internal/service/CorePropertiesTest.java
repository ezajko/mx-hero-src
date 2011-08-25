package org.mxhero.engine.core.internal.service;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

public class CorePropertiesTest {

	@Test
	public void testService(){
		Core properties= new Core();
		Assert.assertNotNull(properties.getValue(Core.SENDPOOL_COREPOOLSIZE));
	}
	
	
	@Test
	public void testThis(){
		Collection<String> types = new ArrayList<String>();
		types.add("text/plain");
		types.add("image/gif");
		types.add("text/html");
		Collection<String> patterns = new ArrayList<String>();
		patterns.add("image");
		for(String name :types){
			for(String pattern : patterns){
				if(name.startsWith(pattern)){
					System.out.println(true);
				}
			}
		}
			System.out.println(false);
		}
	
}
