package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.ApplicationUser;
import org.mxhero.console.backend.entity.Domain;
import org.mxhero.console.backend.entity.DomainAlias;
import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.OwnerVO;
import org.springframework.stereotype.Repository;

@Repository
public class DomainTranslator extends AbstractTranslator<DomainVO, Domain>{

	private AbstractTranslator<OwnerVO, ApplicationUser> ownerTranslator = new OwnerTranslator();
	
	@Override
	protected DomainVO doTranslate(Domain entity) {
		DomainVO domainVO = new DomainVO();
		
		domainVO.setCreationDate(entity.getCreationDate());
		domainVO.setDomain(entity.getDomain());
		domainVO.setId(entity.getId());
		domainVO.setOwner(ownerTranslator.translate(entity.getOwner()));
		domainVO.setServer(entity.getServer());
		domainVO.setUpdatedDate(entity.getUpdatesDate());
		if(entity.getAliases()!=null && entity.getAliases().size()>0){
			domainVO.setAliases(new java.util.ArrayList<String>());
			for(DomainAlias alias : entity.getAliases()){
				domainVO.getAliases().add(alias.getAlias());
			}
		}
		return domainVO;
	}

}
