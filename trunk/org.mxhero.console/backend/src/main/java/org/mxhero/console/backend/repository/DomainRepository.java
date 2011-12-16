package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.vo.DomainVO;

public interface DomainRepository {

	DomainVO update(DomainVO domainVO);
	
	void insert(DomainVO domainVO);

	void delete(String domainIdd);
	
	PageResult<DomainVO> findAll(String domainNamepageSize, int pageNo, int pageSize);
	
	DomainVO findById(String domainId);
	
	DomainVO findByUserId(Integer userId);
	
	List<String> findAliases(String domainId);
	
}
