package org.mxhero.console.backend.service.jpa;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.service.TrafficReportService;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("trafficReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaTrafficReportService implements TrafficReportService {

	@PersistenceContext(unitName = "statisticsPer")
	protected EntityManager entityManager;

	@Override
	public Collection getIncomming(String domain,Calendar since) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date) date "
				+ "FROM  mail_records "
				+ "WHERE insert_date > :date_since "
				+ "AND (flow = 'both' OR flow = 'in') "
				+ "AND phase =  'receive' ";
		
		if(domain!=null && !domain.isEmpty()){
			query = query + "AND recipient_domain_id = :domain ";
		}	
		query = query + "GROUP BY DATE(insert_date) ";
		
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		
		nativeQuery.setParameter("date_since", since);
		if(domain!=null && !domain.isEmpty()){
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getIncommingByDay(String domain, Calendar day) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date), HOUR(insert_date) "
			+ "FROM  mail_records "
			+ "WHERE insert_date > :date_since "
			+ "AND insert_date < :date_to "
			+ "AND (flow = 'both' OR flow = 'in') "
			+ "AND phase =  'receive' ";
	
		if(domain!=null && !domain.isEmpty()){
			query = query + "AND recipient_domain_id = :domain ";
		}	
		query = query + "GROUP BY DATE(insert_date), HOUR(insert_date) ";
		
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		Calendar sinceDate = Calendar.getInstance();
		sinceDate.setTimeInMillis(day.getTimeInMillis());
		sinceDate.set(Calendar.HOUR, 0);
		sinceDate.set(Calendar.MINUTE, 0);
		sinceDate.set(Calendar.SECOND, 0);
		sinceDate.set(Calendar.MILLISECOND, 0);
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(day.getTimeInMillis());
		toDate.set(Calendar.HOUR, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);	
		toDate.add(Calendar.DATE, 1);
		nativeQuery.setParameter("date_since", sinceDate);
		nativeQuery.setParameter("date_to", toDate);
		if(domain!=null && !domain.isEmpty()){
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}
	
	@Override
	public Collection getTopTenIncomingSenders(String domain,Calendar since) {
		String query = "SELECT count(*) count, sender_id "
			+ "FROM  mail_records " 
			+ "WHERE insert_date > :date_since "
			+ "AND (flow = 'both' OR flow = 'in') "
			+ "AND phase =  'receive' ";
		if(domain!=null && !domain.isEmpty()){
			query = query + "AND recipient_domain_id = :domain ";
		}	
		query = query + "GROUP BY sender_id "
			+ "ORDER BY 1 DESC " 
			+ "LIMIT 10";
		
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		nativeQuery.setParameter("date_since", since);
		if(domain!=null && !domain.isEmpty()){
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getTopTenIncomingSendersByDay(String domain, Calendar day) {
		String query = "SELECT count(*) count, sender_id "
			+ "FROM  mail_records " 
			+ "WHERE insert_date > :date_since "
			+ "AND insert_date < :date_to "
			+ "AND (flow = 'both' OR flow = 'in') "
			+ "AND phase =  'receive' ";
		if(domain!=null && !domain.isEmpty()){
			query = query + "AND recipient_domain_id = :domain ";
		}	
		query = query + "GROUP BY sender_id "
			+ "ORDER BY 1 DESC " 
			+ "LIMIT 10";
		
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		Calendar sinceDate = Calendar.getInstance();
		sinceDate.setTimeInMillis(day.getTimeInMillis());
		sinceDate.set(Calendar.HOUR, 0);
		sinceDate.set(Calendar.MINUTE, 0);
		sinceDate.set(Calendar.SECOND, 0);
		sinceDate.set(Calendar.MILLISECOND, 0);
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(day.getTimeInMillis());
		toDate.set(Calendar.HOUR, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);	
		toDate.add(Calendar.DATE, 1);
		nativeQuery.setParameter("date_since", sinceDate);
		nativeQuery.setParameter("date_to", toDate);
		if(domain!=null && !domain.isEmpty()){
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}
	
	@Override
	public Collection getOutgoing(String domain, Calendar since) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date) date "
			+ "FROM  mail_records "
			+ "WHERE insert_date > :date_since "
			+ "AND (flow = 'both' OR flow = 'out') "
			+ "AND phase =  'send' ";
		if(domain!=null && !domain.isEmpty()){	
			query = query + "AND recipient_domain_id = :domain ";
		}
		query = query + "GROUP BY DATE(insert_date) ";
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		nativeQuery.setParameter("date_since", since);
		if(domain!=null && !domain.isEmpty()){	
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getOutgoingByDay(String domain, Calendar day) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date), HOUR(insert_date) "
			+ "FROM  mail_records "
			+ "WHERE insert_date > :date_since "
			+ "AND insert_date < :date_to "
			+ "AND (flow = 'both' OR flow = 'out') "
			+ "AND phase =  'send' ";
		if(domain!=null && !domain.isEmpty()){	
			query = query + "AND recipient_domain_id = :domain ";
		}
		query = query + "GROUP BY DATE(insert_date), HOUR(insert_date) ";
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		Calendar sinceDate = Calendar.getInstance();
		sinceDate.setTimeInMillis(day.getTimeInMillis());
		sinceDate.set(Calendar.HOUR, 0);
		sinceDate.set(Calendar.MINUTE, 0);
		sinceDate.set(Calendar.SECOND, 0);
		sinceDate.set(Calendar.MILLISECOND, 0);
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(day.getTimeInMillis());
		toDate.set(Calendar.HOUR, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);	
		toDate.add(Calendar.DATE, 1);
		nativeQuery.setParameter("date_since", sinceDate);
		nativeQuery.setParameter("date_to", toDate);
		if(domain!=null && !domain.isEmpty()){	
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}
	
	@Override
	public Collection getTopTenOutgoingRecipients(String domain, Calendar since) {
		String query = "SELECT count(*) count, recipient_id "
			+ "FROM  mail_records " + "WHERE insert_date > :date_since "
			+ "AND (flow = 'both' OR flow = 'out') "
			+ "AND phase =  'send' ";
		if(domain!=null && !domain.isEmpty()){	
			query = query + "AND sender_domain_id = :domain ";
		}
		query = query + "GROUP BY recipient_id "
			+ "ORDER BY 1 DESC " 
			+ "LIMIT 10";
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		since.set(Calendar.HOUR, 0);
		since.set(Calendar.MINUTE, 0);
		since.set(Calendar.SECOND, 0);
		since.set(Calendar.MILLISECOND, 0);
		nativeQuery.setParameter("date_since", since);
		if(domain!=null && !domain.isEmpty()){	
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}



	@Override
	public Collection getTopTenOutgoingRecipientsByDay(String domain,
			Calendar day) {
		String query = "SELECT count(*) count, recipient_id "
			+ "FROM  mail_records " 
			+ "WHERE insert_date > :date_since "
			+ "AND insert_date < :date_to "
			+ "AND (flow = 'both' OR flow = 'out') "
			+ "AND phase =  'send' ";
		if(domain!=null && !domain.isEmpty()){	
			query = query + "AND sender_domain_id = :domain ";
		}
		query = query + "GROUP BY recipient_id "
			+ "ORDER BY 1 DESC " 
			+ "LIMIT 10";
		Query nativeQuery = this.entityManager.createNativeQuery(query);
		Calendar sinceDate = Calendar.getInstance();
		sinceDate.setTimeInMillis(day.getTimeInMillis());
		sinceDate.set(Calendar.HOUR, 0);
		sinceDate.set(Calendar.MINUTE, 0);
		sinceDate.set(Calendar.SECOND, 0);
		sinceDate.set(Calendar.MILLISECOND, 0);
		Calendar toDate = Calendar.getInstance();
		toDate.setTimeInMillis(day.getTimeInMillis());
		toDate.set(Calendar.HOUR, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);	
		toDate.add(Calendar.DATE, 1);
		nativeQuery.setParameter("date_since", sinceDate);
		nativeQuery.setParameter("date_to", toDate);
		if(domain!=null && !domain.isEmpty()){	
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

}
