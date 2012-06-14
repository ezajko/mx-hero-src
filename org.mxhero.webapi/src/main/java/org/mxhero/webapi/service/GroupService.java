package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.GroupVO;
import org.mxhero.webapi.vo.PageVO;

public interface GroupService {

	public PageVO<GroupVO> readAll(String domain, Integer limit, Integer offset);

	public GroupVO create(GroupVO groupVO);
	
	public GroupVO read(String domain, String name);

	public void update(GroupVO groupVO);
	
	public void delete(String domain, String name);
	
	public PageVO<AccountVO> readAllNoGroupAccounts(String domain, Integer limit, Integer offset);

	public PageVO<AccountVO> readAllGroupAccounts(String domain, String name, Integer limit, Integer offset);

	public void addAccount(String domain, String name, String account);

	public void removeAccount(String domain, String name, String account);
}
