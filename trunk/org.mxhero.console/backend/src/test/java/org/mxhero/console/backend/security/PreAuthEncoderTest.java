package org.mxhero.console.backend.security;

import org.junit.Assert;
import org.junit.Test;

public class PreAuthEncoderTest {

	@Test
	public void test(){
		String loginName = "admin";
		String domain ="null";
		String timestamp = ""+System.currentTimeMillis();
		String expires = "0";
		System.out.println("loginname="+loginName+"&domain="+domain+"&timestamp="+timestamp+"&expires="+expires+"&preauth="+PreAuthEncoder.encode(loginName, domain, timestamp, expires, PreAuthFilter.PER_AUTH_DEFAULT));
		Assert.assertTrue(PreAuthEncoder.encode(loginName, domain, timestamp, expires, PreAuthFilter.PER_AUTH_DEFAULT).equals(PreAuthEncoder.encode(loginName, domain, timestamp, expires, PreAuthFilter.PER_AUTH_DEFAULT)));
	}
	
}
