package org.mxhero.console.backend.translator;

import java.util.ArrayList;

import org.mxhero.console.backend.entity.AliasAccount;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.stereotype.Repository;

@Repository
public class EmailAccountTranslator extends AbstractTranslator<EmailAccountVO, EmailAccount> {

	@Override
	protected EmailAccountVO doTranslate(EmailAccount entity) {
		EmailAccountVO emailAccountVO = new EmailAccountVO();
		
		emailAccountVO.setCreatedDate(entity.getCreatedDate());
		emailAccountVO.setAccount(entity.getId().getAccount());
		emailAccountVO.setUpdatedDate(entity.getUpdatedDate());
		emailAccountVO.setDomain(entity.getDomain().getDomain());
		emailAccountVO.setDataSource(entity.getDataSource());
		if(entity.getGroup()!=null){
			emailAccountVO.setGroup(entity.getGroup().getId().getName());
		}
		emailAccountVO.setAliases(new ArrayList<EmailAccountAliasVO>());
		for(AliasAccount alias : entity.getAliases()){
			emailAccountVO.getAliases().add(new EmailAccountAliasVO(alias.getId().getAccountAlias(), (alias.getId().getDomainAlias()!=null)?alias.getId().getDomainAlias():alias.getAccount().getDomain().getDomain(),entity.getDataSource(),emailAccountVO));
		}
		return emailAccountVO;
	}

}
