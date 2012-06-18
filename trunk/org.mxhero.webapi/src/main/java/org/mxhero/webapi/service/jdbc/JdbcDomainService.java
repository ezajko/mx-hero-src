package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.repository.AccountRepository;
import org.mxhero.webapi.repository.DomainRepository;
import org.mxhero.webapi.repository.RuleRepository;
import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jdbcDomainService")
public class JdbcDomainService implements DomainService{

	private DomainRepository domainRepository;
	private AccountRepository accountRepository;
	private RuleRepository ruleRepository;
	
	@Autowired(required=true)
	public JdbcDomainService(DomainRepository domainRepository, AccountRepository accountRepository, RuleRepository ruleRepository) {
		this.domainRepository = domainRepository;
		this.accountRepository = accountRepository;
		this.ruleRepository = ruleRepository;
	}

	@Override
	public PageVO<DomainVO> readAll(Integer limit, Integer offset) {
		PageVO<DomainVO> domainVOPage = new PageVO<DomainVO>();
		PageResult<DomainVO> result = domainRepository.findAll(offset, limit);
		domainVOPage.setActualPage(result.getPageNumber());
		domainVOPage.setTotalElements(result.getTotalRecordsNumber());
		domainVOPage.setTotalPages(result.getPageAmount());
		domainVOPage.setElements(result.getPageData());
		return domainVOPage;
	}

	@Override
	public DomainVO create(DomainVO domainVO) {
		domainRepository.insert(domainVO);
		DomainVO insertedDomain = domainRepository.findById(domainVO.getDomain());
		if(insertedDomain==null){
			throw new UnknownResourceException("domain.not.found");
		}
		return insertedDomain;
	}

	@Override
	public DomainVO read(String domain) {
		DomainVO domainVO = domainRepository.findById(domain);
		if(domainVO==null){
			throw new UnknownResourceException("domain.not.found");
		}
		return domainVO;
	}

	@Override
	public void update(DomainVO domainVO) {
		DomainVO insertedDomain = domainRepository.findById(domainVO.getDomain());
		if(insertedDomain==null){
			throw new UnknownResourceException("domain.not.found");
		}
		domainRepository.update(domainVO);
	}

	@Override
	public void delete(String domain) {
		DomainVO insertedDomain = domainRepository.findById(domain);
		if(insertedDomain==null){
			throw new UnknownResourceException("domain.not.found");
		}
		accountRepository.deleteByDomainId(domain);
		ruleRepository.deleteByDomain(domain);
		domainRepository.delete(domain);
	}

}
