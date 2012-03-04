package org.mxhero.console.backend.security;

import org.junit.Test;

public class PreauthTest {

	@Test
	public void test(){
		String loginname="4test.net";
		String domain="4test.net";
		String timestamp=Long.toString(System.currentTimeMillis());
		String expires="0";
		
		System.out.println("http://localhost:8080/mxhero/preauth?loginname="+loginname+"&domain="+domain+"&timestamp="+timestamp+"&expires="+expires+"&preauth=+"+PreAuthEncoder.encode(loginname, domain, timestamp, expires, "4e2816f16c44fab20ecdee39fb850c3b0bb54d03f1d8e073aaea376a4f407f0c"));
		
	}
	
}
