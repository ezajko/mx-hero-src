package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.Authority;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorityTranslator extends AbstractTranslator<AuthorityVO, Authority>{

	@Override
	protected AuthorityVO doTranslate(Authority entity) {
		AuthorityVO authorityVO = new AuthorityVO();
		
		authorityVO.setAuthority(entity.getAuthority());
		authorityVO.setId(entity.getId());
		
		return authorityVO;
	}

}
