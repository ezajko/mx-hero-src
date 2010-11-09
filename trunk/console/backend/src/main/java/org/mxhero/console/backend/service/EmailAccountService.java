package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmailAccountService {

	@Secured("ROLE_DOMAIN_ADMIN")
	PageVO<EmailAccountVO> findPageBySpecs(Integer domainId, String email, String name, String lastName, Integer groupId, Integer page, Integer pageSize);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(Integer emailId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insert(EmailAccountVO emailAccountVO,Integer domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void edit(EmailAccountVO emailAccountVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, Integer domainId, Boolean failOnError);
}
