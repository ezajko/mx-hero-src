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

package org.mxhero.engine.plugin.adsync.internal.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mxhero.engine.plugin.adsync.internal.source.ADSource.Account;

public class ADSourceTest {

	@Test
	public void connectFail() throws Exception{
		ADSource source = new ADSource("zimbra.inova.net", "389", "uid=zimbra,cn=admins,cn=zimbra", "NsMxOU3uvM", false, "ou=people,dc=inova,dc=net");
		List<Account> accounts = null;
		try{
			Map<String,String> properties = new HashMap<String, String>();
			properties.put("fullname", "cn");
			properties.put("lastname", "sn");
			properties.put("email", "mail");
			accounts = source.getAllPersonNames("(&(|(objectclass=zimbraAccount)(objectclass=zimbraDistributionList))(mail=*))",properties);
		}catch(Exception e){
			return;
		}
		//Assert.fail();
	}
	
	
}
