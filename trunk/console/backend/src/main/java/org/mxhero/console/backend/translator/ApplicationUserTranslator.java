package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Authority;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.mxhero.console.backend.vo.DomainVO;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationUserTranslator extends AbstractTranslator<ApplicationUserVO, ApplicationUser> {

	private AbstractTranslator<DomainVO, Domain> domainTranslator = new DomainTranslator();
	private AbstractTranslator<AuthorityVO, Authority> authoritiesTranslator = new AuthorityTranslator();
	
	@Override
	protected ApplicationUserVO doTranslate(ApplicationUser entity) {
		ApplicationUserVO applicationUserVO = new ApplicationUserVO();
		
		applicationUserVO.setAuthorities(authoritiesTranslator.translate(entity.getAuthorities()));
		applicationUserVO.setCreationDate(entity.getCreationDate());
		applicationUserVO.setDomain(domainTranslator.translate(entity.getDomain()));
		applicationUserVO.setId(entity.getId());
		applicationUserVO.setLastName(entity.getLastName());
		applicationUserVO.setLocale(entity.getLocale());
		applicationUserVO.setName(entity.getName());
		applicationUserVO.setNotifyEmail(entity.getNotifyEmail());
		applicationUserVO.setUserName(entity.getUserName());

		return applicationUserVO;
	}

}
