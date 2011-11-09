package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;

import org.mxhero.console.backend.dao.DomainDao;
import org.mxhero.console.backend.dao.EmailAccountDao;
import org.mxhero.console.backend.dao.FeatureRuleDao;
import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.EmailAccountPk;
import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.entity.Group;
import org.mxhero.console.backend.entity.GroupPk;
import org.mxhero.console.backend.infrastructure.BusinessException;
import org.mxhero.console.backend.service.FeatureService;
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
	
	private FeatureRuleDao featureRuleDao;
	
	private FeatureService featureService;

	@Autowired
	public JpaGroupService(GroupDao groupDao, EmailAccountDao emailAccountDao,
			GroupTranslator groupTranslator,
			EmailAccountTranslator emailAccountTranslator,
			DomainDao domainDao,
			FeatureRuleDao featureRuleDao,
			FeatureService featureService) {
		super();
		this.groupDao = groupDao;
		this.emailAccountDao = emailAccountDao;
		this.groupTranslator = groupTranslator;
		this.emailAccountTranslator = emailAccountTranslator;
		this.domainDao = domainDao;
		this.featureRuleDao = featureRuleDao;
		this.featureService = featureService;
	}

	@Override
	public Collection<EmailAccountVO> findMembersByGroupId(String groupId, String domainId) {
		return emailAccountTranslator.translate(emailAccountDao.findAllByGroupId(groupId,domainId));
	}

	@Override
	public Collection<EmailAccountVO> findMembersByDomainIdWithoutGroup(
			String domainId) {
		return emailAccountTranslator.translate(emailAccountDao.findAllByDomainIdWithoutGroup(domainId));
	}

	@Override
	public Collection<GroupVO> findAll(String domainId) {
		return groupTranslator.translate(groupDao.findByDomainId(domainId));
	}

	@Override
	public void remove(String groupName, String domainId) {
		Group group = groupDao.readByPrimaryKey(new GroupPk(groupName,domainId));
		if(group!=null){
			for(EmailAccount emailAccount : group.getMembers()){
				emailAccount.setGroup(null);
				emailAccountDao.save(emailAccount);
			}
			
			for(FeatureRule rule : featureRuleDao.findByDirectionTypeAndDomainIdAndGroupId("group",group.getId().getDomainId(),group.getId().getName())){
				featureService.remove(rule.getId());
			}
			
			group = groupDao.readByPrimaryKey(new GroupPk(groupName,domainId));
			groupDao.delete(group);
		}
		
	}

	@Override
	public void insert(GroupVO groupVO, Collection<EmailAccountVO> members) {
		GroupPk pk = new GroupPk();
		pk.setDomainId(groupVO.getDomain());
		pk.setName(groupVO.getName());
		Group newGroup = new Group();
		newGroup.setId(pk);
		newGroup.setDescription(groupVO.getDescription());
		newGroup.setUpdatedDate(Calendar.getInstance());
		newGroup.setCreatedDate(Calendar.getInstance());
		newGroup.setDomain(domainDao.readByPrimaryKey(groupVO.getDomain()));	
		try{
			groupDao.save(newGroup);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
		if(members!=null && members.size()>0){
			for(EmailAccountVO emailAccountVO : members){
				try{
					EmailAccountPk emailPk = new EmailAccountPk();
					emailPk.setAccount(emailAccountVO.getAccount());
					emailPk.setDomainId(groupVO.getDomain());
					EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailPk);
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
		Group group = groupDao.readByPrimaryKey(new GroupPk(groupVO.getName(),groupVO.getDomain()));
		Collection<EmailAccount> previousMembers= group.getMembers();
		group.setDescription(groupVO.getDescription());
		group.setUpdatedDate(Calendar.getInstance());
		group.setMembers(null);
		try{
			groupDao.save(group);
		} catch (DataIntegrityViolationException e){
			throw new BusinessException(GROUP_ALREADY_EXISTS);
		}
		if(previousMembers!=null){
			for(EmailAccount previousAccount : previousMembers){
				previousAccount.setGroup(null);
				emailAccountDao.save(previousAccount);
			}
		}
		
		if(members!=null && members.size()>0){
			for(EmailAccountVO emailAccountVO : members){
				try{
					EmailAccountPk emailPk = new EmailAccountPk();
					emailPk.setAccount(emailAccountVO.getAccount());
					emailPk.setDomainId(group.getId().getDomainId());
					EmailAccount emailAccount =emailAccountDao.readByPrimaryKey(emailPk);
					if(emailAccount!=null){
						emailAccount.setGroup(group);
						emailAccountDao.save(emailAccount);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
