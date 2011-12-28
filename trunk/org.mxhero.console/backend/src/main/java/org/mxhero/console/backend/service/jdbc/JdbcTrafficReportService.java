package org.mxhero.console.backend.service.jdbc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import javax.sql.DataSource;

import org.mxhero.console.backend.service.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service("trafficReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JdbcTrafficReportService implements TrafficReportService {

	private NamedParameterJdbcTemplate template;
	
	@Autowired(required=true)
	public JdbcTrafficReportService(@Qualifier("statisticsDataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	public Collection getIncomming(String domain, long since, String offset) {
		String query = " SELECT tcount.c as `count`, tsize.s as `bytes`, tcount.ddate as `date`"
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date >= ? "
				+ " AND r0.stat_key = 'email.size' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tsize "
				+ " join (SELECT SUM(r0.amount) c, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 WHERE r0.insert_date >= ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tcount "
				+ " on tsize.ddate = tcount.ddate";

		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN$",
					" AND recipient_domain_id = ? ");
			return template.getJdbcOperations().queryForList(query,offset,new Timestamp(since),domain,offset,offset,new Timestamp(since),domain,offset);
		} else {
			query = query.replace("$REPLACE_DOMAIN$", "");
			return template.getJdbcOperations().queryForList(query,offset,new Timestamp(since),offset,offset,new Timestamp(since),offset);
		}
	}

	@Override
	public Collection getIncommingByDay(String domain, long day) {
		String query = " SELECT tcount.c as `count`, tsize.s as `bytes`,  DATE(tcount.ddate) as `date`, HOUR(tcount.ddate) as `hours` "
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, r0.insert_date ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date >= :amount_date_since AND r0.insert_date < :amount_date_to"
				+ " AND r0.stat_key = 'email.size' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN_AMOUNT$ "
				+ " group by r0.insert_date ) tsize "
				+ " join (SELECT SUM(r0.amount) c, r0.insert_date ddate "
				+ " FROM mail_stats_grouped r0 " 
				+ " WHERE r0.insert_date >= :size_date_since AND r0.insert_date < :size_date_to "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND recipient_domain_id is not null "
				+ " AND recipient_domain_id !='' "
				+ " $REPLACE_DOMAIN_SIZE$ "
				+ " group by r0.insert_date ) tcount "
				+ " on tsize.ddate = tcount.ddate";

		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());
		
		MapSqlParameterSource params = new MapSqlParameterSource("amount_date_since", since);
		params.addValue("amount_date_to", until);
		params.addValue("size_date_since", since);
		params.addValue("size_date_to", until);
		
		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN_AMOUNT$","AND recipient_domain_id = :domain_amount ");
			query = query.replace("$REPLACE_DOMAIN_SIZE$","AND recipient_domain_id = :domain_size ");
			params.addValue("domain_amount", domain);
			params.addValue("domain_size", domain);
		}else{
			query = query.replace("$REPLACE_DOMAIN_AMOUNT$","");
			query = query.replace("$REPLACE_DOMAIN_SIZE$","");
		}

		return template.queryForList(query, params);
	}



	@Override
	public Collection getOutgoing(String domain, long since, String offset) {
		String query = " SELECT tcount.c as `count`, tsize.s as `bytes`, tcount.ddate as `date`"
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date >= ? "
				+ " AND r0.stat_key = 'email.size' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tsize "
				+ " join (SELECT SUM(r0.amount) c, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ddate "
				+ " FROM mail_stats_grouped r0 WHERE r0.insert_date >= ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN$ "
				+ " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ) tcount "
				+ " on tsize.ddate = tcount.ddate";

		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN$",
					" AND sender_domain_id = ? ");
			return template.getJdbcOperations().queryForList(query,offset,new Timestamp(since),domain,offset,offset,new Timestamp(since),domain,offset);
		} else {
			query = query.replace("$REPLACE_DOMAIN$", "");
			return template.getJdbcOperations().queryForList(query,offset,new Timestamp(since),offset,offset,new Timestamp(since),offset);
		}
	}

	@Override
	public Collection getOutgoingByDay(String domain, long day) {
		String query = " SELECT tcount.c as `count`, tsize.s as `bytes`,  DATE(tcount.ddate) as `date`, HOUR(tcount.ddate) as `hours` "
				+ " FROM"
				+ " (SELECT SUM(r0.amount) s, r0.insert_date ddate "
				+ " FROM mail_stats_grouped r0 "
				+ " WHERE r0.insert_date >= :amount_date_since AND r0.insert_date < :amount_date_to"
				+ " AND r0.stat_key = 'email.size' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN_AMOUNT$ "
				+ " group by r0.insert_date ) tsize "
				+ " join (SELECT SUM(r0.amount) c, r0.insert_date ddate "
				+ " FROM mail_stats_grouped r0 " 
				+ " WHERE r0.insert_date >= :size_date_since AND r0.insert_date < :size_date_to "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND sender_domain_id is not null "
				+ " AND sender_domain_id !='' "
				+ " $REPLACE_DOMAIN_SIZE$ "
				+ " group by r0.insert_date ) tcount "
				+ " on tsize.ddate = tcount.ddate";
		
		Timestamp since = new Timestamp(day);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(day);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp until = new Timestamp(untilDate.getTimeInMillis());
		
		MapSqlParameterSource params = new MapSqlParameterSource("amount_date_since", since);
		params.addValue("amount_date_to", until);
		params.addValue("size_date_since", since);
		params.addValue("size_date_to", until);
		
		if (domain != null && !domain.isEmpty()) {
			query = query.replace("$REPLACE_DOMAIN_AMOUNT$","AND recipient_domain_id = :domain_amount ");
			query = query.replace("$REPLACE_DOMAIN_SIZE$","AND recipient_domain_id = :domain_size ");
			params.addValue("domain_amount", domain);
			params.addValue("domain_size", domain);
		}else{
			query = query.replace("$REPLACE_DOMAIN_AMOUNT$","");
			query = query.replace("$REPLACE_DOMAIN_SIZE$","");
		}
		
		return template.queryForList(query, params);
	}

}
