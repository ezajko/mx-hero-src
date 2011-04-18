package org.mxhero.console.backend.service.jpa;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.service.ThreatsReportService;
import org.mxhero.console.backend.statistics.dao.RecordDao;
import org.mxhero.console.backend.statistics.entity.Record;
import org.mxhero.console.backend.statistics.entity.RecordPk_;
import org.mxhero.console.backend.statistics.entity.Record_;
import org.mxhero.console.backend.statistics.entity.Stat;
import org.mxhero.console.backend.statistics.entity.StatPk_;
import org.mxhero.console.backend.statistics.entity.Stat_;
import org.mxhero.console.backend.translator.RecordTranslator;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("threatsReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaThreatsReportService implements ThreatsReportService {

	private static final String SPAM_DETECTED = "spam.detected";
	private static final String SPAM_DETECTED_VALUE = "true";
	private static final String VIRUS_DETECTED = "virus.detected";
	private static final String VIRUS_DETECTED_VALUE = "true";
	
	@PersistenceContext(unitName = "statisticsPer")
	private EntityManager entityManager;
	
	private RecordDao recordDao;
	
	private String mailQuerySql = " select record0_.* " 
		+" from statistics.mail_records record0_ "
		+" inner join statistics.mail_records record2_ on record2_.parent_message_id=record0_.parent_message_id "
		+" inner join statistics.mail_stats stats1_ on record2_.insert_date=stats1_.insert_date "
		+" and record2_.record_sequence=stats1_.record_sequence "
		+" where record0_.insert_date between ? and ? "
		+" and record2_.insert_date between ? and ? "
		+" and stats1_.stat_key = ? "
		+" and stats1_.stat_value = ? " 
		+" and (record0_.phase='send' and record0_.state='drop' or record0_.phase='receive' )";
	
	private RecordTranslator recordTranslator;
	
	@Autowired
	public JpaThreatsReportService(RecordDao recordDao,RecordTranslator recordTranslator) {
		super();
		this.recordDao = recordDao;
		this.recordTranslator = recordTranslator;
	}

	@Override
	public Collection getSpamHits(String domain, Calendar since) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		SetJoin<Record, Stat> statjoin= root.join(Record_.stats);
		Predicate predicate = builder.greaterThanOrEqualTo(root.get(Record_.id).get(RecordPk_.insertDate),sinceTime);
		if(domain!=null && !domain.trim().isEmpty()){
			predicate = builder.and(predicate,builder.or(builder.equal(root.get(Record_.senderDomainId), domain), builder.or(builder.equal(root.get(Record_.recipientDomainId), domain))));
		}
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.id).get(StatPk_.key), SPAM_DETECTED));
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.value), SPAM_DETECTED_VALUE));
		query = query.where(predicate);
		query = query.groupBy(builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public Collection getSpamHitsDay(String domain, Calendar since) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		Calendar until = Calendar.getInstance();
		until.setTimeInMillis(since.getTimeInMillis());
		until.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(until.getTimeInMillis());

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect(builder.count(root.get(Record_.id).get(RecordPk_.sequence))
				,builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate))
				,builder.function("hour", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		SetJoin<Record, Stat> statjoin= root.join(Record_.stats);
		Predicate predicate = builder.between(root.get(Record_.id).get(RecordPk_.insertDate),sinceTime,untilTime);
		if(domain!=null && !domain.trim().isEmpty()){
			predicate = builder.and(predicate,builder.or(builder.equal(root.get(Record_.senderDomainId), domain), builder.or(builder.equal(root.get(Record_.recipientDomainId), domain))));
		}
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.id).get(StatPk_.key), SPAM_DETECTED));
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.value), SPAM_DETECTED_VALUE));
		query = query.where(predicate);
		query = query.groupBy(builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate))
				,builder.function("hour", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		return entityManager.createQuery(query).getResultList();
	}
	
	@Override
	public Collection getVirusHits(String domain, Calendar since) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect((builder.count(root.get(Record_.id).get(RecordPk_.sequence))),builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		SetJoin<Record, Stat> statjoin= root.join(Record_.stats);
		Predicate predicate = builder.greaterThanOrEqualTo(root.get(Record_.id).get(RecordPk_.insertDate),sinceTime);
		if(domain!=null && !domain.trim().isEmpty()){
			predicate = builder.and(predicate,builder.or(builder.equal(root.get(Record_.senderDomainId), domain), builder.or(builder.equal(root.get(Record_.recipientDomainId), domain))));
		}
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.id).get(StatPk_.key), VIRUS_DETECTED));
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.value), VIRUS_DETECTED_VALUE));
		query = query.where(predicate);
		query = query.groupBy(builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public Collection getVirusHitsDay(String domain, Calendar since) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		Calendar until = Calendar.getInstance();
		until.setTimeInMillis(since.getTimeInMillis());
		until.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(until.getTimeInMillis());
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery query = builder.createQuery();
		Root<Record> root = query.from(Record.class);
		query = query.multiselect(builder.count(root.get(Record_.id).get(RecordPk_.sequence))
				,builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate))
				,builder.function("hour", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		SetJoin<Record, Stat> statjoin= root.join(Record_.stats);
		Predicate predicate = builder.greaterThanOrEqualTo(root.get(Record_.id).get(RecordPk_.insertDate),sinceTime);
		if(domain!=null && !domain.trim().isEmpty()){
			predicate = builder.and(predicate,builder.or(builder.equal(root.get(Record_.senderDomainId), domain), builder.or(builder.equal(root.get(Record_.recipientDomainId), domain))));
		}
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.id).get(StatPk_.key), VIRUS_DETECTED));
		predicate = builder.and(predicate, builder.equal(statjoin.get(Stat_.value), VIRUS_DETECTED_VALUE));
		query = query.where(predicate);
		query = query.groupBy(builder.function("date", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate))
				,builder.function("hour", Calendar.class, root.get(Record_.id).get(RecordPk_.insertDate)));
		return entityManager.createQuery(query).getResultList();
	}

	
	@Override
	public Collection<RecordVO> getSpamEmails(String domain, Calendar since, Calendar until) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		until.set(Calendar.HOUR, 0);
		until.set(Calendar.MINUTE, 0);
		until.set(Calendar.SECOND, 0);
		until.set(Calendar.MILLISECOND, 0);
		until.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(until.getTimeInMillis());

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " and (record0_.recipient_domain_id = ? or record0_.sender_domain_id = ?) ";
		}
		
		Query query = entityManager.createNativeQuery(queryString, Record.class);
		query.setParameter(1, sinceTime);
		query.setParameter(2, untilTime);
		query.setParameter(3, sinceTime);
		query.setParameter(4, untilTime);
		query.setParameter(5, "spam.detected");
		query.setParameter(6, "true");
		if(domain!=null && domain.trim().length()>0){
			query.setParameter(7, domain);
			query.setParameter(8, domain);
		}
		query.setMaxResults(PluginReportService.MAX_RESULT);
		return recordTranslator.translate(query.getResultList());

	}

	@Override
	public Collection<RecordVO> getVirusEmails(String domain, Calendar since, Calendar until) {
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		Timestamp sinceTime = new Timestamp(since.getTimeInMillis());
		until.set(Calendar.HOUR, 0);
		until.set(Calendar.MINUTE, 0);
		until.set(Calendar.SECOND, 0);
		until.set(Calendar.MILLISECOND, 0);
		until.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(until.getTimeInMillis());

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " and (record0_.recipient_domain_id = ? or record0_.sender_domain_id = ?) ";
		}
		
		Query query = entityManager.createNativeQuery(queryString, Record.class);
		query.setParameter(1, sinceTime);
		query.setParameter(2, untilTime);
		query.setParameter(3, sinceTime);
		query.setParameter(4, untilTime);
		query.setParameter(5, "virus.detected");
		query.setParameter(6, "true");

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(7, domain);
			query.setParameter(8, domain);
		}
		query.setMaxResults(PluginReportService.MAX_RESULT);
		return recordTranslator.translate(query.getResultList());
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	
}
