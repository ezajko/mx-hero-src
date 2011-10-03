package org.mxhero.console.backend.service.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.AliasAccount;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.entity.GroupPk;
import org.mxhero.console.backend.service.CustomReportService;
import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.statistics.entity.Record;
import org.mxhero.console.backend.statistics.entity.RecordPk_;
import org.mxhero.console.backend.statistics.entity.Record_;
import org.mxhero.console.backend.translator.RecordTranslator;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("customReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaCustomReportService implements CustomReportService{

	private static final String INDIVIDUAL="individual";
	private static final String GROUP="group";
	private static final String DOMAIN="domain";	
	private static final String ALLDOMAINS="alldomains";
	private static final String ANYONEELSE="anyoneelse";
	
	@PersistenceContext(unitName = "statisticsPer")
	private EntityManager entityManager;
	
	private GroupDao groupDao;
	
	private RecordTranslator recordTranslator;
	
	@Autowired
	public JpaCustomReportService(GroupDao groupDao, RecordTranslator recordTranslator) {
		super();
		this.groupDao = groupDao;
		this.recordTranslator = recordTranslator;
	}

	@Override
	public Collection getTopTenSenders(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),root.get(Record_.from));
		query = query.where(getFromToPredicatetoPredicate(from,to,since,until,root,query,builder));
		query = query.groupBy(root.get(Record_.from));
		query = query.orderBy(builder.desc((builder.count(root.get(Record_.id).get(RecordPk_.sequence)))));

		return entityManager.createQuery(query).setMaxResults(10).getResultList();
	}

	@Override
	public Collection getTopTenRecipients(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),root.get(Record_.recipient));
		query = query.where(getFromToPredicatetoPredicate(from,to,since,until,root,query,builder));
		query = query.groupBy(root.get(Record_.recipient));
		query = query.orderBy(builder.desc((builder.count(root.get(Record_.id).get(RecordPk_.sequence)))));

		return entityManager.createQuery(query).setMaxResults(10).getResultList();

	}

	@Override
	public Collection<RecordVO> getEmails(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, final long since, final long until) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Record> query = builder.createQuery(Record.class);
		Root<Record> root = query.from(Record.class);
		query = query.where(getFromToPredicatetoPredicate(from,to,since,until,root,query,builder));
		query = query.orderBy(builder.desc(root.get(Record_.id).get(RecordPk_.insertDate)));
		return recordTranslator.translate(entityManager.createQuery(query).setMaxResults(PluginReportService.MAX_RESULT).getResultList());	
	}
	
	private Predicate getFromToPredicatetoPredicate(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, 
			final long since, 
			final long until,
			Root<Record> root, 
			CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		
		Collection<EmailAccount> groupFrom = (from.getDirectionType().equals(GROUP))?groupDao.readByPrimaryKey(new GroupPk(from.getGroup(),from.getDomain())).getMembers():new ArrayList<EmailAccount>();
		Collection<EmailAccount> groupTo = (to.getDirectionType().equals(GROUP))?groupDao.readByPrimaryKey(new GroupPk(to.getGroup(),to.getDomain())).getMembers():new ArrayList<EmailAccount>();
		
		final Collection<String> groupFromEmails = new ArrayList<String>();
		final Collection<String> groupToEmails = new ArrayList<String>();
		
		for(EmailAccount email : groupFrom){
			for(AliasAccount aliasAccount : email.getAliases()){
				groupFromEmails.add(aliasAccount.getId().getAccountAlias()+"@"+aliasAccount.getId().getDomainAlias());
			}
		}
		for(EmailAccount email : groupTo){
			for(AliasAccount aliasAccount : email.getAliases()){
				groupToEmails.add(aliasAccount.getId().getAccountAlias()+"@"+aliasAccount.getId().getDomainAlias());
			}
		}

		//time interval
		Predicate predicate = builder.between(root.get(Record_.id).get(RecordPk_.insertDate), new Timestamp(since), new Timestamp(until));
		
		//from condition
		if(from.getDirectionType().equals(ANYONEELSE)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.senderDomainId), root.get(Record_.recipientDomainId)));
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.flow), "in"));
		}else if(from.getDirectionType().equals(DOMAIN)){
			predicate = builder.and(predicate,builder.equal(root.get(Record_.senderDomainId), from.getFreeValue()));
		}else if(from.getDirectionType().equals(GROUP)){
			if(groupFromEmails.size()>0){
				predicate = builder.and(predicate,root.get(Record_.senderId).in(groupFromEmails));
			}
		}else if(from.getDirectionType().equals(INDIVIDUAL)){	
			predicate = builder.and(predicate,builder.or(builder.equal(root.get(Record_.senderId),from.getFreeValue()),builder.equal(root.get(Record_.from),from.getFreeValue())));
		}
		
		//to condition
		if(to.getDirectionType().equals(ANYONEELSE)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.senderDomainId), root.get(Record_.recipientDomainId)));
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.flow), "out"));
		} else if(to.getDirectionType().equals(DOMAIN)){
			predicate = builder.and(predicate,builder.equal(root.get(Record_.recipientDomainId), to.getFreeValue()));
		}else if(to.getDirectionType().equals(GROUP)){
			if(groupToEmails.size()>0){
				predicate = builder.and(predicate,root.get(Record_.recipientId).in(groupToEmails));
			}
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			predicate = builder.and(predicate,builder.equal(root.get(Record_.recipientId),to.getFreeValue()));
		}
				
		return predicate;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}