package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.vo.OwnerVO;
import org.springframework.stereotype.Repository;

@Repository
public class OwnerTranslator extends AbstractTranslator<OwnerVO, ApplicationUser>{

	@Override
	protected OwnerVO doTranslate(ApplicationUser entity) {
		OwnerVO ownerVO = new OwnerVO();
		
		ownerVO.setEmail(entity.getNotifyEmail());
		ownerVO.setId(entity.getId());
		
		return ownerVO;
	}

}
