package org.mxhero.engine.plugin.adsync.source;

import junit.framework.Assert;

import org.junit.Test;

public class ADSourceTest {

	@Test
	public void connectFail() throws Exception{
		ADSource source = new ADSource("localhost", "389", "uid=example,cn=admins,cn=example", "password", false, "dc=example,dc=com");
		try{
			source.getAllPersonNames(null);
		}catch(Exception e){
			return;
		}
		Assert.fail();
	}
	
	
}
