package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.security.access.annotation.Secured;

public interface EmailAccountService {

	@Secured("ROLE_DOMAIN_ADMIN")
	PageVO findPageBySpecs(String domainId, String email,  String group, int pageNo, int pageSize);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void remove(String account, String domain);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insert(EmailAccountVO emailAccountVO,String domainId);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void edit(EmailAccountVO emailAccountVO);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void insertAccountAlias(String accountAlias, String domainAlias, String account, String domain);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	void removeAccountAlias(String accountAlias, String domainAlias);
	
	@Secured("ROLE_DOMAIN_ADMIN")
	Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, String domainId, Boolean failOnError);
}
