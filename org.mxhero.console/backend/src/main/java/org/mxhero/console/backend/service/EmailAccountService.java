package org.mxhero.console.backend.service;

import java.util.Collection;

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;

public interface EmailAccountService {

	PageVO findPageBySpecs(String domainId, String email,  String group, int pageNo, int pageSize);
	
	void remove(String account, String domain);
	
	void insert(EmailAccountVO emailAccountVO,String domainId);
	
	void edit(EmailAccountVO emailAccountVO);
	
	void insertAccountAlias(String accountAlias, String domainAlias, String account, String domain);
	
	void removeAccountAlias(String accountAlias, String domainAlias);
	
	Collection<EmailAccountVO> upload(Collection<EmailAccountVO> emailAccountVOs, String domainId, Boolean failOnError);
}
