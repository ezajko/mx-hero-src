package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.DomainAdLdap;
import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.springframework.stereotype.Repository;

@Repository
public class DomainAdLdaoTranslator extends AbstractTranslator<DomainAdLdapVO, DomainAdLdap>{

	@Override
	protected DomainAdLdapVO doTranslate(DomainAdLdap entity) {
		DomainAdLdapVO adLdapVO = new DomainAdLdapVO();
		
		adLdapVO.setAddres(entity.getAddres());
		adLdapVO.setBase(entity.getBase());
		adLdapVO.setDirectoryType(entity.getDirectoryType());
		adLdapVO.setDomainId(entity.getDomainId());
		adLdapVO.setError(entity.getError());
		adLdapVO.setFilter(entity.getFilter());
		adLdapVO.setLastUpdate(entity.getLastUpdate());
		adLdapVO.setNextUpdate(entity.getNextUpdate());
		adLdapVO.setOverrideFlag(entity.getOverrideFlag());
		adLdapVO.setPassword(entity.getPassword());
		adLdapVO.setPort(entity.getPort());
		adLdapVO.setSslFlag(entity.getSslFlag());
		adLdapVO.setUser(entity.getUser());
		
		return adLdapVO;
	}

}
