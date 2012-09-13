/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.console.backend.service.jdbc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.jdbc.mapper.RecordMapper;
import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.service.ThreatsReportService;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbcThreatsReportService")
public class JdbcThreatsReportService implements ThreatsReportService {

	private static final String SPAM_DETECTED = "spam.detected";
	private static final String SPAM_DETECTED_VALUE = "true";
	private static final String VIRUS_DETECTED = "virus.detected";
	private static final String VIRUS_DETECTED_VALUE = "true";
	
	private NamedParameterJdbcTemplate template;
	
	String hitsSqlQuery = "SELECT SUM(r0.amount) as `count`, date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) as `date` " 
		+" FROM mail_stats_grouped r0 " 
		+" WHERE r0.insert_date >= ? "
		+" AND r0.stat_key = ? " 
		+" AND r0.stat_value = ? ";
	
	String dayHitsSqlQuery = "SELECT SUM(r0.amount) as `count`, date(r0.insert_date) as `date`, hour(r0.insert_date) as `hours` " 
		+" FROM mail_stats_grouped r0 " 
		+" WHERE r0.insert_date >= ? and r0.insert_date < ?"
		+" AND r0.stat_key = ? " 
		+" AND r0.stat_value = ? ";
	
	String mailQuerySql = "SELECT r0.* " 
		+" FROM mail_records r0 " 
		+" WHERE r0.insert_date between ? AND ? "
		+" AND EXISTS( SELECT 1 FROM mail_stats s "
								+" WHERE s.insert_date = r0.insert_date " 
								+" AND s.record_sequence = r0.record_sequence " 
								+" AND s.server_name = r0.server_name " 
								+" AND s.stat_key = ? " 
								+" AND s.stat_value = ?) ";
	
	@Autowired(required=true)
	public JdbcThreatsReportService(@Qualifier("statisticsDataSource")DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	@Override
	public Collection getSpamHits(String domain, long since, String offset) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = hitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		queryString = queryString + " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ";
		
		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().queryForList(queryString, offset,sinceTime,SPAM_DETECTED,SPAM_DETECTED_VALUE,domain,domain,offset);
		}else{
			return template.getJdbcOperations().queryForList(queryString, offset,sinceTime,SPAM_DETECTED,SPAM_DETECTED_VALUE,offset);
		}
	}

	@Override
	public Collection getSpamHitsDay(String domain, long since) {
		Timestamp sinceTime = new Timestamp(since);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(since);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(untilDate.getTimeInMillis());
		
		String queryString = dayHitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(r0.insert_date),hour(r0.insert_date) ";

		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().queryForList(queryString, sinceTime,untilTime, SPAM_DETECTED, SPAM_DETECTED_VALUE, domain, domain);
		}else{
			return template.getJdbcOperations().queryForList(queryString, sinceTime,untilTime, SPAM_DETECTED, SPAM_DETECTED_VALUE);
		}
	}
	
	@Override
	public Collection getVirusHits(String domain, long since, String offset) {
		Timestamp sinceTime = new Timestamp(since);

		String queryString = hitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(CONVERT_TZ(r0.insert_date, '+00:00', ? )) ";
		
		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().queryForList(queryString, offset,sinceTime,VIRUS_DETECTED,VIRUS_DETECTED_VALUE,domain,domain,offset);
		}else{
			return template.getJdbcOperations().queryForList(queryString, offset,sinceTime,VIRUS_DETECTED,VIRUS_DETECTED_VALUE,offset);
		}
	}

	@Override
	public Collection getVirusHitsDay(String domain, long since) {
		Timestamp sinceTime = new Timestamp(since);
		Calendar untilDate = Calendar.getInstance();
		untilDate.setTimeInMillis(since);
		untilDate.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp untilTime = new Timestamp(untilDate.getTimeInMillis());
		
		String queryString = dayHitsSqlQuery;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " group by date(r0.insert_date),hour(r0.insert_date) ";
		
		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().queryForList(queryString, sinceTime, untilTime, VIRUS_DETECTED, VIRUS_DETECTED_VALUE, domain, domain);
		}else{
			return template.getJdbcOperations().queryForList(queryString, sinceTime, untilTime, VIRUS_DETECTED, VIRUS_DETECTED_VALUE);
		}
	}

	@Override
	public Collection<RecordVO> getSpamEmails(String domain, long since, long until) {

		Timestamp sinceTime = new Timestamp(since);
		Timestamp untilTime = new Timestamp(until);

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " ORDER BY r0.insert_date DESC limit "+PluginReportService.MAX_RESULT+" ;";
		
		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().query(queryString, new Object[]{sinceTime,untilTime,SPAM_DETECTED,SPAM_DETECTED_VALUE,domain,domain}, new RecordMapper());
		}else{
			return template.getJdbcOperations().query(queryString, new Object[]{sinceTime,untilTime,SPAM_DETECTED,SPAM_DETECTED_VALUE}, new RecordMapper());
		}
	}

	@Override
	public Collection<RecordVO> getVirusEmails(String domain, long since, long until) {
		Timestamp sinceTime = new Timestamp(since);
		Timestamp untilTime = new Timestamp(until);

		String queryString = mailQuerySql;
		if(domain!=null && domain.trim().length()>0){
			queryString = queryString + " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		}
		
		queryString = queryString + " ORDER BY r0.insert_date DESC limit "+PluginReportService.MAX_RESULT+" ;";
		
		if(domain!=null && domain.trim().length()>0){
			return template.getJdbcOperations().query(queryString, new Object[]{sinceTime,untilTime,VIRUS_DETECTED,VIRUS_DETECTED_VALUE,domain,domain}, new RecordMapper());
		}else{
			return template.getJdbcOperations().query(queryString, new Object[]{sinceTime,untilTime,VIRUS_DETECTED,VIRUS_DETECTED_VALUE}, new RecordMapper());
		}
	}
	
}
