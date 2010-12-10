package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.stereotype.Repository;

@Repository
public class EmailAccountTranslator extends AbstractTranslator<EmailAccountVO, EmailAccount> {

	@Override
	protected EmailAccountVO doTranslate(EmailAccount entity) {
		EmailAccountVO emailAccountVO = new EmailAccountVO();
		
		emailAccountVO.setCreatedDate(entity.getCreatedDate());
		emailAccountVO.setAccount(entity.getAccount());
		emailAccountVO.setId(entity.getId());
		emailAccountVO.setLastName(entity.getLastName());
		emailAccountVO.setName(entity.getName());
		emailAccountVO.setUpdatedDate(entity.getUpdatedDate());
		emailAccountVO.setDomain(entity.getDomain().getDomain());
		if(entity.getGroup()!=null){
			emailAccountVO.setGroupId(entity.getGroup().getId());
		}
		
		return emailAccountVO;
	}

}
