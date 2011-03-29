package org.mxhero.console.backend.service.jpa;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.mxhero.console.backend.dao.GroupDao;
import org.mxhero.console.backend.entity.EmailAccount;
import org.mxhero.console.backend.service.CustomReportService;
import org.mxhero.console.backend.statistics.dao.RecordDao;
import org.mxhero.console.backend.statistics.entity.Record;
import org.mxhero.console.backend.statistics.entity.RecordPk_;
import org.mxhero.console.backend.statistics.entity.Record_;
import org.mxhero.console.backend.translator.RecordTranslator;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.synyx.hades.domain.Specification;

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
	
	private RecordDao recordDao;
	
	private GroupDao groupDao;
	
	private RecordTranslator recordTranslator;
	
	@Autowired
	public JpaCustomReportService(RecordDao recordDao, GroupDao groupDao, RecordTranslator recordTranslator) {
		super();
		this.recordDao = recordDao;
		this.groupDao = groupDao;
		this.recordTranslator = recordTranslator;
	}

	@Override
	public Collection getTopTenSenders(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, Calendar since, Calendar until) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),root.get(Record_.senderId));
		query = query.where(getFromToPredicatetoPredicate(from,to,since,until,root,query,builder));
		query = query.groupBy(root.get(Record_.senderId));
		query = query.orderBy(builder.desc((builder.count(root.get(Record_.id).get(RecordPk_.sequence)))));

		return entityManager.createQuery(query).setMaxResults(10).getResultList();
	}

	@Override
	public Collection getTopTenRecipients(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, Calendar since, Calendar until) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),root.get(Record_.recipientId));
		query = query.where(getFromToPredicatetoPredicate(from,to,since,until,root,query,builder));
		query = query.groupBy(root.get(Record_.recipientId));
		query = query.orderBy(builder.desc((builder.count(root.get(Record_.id).get(RecordPk_.sequence)))));

		return entityManager.createQuery(query).setMaxResults(10).getResultList();

	}

	@Override
	public Collection<RecordVO> getEmails(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, final Calendar since, final Calendar until) {
		
		//PageRequest pr = new PageRequest(0, 10);
		
		Specification<Record> specification = new Specification<Record>() {
			
			@Override
			public Predicate toPredicate(Root<Record> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				//time interval
				return getFromToPredicatetoPredicate(from,to,since,until,root,query,builder);
			}
		};
		
		return recordTranslator.translate(recordDao.readAll(specification ));
	}
	
	private Predicate getFromToPredicatetoPredicate(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, 
			final Calendar since, 
			final Calendar until,
			Root<Record> root, 
			CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		
		Collection<EmailAccount> groupFrom = (from.getDirectionType().equals(GROUP))?groupDao.readByPrimaryKey(from.getValueId()).getMembers():new ArrayList<EmailAccount>();
		Collection<EmailAccount> groupTo = (from.getDirectionType().equals(GROUP))?groupDao.readByPrimaryKey(from.getValueId()).getMembers():new ArrayList<EmailAccount>();
		
		final Collection<String> groupFromEmails = new ArrayList<String>();
		final Collection<String> groupToEmails = new ArrayList<String>();
		
		for(EmailAccount email : groupFrom){
			groupFromEmails.add(email.getAccount()+"@"+email.getDomain());
		}
		for(EmailAccount email : groupTo){
			groupToEmails.add(email.getAccount()+"@"+email.getDomain());
		}
		
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		
		until.set(Calendar.HOUR, 0);
		until.set(Calendar.MINUTE, 0);
		until.set(Calendar.SECOND, 0);
		until.set(Calendar.MILLISECOND, 0);
		until.add(Calendar.DATE, 1);
		
		//time interval
		Predicate predicate = builder.between(root.get(Record_.id).get(RecordPk_.insertDate), new Timestamp(since.getTimeInMillis()), new Timestamp(until.getTimeInMillis()));
		
		//from condition
		if(from.getDirectionType().equals(ANYONEELSE)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.senderDomainId), root.get(Record_.recipientDomainId)));
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.flow), "in"));
		}else if(from.getDirectionType().equals(DOMAIN)){
			predicate = builder.and(predicate,builder.equal(root.get(Record_.senderDomainId), from.getFreeValue()));
		}else if(from.getDirectionType().equals(GROUP)){
			predicate = builder.and(predicate,root.get(Record_.senderId).in(groupFromEmails));
		}else if(from.getDirectionType().equals(INDIVIDUAL)){
			predicate = builder.and(predicate,root.get(Record_.senderId).in(from.getFreeValue()));
		}
		
		//to condition
		if(to.getDirectionType().equals(ANYONEELSE)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.senderDomainId), root.get(Record_.recipientDomainId)));
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			predicate = builder.and(predicate,builder.notEqual(root.get(Record_.flow), "out"));
		} else if(to.getDirectionType().equals(DOMAIN)){
			predicate = builder.and(predicate,builder.equal(root.get(Record_.recipientDomainId), to.getFreeValue()));
		}else if(to.getDirectionType().equals(GROUP)){
			predicate = builder.and(predicate,root.get(Record_.recipientId).in(groupToEmails));
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			predicate = builder.and(predicate,root.get(Record_.recipientId).in(to.getFreeValue()));
		}
		
		//phase and state
		predicate = builder.and(predicate,
				builder.or(
							builder.and(builder.equal(root.get(Record_.phase),"send"),builder.equal(root.get(Record_.state),"drop"))
							,builder.equal(root.get(Record_.phase),"receive")));
		
		return predicate;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
