package org.mxhero.webapi.service;

import java.util.List;

import org.mxhero.webapi.vo.AccountPropertyVO;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.PageVO;

public interface DomainAccountService {

	public PageVO<AccountVO> readAll(String domain, Integer limit, Integer offset);
	
	public AccountVO create(AccountVO accountVO);
	
	public AccountVO read(String domain, String account);
	
	public void update(AccountVO accountVO);
	
	public void delete(String domain, String account);
	
	public void updateProperties(String domain, String account, List<AccountPropertyVO> properties);
	
	public List<AccountPropertyVO> readProperties(String domain, String account);
	
	public void deleteProperties(String domain, String account);
}
