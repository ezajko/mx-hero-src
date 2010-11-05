package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.infrastructure.PipedSpecifications;
import org.mxhero.console.backend.service.EmailAccountService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.PageRequest;

import static org.mxhero.console.backend.infrastructure.PipedSpecifications.*;
import static org.mxhero.console.backend.dao.specs.EmailAccountSpecs.*;


@Service("emailAccountService")
@RemotingDestination(channels={"flex-amf"})
public class JpaEmailAccountService implements EmailAccountService{

	public static final String EMAIL_ALREADY_EXISTS="email.already.exists";
	
	private EmailAccountDao emailAccountDao;
	
	private DomainDao domainDao;
	
	private EmailAccountTranslator emailAccountTranslator;
	
	@Autowired
	public JpaEmailAccountService(EmailAccountDao emailAccountDao, EmailAccountTranslator emailAccountTranslator, DomainDao domainDao){
		this.emailAccountDao=emailAccountDao;
		this.emailAccountTranslator=emailAccountTranslator;
		this.domainDao=domainDao;
	}

	public PageVO<EmailAccountVO> findPageBySpecs(Integer domainId, String email, String name, String lastName, Integer page, Integer pageSize) {

		PipedSpecifications<EmailAccount> specifications = where(domainIdEqual(domainId));
		if(email!=null && !email.trim().isEmpty()){
			specifications.and(accountLike(email));
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

	}

	@Override
	public void remove(Integer emailId) {
		EmailAccount emailAccount= emailAccountDao.readByPrimaryKey(emailId);
		emailAccountDao.delete(emailAccount);
	}

	@Override
	public void edit(EmailAccountVO emailAccountVO) {
		EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailAccountVO.getId());
		emailAccount.setName(emailAccountVO.getName());
		emailAccount.setLastName(emailAccountVO.getLastName());
		emailAccount.setUpdatedDate(Calendar.getInstance());
		emailAccountDao.save(emailAccount);
	}

	@Override
	public void insert(EmailAccountVO emailAccountVO, Integer domainId) {

		EmailAccount newEmailAccount = new EmailAccount();
		newEmailAccount.setUpdatedDate(Calendar.getInstance());
		newEmailAccount.setCreatedDate(Calendar.getInstance());
		newEmailAccount.setAccount(emailAccountVO.getAccount());
		newEmailAccount.setLastName(emailAccountVO.getLastName());
		newEmailAccount.setName(emailAccountVO.getName());
		newEmailAccount.setDomain(domainDao.readByPrimaryKey(domainId));
		try{
			emailAccountDao.save(newEmailAccount);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(EMAIL_ALREADY_EXISTS);
		}
	}

	@Override
	public void upload(Collection<EmailAccountVO> emailAccountVOs,
			Boolean failOnError) {
	
		System.out.println("HELLO");
	}
	
	
}
