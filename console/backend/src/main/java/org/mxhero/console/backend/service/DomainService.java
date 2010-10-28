package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.DomainVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DomainService {

	@Secured("ROLE_ADMIN")
	Collection<DomainVO> findAll();
	
	@Secured("ROLE_ADMIN")
	void remove(Integer id);
	
	@Secured("ROLE_ADMIN")
	void insert(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
	@Secured("ROLE_ADMIN")
	void edit(DomainVO domainVO,Boolean hasAdmin, String password, String email);
	
}
