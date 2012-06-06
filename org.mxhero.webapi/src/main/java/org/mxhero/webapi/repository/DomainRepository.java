package org.mxhero.webapi.repository;

import java.util.List;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.vo.DomainVO;


public interface DomainRepository {

	DomainVO update(DomainVO domainVO);
	
	void insert(DomainVO domainVO);

	void delete(String domainIdd);
	
	PageResult<DomainVO> findAll(Integer pageNo, Integer pageSize);
	
	DomainVO findById(String domainId);
	
	DomainVO findByUserId(Integer userId);
	
	List<String> findAliases(String domainId);
	
}
