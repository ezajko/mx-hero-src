package org.mxhero.console.backend.repository;

import java.util.List;

import org.mxhero.console.backend.vo.DomainVO;

public interface DomainRepository {

	DomainVO update(DomainVO domainVO);
	
	void insert(DomainVO domainVO);

	void delete(String domainIdd);
	
	List<DomainVO> findAll();
	
	DomainVO findById(String domainId);
	
	DomainVO findByUserId(Integer userId);
	
	List<String> findAliases(String domainId);
	
}
