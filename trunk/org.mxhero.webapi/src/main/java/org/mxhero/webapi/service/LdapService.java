package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.LdapVO;

public interface LdapService {

	public LdapVO create(LdapVO ldapVO);

	public LdapVO read(String domain);

	public void update(LdapVO ldapVO);

	public void delete(String domain);
	
}
