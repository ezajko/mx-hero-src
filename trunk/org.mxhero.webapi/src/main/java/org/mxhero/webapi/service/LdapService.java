package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.LdapVO;

public interface LdapService {

	public LdapVO create(String domain, LdapVO ldapVO);

	public LdapVO read(String domain);

	public void update(String domain, LdapVO ldapVO);

	public void delete(String domain);
	
}
