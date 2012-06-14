package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.PageVO;

public interface DomainAccountService {

	public PageVO<AccountVO> readAll(String domain, Integer limit, Integer offset);
	
	public AccountVO create(AccountVO accountVO);
	
	public AccountVO read(String domain, String account);
	
	public void update(AccountVO accountVO);
	
	public void delete(String domain, String account);
	
}
