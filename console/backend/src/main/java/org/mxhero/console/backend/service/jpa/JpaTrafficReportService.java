package org.mxhero.console.backend.service.jpa;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mxhero.console.backend.service.TrafficReportService;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("trafficReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaTrafficReportService implements TrafficReportService {

	@PersistenceContext(unitName = "statisticsPer")
	protected EntityManager entityManager;

	@Override
	public Collection getIncomming(String domain, long since, String offset) {
		String query = " SELECT tcount.c, tsize.s, tcount.ddate "
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.size' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tsize "
				+ " join (SELECT SUM(r0.amount) c, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tcount "
				+ " on tsize.ddate = tcount.ddate";

		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN$",
					" AND recipient_domain_id = ? ");
		} else {
			query = query.replace("$REPLACE_DOMAIN$", "");
		}

		Query nativeQuery = this.entityManager.createNativeQuery(query);

		nativeQuery.setParameter(1, offset);
		nativeQuery.setParameter(2, new Timestamp(since));
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter(3, domain);
			nativeQuery.setParameter(4, offset);
			nativeQuery.setParameter(5, offset);
			nativeQuery.setParameter(6, new Timestamp(since));
			nativeQuery.setParameter(7, domain);
			nativeQuery.setParameter(8, offset);
		} else {
			nativeQuery.setParameter(3, offset);
			nativeQuery.setParameter(4, offset);
			nativeQuery.setParameter(5, new Timestamp(since));
			nativeQuery.setParameter(6, offset);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getIncommingByDay(String domain, long day) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date), HOUR(insert_date) "
				+ "FROM  mail_records "
				+ "WHERE insert_date > :date_since "
				+ "AND insert_date < :date_to "
				+ "AND (flow = 'both' OR flow = 'in') ";

		if (domain != null && !domain.isEmpty()) {
			query = query + "AND recipient_domain_id = :domain ";
		}
		query = query + "GROUP BY DATE(insert_date), HOUR(insert_date) ";

		Query nativeQuery = this.entityManager.createNativeQuery(query);

		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());

		nativeQuery.setParameter("date_since", since);
		nativeQuery.setParameter("date_to", until);

		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getTopTenIncomingSenders(String domain, long since,
			boolean domainOnly) {
		String query = "SELECT count(*) count, from_recipeints "
				+ "FROM  mail_records r " + "WHERE insert_date > :date_since ";
		if (domain != null && !domain.isEmpty()) {
			query = query + " AND sender_domain_id = :domain ";
			if (domainOnly) {
				query = query + " AND recipient_domain_id = :domain ";
			}
			query = query + " AND (flow = 'both' OR flow = 'in') ";
		} else {
			if (domainOnly) {
				query = query + " AND flow = 'both' ";
			} else {
				query = query + " AND (flow = 'both' OR flow = 'in') ";
			}
		}

		query = query + " GROUP BY from_recipeints " + " ORDER BY 1 DESC "
				+ " LIMIT 10 ";

		Query nativeQuery = this.entityManager.createNativeQuery(query);

		nativeQuery.setParameter("date_since", new Timestamp(since));
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getTopTenIncomingSendersByDay(String domain, long day,
			boolean domainOnly) {
		String query = "SELECT count(*) count, from_recipeints "
				+ "FROM  mail_records " + "WHERE insert_date > :date_since "
				+ "AND insert_date < :date_to ";

		if (domain != null && !domain.isEmpty()) {
			query = query + " AND sender_domain_id = :domain ";
			if (domainOnly) {
				query = query + " AND recipient_domain_id = :domain ";
			}
			query = query + " AND (flow = 'both' OR flow = 'in') ";
		} else {
			if (domainOnly) {
				query = query + " AND flow = 'both' ";
			} else {
				query = query + " AND (flow = 'both' OR flow = 'in') ";
			}
		}

		query = query + " GROUP BY from_recipeints " + " ORDER BY 1 DESC "
				+ " LIMIT 10";

		Query nativeQuery = this.entityManager.createNativeQuery(query);
		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());

		nativeQuery.setParameter("date_since", since);
		nativeQuery.setParameter("date_to", until);

		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getOutgoing(String domain, long since, String offset) {
		String query = " SELECT tcount.c, tsize.s, tcount.ddate "
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.size' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tsize "
				+ " join (SELECT SUM(r0.amount) c, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tcount "
				+ " on tsize.ddate = tcount.ddate";

		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN$",
					" AND sender_domain_id = ? ");
		} else {
			query = query.replace("$REPLACE_DOMAIN$", "");
		}

		Query nativeQuery = this.entityManager.createNativeQuery(query);

		nativeQuery.setParameter(1, offset);
		nativeQuery.setParameter(2, new Timestamp(since));
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter(3, domain);
			nativeQuery.setParameter(4, offset);
			nativeQuery.setParameter(5, offset);
			nativeQuery.setParameter(6, new Timestamp(since));
			nativeQuery.setParameter(7, domain);
			nativeQuery.setParameter(8, offset);
		} else {
			nativeQuery.setParameter(3, offset);
			nativeQuery.setParameter(4, offset);
			nativeQuery.setParameter(5, new Timestamp(since));
			nativeQuery.setParameter(6, offset);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getOutgoingByDay(String domain, long day) {
		String query = "SELECT count(*) count, sum(bytes_size) bytes, DATE(insert_date), HOUR(insert_date) "
				+ "FROM  mail_records "
				+ "WHERE insert_date > :date_since "
				+ "AND insert_date < :date_to "
				+ "AND (flow = 'both' OR flow = 'out') ";
		if (domain != null && !domain.isEmpty()) {
			query = query + "AND recipient_domain_id = :domain ";
		}
		query = query + "GROUP BY DATE(insert_date), HOUR(insert_date) ";
		Query nativeQuery = this.entityManager.createNativeQuery(query);

		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());

		nativeQuery.setParameter("date_since", since);
		nativeQuery.setParameter("date_to", until);
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getTopTenOutgoingRecipients(String domain, long since,
			boolean domainOnly) {
		String query = "SELECT count(*) count, recipient "
				+ "FROM  mail_records " + "WHERE insert_date > :date_since ";
		if (domain != null && !domain.isEmpty()) {
			query = query + " AND recipient_domain_id = :domain ";
			if (domainOnly) {
				query = query + " AND sender_domain_id = :domain ";
			}
			query = query + " AND (flow = 'both' OR flow = 'out') ";
		} else {
			if (domainOnly) {
				query = query + " AND flow = 'both' ";
			} else {
				query = query + " AND (flow = 'both' OR flow = 'out') ";
			}
		}

		query = query + "GROUP BY recipient " + "ORDER BY 1 DESC " + "LIMIT 10";
		Query nativeQuery = this.entityManager.createNativeQuery(query);

		nativeQuery.setParameter("date_since", new Timestamp(since));
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

	@Override
	public Collection getTopTenOutgoingRecipientsByDay(String domain, long day,
			boolean domainOnly) {
		String query = "SELECT count(*) count, recipient "
				+ "FROM  mail_records " + "WHERE insert_date > :date_since "
				+ "AND insert_date < :date_to ";
		if (domain != null && !domain.isEmpty()) {
			query = query + " AND recipient_domain_id = :domain ";
			if (domainOnly) {
				query = query + " AND sender_domain_id = :domain ";
			}
			query = query + " AND (flow = 'both' OR flow = 'out') ";
		} else {
			if (domainOnly) {
				query = query + " AND flow = 'both' ";
			} else {
				query = query + " AND (flow = 'both' OR flow = 'out') ";
			}
		}

		query = query + "GROUP BY recipient " + "ORDER BY 1 DESC " + "LIMIT 10";
		Query nativeQuery = this.entityManager.createNativeQuery(query);

		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());

		nativeQuery.setParameter("date_since", since);
		nativeQuery.setParameter("date_to", until);
		if (domain != null && !domain.isEmpty()) {
			nativeQuery.setParameter("domain", domain);
		}
		return nativeQuery.getResultList();
	}

}
