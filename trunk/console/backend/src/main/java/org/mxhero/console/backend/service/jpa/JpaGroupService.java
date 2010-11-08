package org.mxhero.console.backend.service.jpa;

import java.util.Collection;

import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.service.GroupService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.translator.GroupTranslator;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("groupService")
@RemotingDestination(channels={"flex-amf"})
public class JpaGroupService implements GroupService{

	private GroupDao groupDao;
	
	private EmailAccountDao emailAccountDao;
	
	private GroupTranslator groupTranslator;
	
	private EmailAccountTranslator emailAccountTranslator;

	@Autowired
	public JpaGroupService(GroupDao groupDao, EmailAccountDao emailAccountDao,
			GroupTranslator groupTranslator,
			EmailAccountTranslator emailAccountTranslator) {
		super();
		this.groupDao = groupDao;
		this.emailAccountDao = emailAccountDao;
		this.groupTranslator = groupTranslator;
		this.emailAccountTranslator = emailAccountTranslator;
	}

	@Override
	public Collection<EmailAccountVO> findMembersByGroupId(Integer groupId) {
		return emailAccountTranslator.translate(emailAccountDao.findAllByGroupId(groupId));
	}

	@Override
	public Collection<EmailAccountVO> findMembersBydomainIdWithoutGroup(
			Integer domainId) {
		return emailAccountTranslator.translate(emailAccountDao.findAllByDomainIdWithoutGroup(domainId));
	}

	@Override
	public Collection<GroupVO> findAll(Integer domainId) {
		return groupTranslator.translate(groupDao.findByDomainId(domainId));
	}

	@Override
	public void remove(Integer groupId) {
		groupDao.delete(groupDao.readByPrimaryKey(groupId));
	}

}
