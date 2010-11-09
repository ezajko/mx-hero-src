package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.Group;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.GroupService;
import org.mxhero.console.backend.translator.EmailAccountTranslator;
import org.mxhero.console.backend.translator.GroupTranslator;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("groupService")
@RemotingDestination(channels={"flex-amf"})
public class JpaGroupService implements GroupService{

	public static final String GROUP_ALREADY_EXISTS="group.already.exists";
	
	private GroupDao groupDao;
	
	private DomainDao domainDao;
	
	private EmailAccountDao emailAccountDao;
	
	private GroupTranslator groupTranslator;
	
	private EmailAccountTranslator emailAccountTranslator;

	@Autowired
	public JpaGroupService(GroupDao groupDao, EmailAccountDao emailAccountDao,
			GroupTranslator groupTranslator,
			EmailAccountTranslator emailAccountTranslator,
			DomainDao domainDao) {
		super();
		this.groupDao = groupDao;
		this.emailAccountDao = emailAccountDao;
		this.groupTranslator = groupTranslator;
		this.emailAccountTranslator = emailAccountTranslator;
		this.domainDao = domainDao;
	}

	@Override
	public Collection<EmailAccountVO> findMembersByGroupId(Integer groupId) {
		return emailAccountTranslator.translate(emailAccountDao.findAllByGroupId(groupId));
	}

	@Override
	public Collection<EmailAccountVO> findMembersByDomainIdWithoutGroup(
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

	@Override
	public void insert(GroupVO groupVO, Integer domainId,Collection<EmailAccountVO> members) {
		Group newGroup = new Group();
		newGroup.setName(groupVO.getName());
		newGroup.setDescription(groupVO.getDescription());
		newGroup.setUpdatedDate(Calendar.getInstance());
		newGroup.setCreatedDate(Calendar.getInstance());
		newGroup.setDomain(domainDao.readByPrimaryKey(domainId));	
		try{
			groupDao.save(newGroup);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
		if(members!=null && members.size()>0){
			for(EmailAccountVO emailAccountVO : members){
				try{
					EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailAccountVO.getId());
					if(emailAccount!=null){
						emailAccount.setGroup(newGroup);
					}
					emailAccountDao.save(emailAccount);
				}catch (Exception e){
					
				}
			}
		}
	}

	@Override
	public void edit(GroupVO groupVO, Collection<EmailAccountVO> members) {
		Group group = groupDao.readByPrimaryKey(groupVO.getId());
		group.setDescription(groupVO.getDescription());
		group.setUpdatedDate(Calendar.getInstance());
		group.setMembers(null);
		try{
			groupDao.save(group);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
		if(members!=null && members.size()>0){
			for(EmailAccountVO emailAccountVO : members){
				try{
					EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailAccountVO.getId());
					if(emailAccount!=null){
						emailAccount.setGroup(group);
					}
					emailAccountDao.save(emailAccount);
				}catch (Exception e){
					
				}
			}
		}
	}

}
