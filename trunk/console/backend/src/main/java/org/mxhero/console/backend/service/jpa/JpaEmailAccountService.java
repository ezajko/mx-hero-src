package org.mxhero.console.backend.service.jpa;

import java.util.Collection;

import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.PageRequest;
import org.synyx.hades.domain.Specifications;

import static org.synyx.hades.domain.Specifications.where;
import static org.mxhero.console.backend.dao.specs.EmailAccountSpecs.*;


@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class JpaEmailAccountService implements EmailAccountService{

	private EmailAccountDao emailAccountDao;
	
	private EmailAccountTranslator emailAccountTranslator;
	
	@Autowired
	public JpaEmailAccountService(EmailAccountDao emailAccountDao, EmailAccountTranslator emailAccountTranslator){
		this.emailAccountDao=emailAccountDao;
		this.emailAccountTranslator=emailAccountTranslator;
	}
	
	@Override
	public Collection<EmailAccountVO> findAll(Integer domainId) {
		try{	
			PageRequest pr = new PageRequest(0, 2);

			Page<EmailAccount> page = emailAccountDao.finbAllByDomainId(domainId,pr);
			return emailAccountTranslator.translate(page.asList());
		} catch (IncorrectResultSizeDataAccessException e){
			e.printStackTrace();
		}
		return null;
	}


	public PageVO<EmailAccountVO> findPageBySpecs(Integer domainId, String email, String name, String lastName, Integer page, Integer pageSize) {
		try{	
			Specifications<EmailAccount> specifications = where(domainIdEqual(domainId));
			if(email!=null && !email.trim().isEmpty()){
				specifications.and(emailLike(email));
			}
			if(name!=null && !name.trim().isEmpty()){
				specifications.and(nameLike(name));
			}
			if(lastName!=null && !lastName.trim().isEmpty()){
				specifications.and(lastNameLike(lastName));
			}

			PageRequest pr = new PageRequest(page, pageSize);
			Page<EmailAccount> pageData = emailAccountDao.readAll(specifications, pr);
			return emailAccountTranslator.translate(pageData);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
}
