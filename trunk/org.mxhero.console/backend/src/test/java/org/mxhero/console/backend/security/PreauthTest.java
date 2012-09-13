/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
