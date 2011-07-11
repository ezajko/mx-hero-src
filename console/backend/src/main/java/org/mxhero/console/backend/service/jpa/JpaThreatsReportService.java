package org.mxhero.console.backend.service.jpa;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.service.ThreatsReportService;
import org.mxhero.console.backend.statistics.dao.RecordDao;
import org.mxhero.console.backend.statistics.entity.Record;
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
	
	String hitsSqlQuery = "SELECT COUNT(*),date(CONVERT_TZ(r0.insert_date, '+00:00', ? ))" 
		+" FROM mail_records r0 " 
		+" WHERE r0.insert_date > ? "
		+" AND EXISTS( SELECT 1 FROM mail_stats s "
								+" WHERE s.insert_date = r0.insert_date " 
								+" AND s.record_sequence = r0.record_sequence " 
								+" AND s.stat_key = ? " 
								+" AND s.stat_value = ?) ";
	
	String dayHitsSqlQuery = "SELECT COUNT(*),date(r0.insert_date),hour(r0.insert_date)" 
		+" FROM mail_records r0 " 
		+" WHERE r0.insert_date > ? "
		+" AND EXISTS( SELECT 1 FROM mail_stats s "
								+" WHERE s.insert_date = r0.insert_date " 
								+" AND s.record_sequence = r0.record_sequence " 
								+" AND s.stat_key = ? " 
								+" AND s.stat_value = ?) ";
	
	String mailQuerySql = "SELECT r0.*" 
		+" FROM mail_records r0 " 
		+" WHERE r0.insert_date between ? AND ? "
		+" AND EXISTS( SELECT 1 FROM mail_stats s "
								+" WHERE s.insert_date = r0.insert_date " 
								+" AND s.record_sequence = r0.record_sequence " 
								+" AND s.stat_key = ? " 
								+" AND s.stat_value = ?) ";
	
	private RecordTranslator recordTranslator;
	
	@Autowired
	public JpaThreatsReportService(RecordDao recordDao,RecordTranslator recordTranslator) {
		super();
		this.recordDao = recordDao;
		this.recordTranslator = recordTranslator;
	}

	@Override
	public Collection getSpamHits(String domain, long since, String offset) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = hitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ";
		
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter(1, offset);
		query.setParameter(2, sinceTime);
		query.setParameter(3, SPAM_DETECTED);
		query.setParameter(4, SPAM_DETECTED_VALUE);

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(5, domain);
			query.setParameter(6, domain);
			query.setParameter(7, offset);
		}else{
			query.setParameter(5, offset);
		}
		
		return query.getResultList();
	}

	@Override
	public Collection getSpamHitsDay(String domain, long since) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = dayHitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(r0.insert_date),hour(r0.insert_date) ";
		
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter(1, sinceTime);
		query.setParameter(2, SPAM_DETECTED);
		query.setParameter(3, SPAM_DETECTED_VALUE);

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(4, domain);
			query.setParameter(5, domain);
		}
		
		return query.getResultList();
	}
	
	@Override
	public Collection getVirusHits(String domain, long since, String offset) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = hitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ";
		
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter(1, offset);
		query.setParameter(2, sinceTime);
		query.setParameter(3, VIRUS_DETECTED);
		query.setParameter(4, VIRUS_DETECTED_VALUE);

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(5, domain);
			query.setParameter(6, domain);
			query.setParameter(7, offset);
		}else{
			query.setParameter(5, offset);
		}
		
		return query.getResultList();
	}

	@Override
	public Collection getVirusHitsDay(String domain, long since) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = dayHitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(r0.insert_date),hour(r0.insert_date) ";
		
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter(1, sinceTime);
		query.setParameter(2, VIRUS_DETECTED);
		query.setParameter(3, VIRUS_DETECTED_VALUE);

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(4, domain);
			query.setParameter(5, domain);
		}
		
		return query.getResultList();
	}

	
	@Override
	public Collection<RecordVO> getSpamEmails(String domain, long since, long until) {

		Timestamp sinceTime = new Timestamp(since);
		Timestamp untilTime = new Timestamp(until);

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " ORDER BY r0.insert_date DESC ";
		
		Query query = entityManager.createNativeQuery(queryString, Record.class);
		query.setParameter(1, sinceTime);
		query.setParameter(2, untilTime);
		query.setParameter(3, SPAM_DETECTED);
		query.setParameter(4, SPAM_DETECTED_VALUE);
		if(domain!=null && domain.trim().length()>0){
			query.setParameter(5, domain);
			query.setParameter(6, domain);
		}
		query.setMaxResults(PluginReportService.MAX_RESULT);
		return recordTranslator.translate(query.getResultList());

	}

	@Override
	public Collection<RecordVO> getVirusEmails(String domain, long since, long until) {
		Timestamp sinceTime = new Timestamp(since);
		Timestamp untilTime = new Timestamp(until);

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " ORDER BY r0.insert_date DESC ";
		
		Query query = entityManager.createNativeQuery(queryString, Record.class);
		query.setParameter(1, sinceTime);
		query.setParameter(2, untilTime);
		query.setParameter(3, VIRUS_DETECTED);
		query.setParameter(4, VIRUS_DETECTED_VALUE);

		if(domain!=null && domain.trim().length()>0){
			query.setParameter(5, domain);
			query.setParameter(6, domain);
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
