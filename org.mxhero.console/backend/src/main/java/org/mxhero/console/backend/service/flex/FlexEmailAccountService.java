package org.mxhero.console.backend.service.flex;

import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class FlexEmailAccountService implements EmailAccountService{

	private EmailAccountService service;

	@Autowired(required=true)
	public FlexEmailAccountService(@Qualifier("jdbcEmailAccountService")EmailAccountService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public PageVO findPageBySpecs(String domainId, String email, String group,
			int pageNo, int pageSize) {
		return service.findPageBySpecs(domainId, email, group, pageNo, pageSize);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void remove(String account, String domain) {
		service.remove(account, domain);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void insert(EmailAccountVO emailAccountVO, String domainId) {
		this.service.insert(emailAccountVO, domainId);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void edit(EmailAccountVO emailAccountVO) {
		this.service.edit(emailAccountVO);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void insertAccountAlias(String accountAlias, String domainAlias,
			String account, String domain) {
		this.service.insertAccountAlias(accountAlias, domainAlias, account, domain);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public void removeAccountAlias(String accountAlias, String domainAlias) {
		service.removeAccountAlias(accountAlias, domainAlias);
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<EmailAccountVO> upload(
			Collection<EmailAccountVO> emailAccountVOs, String domainId,
			Boolean failOnError) {
		return service.upload(emailAccountVOs, domainId, failOnError);
	}

	@Override
	public void updateProperties(String domain, String account,
			List<AccountPropertyVO> properties) {
		service.updateProperties(domain, account, properties);
	}

	@Override
	public List<AccountPropertyVO> readProperties(String domain, String account) {
		return service.readProperties(domain, account);
	}

}
