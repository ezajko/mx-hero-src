package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmailAccountService {

	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> findAll(Integer domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO<EmailAccountVO> findPageBySpecs(Integer domainId, String email, String name, String lastName, Integer page, Integer pageSize);
		
}
