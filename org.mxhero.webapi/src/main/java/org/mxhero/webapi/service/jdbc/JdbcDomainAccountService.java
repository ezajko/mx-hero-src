package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.repository.AccountRepository;
import org.mxhero.webapi.service.DomainAccountService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.AccountVO;
import org.mxhero.webapi.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcDomainAccountService implements DomainAccountService{

	private AccountRepository accountRepository;
	
	@Autowired
	public JdbcDomainAccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public PageVO<AccountVO> readAll(String domain, Integer limit,
			Integer offset) {
		PageVO<AccountVO> accountVOPage = new PageVO<AccountVO>();
		PageResult<AccountVO> result = accountRepository.findAll(domain, null, null, offset, limit);
		accountVOPage.setActualPage(result.getPageNumber());
		accountVOPage.setTotalElements(result.getTotalRecordsNumber());
		accountVOPage.setTotalPages(result.getPageAmount());
		accountVOPage.setElements(result.getPageData());
		return accountVOPage;
	}

	@Override
	public AccountVO create(AccountVO accountVO) {
		accountRepository.insert(accountVO);
		AccountVO insertedAccount = accountRepository.findById(accountVO.getAccount(), accountVO.getDomain());
		if(insertedAccount==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		return insertedAccount;
	}

	@Override
	public AccountVO read(String domain, String account) {
		AccountVO accountVO = accountRepository.findById(account, domain);
		if(accountVO==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		return accountVO;
	}

	@Override
	public void update(AccountVO accountVO) {
		AccountVO insertedAccount = accountRepository.findById(accountVO.getAccount(), accountVO.getDomain());
		if(insertedAccount==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		accountRepository.update(accountVO);
	}

	@Override
	public void delete(String domain, String account) {
		AccountVO insertedAccount = accountRepository.findById(account, domain);
		if(insertedAccount==null){
			throw new UnknownResourceException("domain.account.not.found");
		}
		accountRepository.delete(account, domain);
	}

}
