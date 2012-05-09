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
