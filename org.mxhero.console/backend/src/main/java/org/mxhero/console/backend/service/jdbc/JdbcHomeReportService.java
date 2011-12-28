package org.mxhero.console.backend.service.jdbc;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.jdbc.mapper.DomainMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.FeatureRuleMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.GroupMapper;
import org.mxhero.console.backend.service.HomeReportService;
import org.mxhero.console.backend.vo.ActivityDataVO;
import org.mxhero.console.backend.vo.MessagesCompositionVO;
import org.mxhero.console.backend.vo.MxHeroDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service("homeReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JdbcHomeReportService implements HomeReportService{


	private NamedParameterJdbcTemplate mxheroTemplate;
	
	private NamedParameterJdbcTemplate statisticsTemplate;
	
	@Autowired(required=true)
	public JdbcHomeReportService(@Qualifier("mxheroDataSource")DataSource mxheroDs,
							@Qualifier("statisticsDataSource")DataSource statisticsDs){
		this.mxheroTemplate = new NamedParameterJdbcTemplate(mxheroDs);
		this.statisticsTemplate = new NamedParameterJdbcTemplate(statisticsDs);
	}
	
	@Override
	public MxHeroDataVO getMxHeroData(String domainId) {
		MxHeroDataVO data = new MxHeroDataVO();
		
		if(domainId==null){
			data.setDomains(mxheroTemplate.getJdbcOperations().queryForLong("SELECT COUNT(*) FROM `"+DomainMapper.DATABASE+"`.`"+DomainMapper.TABLE_NAME+"` ;"));
			data.setGroups(null);
			data.setAccounts(mxheroTemplate.getJdbcOperations().queryForLong("SELECT COUNT(*) FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"` ;"));
			data.setEnabledRules(mxheroTemplate.getJdbcOperations().queryForLong("SELECT COUNT(*) FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` WHERE `"+FeatureRuleMapper.ENABLED+"` = true ;"));
			data.setDisableRules(mxheroTemplate.getJdbcOperations().queryForLong("SELECT COUNT(*) FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` WHERE `"+FeatureRuleMapper.ENABLED+"` = false ;"));
		}else{
			data.setDomains(null);
			data.setGroups(mxheroTemplate.queryForLong("SELECT COUNT(*) FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"` WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId ", new MapSqlParameterSource("domainId",domainId)));
			data.setAccounts(mxheroTemplate.queryForLong("SELECT COUNT(*) FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"` WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId ", new MapSqlParameterSource("domainId",domainId)));
			data.setEnabledRules(mxheroTemplate.queryForLong("SELECT COUNT(*) FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` WHERE `"+FeatureRuleMapper.ENABLED+"` = true AND `"+FeatureRuleMapper.DOMAIN_ID+"` = :domainId ", new MapSqlParameterSource("domainId",domainId)));
			data.setDisableRules(mxheroTemplate.queryForLong("SELECT COUNT(*) FROM `"+FeatureRuleMapper.DATABASE+"`.`"+FeatureRuleMapper.TABLE_NAME+"` WHERE `"+FeatureRuleMapper.ENABLED+"` = false AND `"+FeatureRuleMapper.DOMAIN_ID+"` = :domainId ", new MapSqlParameterSource("domainId",domainId)));			
		}

		return data;
	}

	@Override
	public MessagesCompositionVO getMessagesCompositionData(long since, String domainId) {
		MessagesCompositionVO messages = new MessagesCompositionVO();
		
		String hitsSqlQuery = "SELECT COALESCE(SUM(r0.amount),0)" 
			+" FROM mail_stats_grouped r0 " 
			+" WHERE r0.insert_date >= ? "
			+" AND r0.stat_key = ? " 
			+" AND r0.stat_value = ? ";
		
		String hitsSqlQueryNoValue = "SELECT COALESCE(SUM(r0.amount),0)" 
			+" FROM mail_stats_grouped r0 " 
			+" WHERE r0.insert_date >= ? "
			+" AND r0.stat_key = ? ";
		
		if(domainId==null){
			long total = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQueryNoValue, new Object[]{new Timestamp(since),"email.count"});
			long spam = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQuery,new Object[]{new Timestamp(since),"spam.detected","true"});
			long virus = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQuery,new Object[]{new Timestamp(since),"virus.detected","true"});				
			long blocked = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQueryNoValue,new Object[]{new Timestamp(since),"email.blocked"});
			
			messages.setSpam(spam);
			messages.setVirus(virus);
			messages.setBlocked(blocked);
			messages.setClean(total-spam-virus-blocked);
		}else{
			long total = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQueryNoValue+" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ", new Object[]{new Timestamp(since),"email.count",domainId,domainId});
			long spam = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQuery+" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ",new Object[]{new Timestamp(since),"spam.detected","true",domainId,domainId});
			long virus = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQuery+" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ",new Object[]{new Timestamp(since),"virus.detected","true",domainId,domainId});				
			long blocked = statisticsTemplate.getJdbcOperations().queryForLong(hitsSqlQueryNoValue+" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ",new Object[]{new Timestamp(since),"email.blocked",domainId,domainId});
			
			messages.setSpam(spam);
			messages.setVirus(virus);
			messages.setBlocked(blocked);
			messages.setClean(total-spam-virus-blocked);
		}
		
		return messages;
	}
	
	@Override
	public ActivityDataVO getActivity(long since, String domainId) {
		ActivityDataVO data = new ActivityDataVO();
		String threatsQuery = "SELECT COUNT(*) as `count`, date_format(r0.insert_date,'%Y-%m-%d %H:%i') as `date` FROM mail_records r0 " 
				+" WHERE r0.insert_date > ? "
				+" AND EXISTS( SELECT 1 FROM mail_stats s "
										+" WHERE s.insert_date = r0.insert_date " 
										+" AND s.record_sequence = r0.record_sequence " 
										+" AND s.server_name = r0.server_name " 
										+" AND ${CONDITION}) ";
		String trafficQuery =" SELECT COUNT(*) as `count`, date_format(r0.insert_date,'%Y-%m-%d %H:%i') as `date` FROM mail_records r0 " +
				" WHERE r0.insert_date >= ? ";
		String incomming = " AND (r0.flow = 'both' OR r0.flow = 'in') ";
		String outgoing = " AND (r0.flow = 'both' OR r0.flow = 'out') ";
		String group ="GROUP BY  date_format(r0.insert_date,'%Y-%m-%d %H:%i')";

		if(domainId==null){
			data.setIncomming(statisticsTemplate.getJdbcOperations().queryForList(trafficQuery.concat(incomming).concat(group),new Object[]{new Timestamp(since)}));
			data.setOutgoing(statisticsTemplate.getJdbcOperations().queryForList(trafficQuery.concat(outgoing).concat(group),new Object[]{new Timestamp(since)}));
			data.setSpam(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'"),new Object[]{new Timestamp(since)}));
			data.setVirus(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'"),new Object[]{new Timestamp(since)}));
			data.setBlocked(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'"),new Object[]{new Timestamp(since)}));
		}else{
			data.setIncomming(statisticsTemplate.getJdbcOperations().queryForList(trafficQuery.concat(incomming).concat(" AND r0.recipient_domain_id = ? ").concat(group),new Object[]{new Timestamp(since),domainId}));
			data.setOutgoing(statisticsTemplate.getJdbcOperations().queryForList(trafficQuery.concat(outgoing).concat(" AND r0.sender_domain_id = ? ").concat(group),new Object[]{new Timestamp(since),domainId}));
			data.setSpam(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'"),new Object[]{new Timestamp(since),domainId,domainId}));
			data.setVirus(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'"),new Object[]{new Timestamp(since),domainId,domainId}));
			data.setBlocked(statisticsTemplate.getJdbcOperations().queryForList(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'"),new Object[]{new Timestamp(since),domainId,domainId}));		
		}
		
		return data;
	}

	@Override
	public ActivityDataVO getActivityByHour(long since, String domainId){
		ActivityDataVO data = new ActivityDataVO();
		
		String groupBy = " GROUP BY DATE(r0.insert_date), HOUR(r0.insert_date) ";
		String anyDomain = " AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ";
		String senderDomain = " AND  r0.sender_domain_id = ? ";
		String recipientDomain = " AND  r0.recipient_domain_id = ? ";
		String incommingQuery = " SELECT SUM(r0.amount) as `count`, DATE(r0.insert_date) as `date`, HOUR(r0.insert_date) as `hours` "
				+ " FROM  mail_stats_grouped r0 "
				+ " WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND r0.recipient_domain_id is not null "
				+ " AND r0.recipient_domain_id !='' " ;
		String outgoingQuery = "SELECT SUM(r0.amount) as `count`, DATE(r0.insert_date) as `date`, HOUR(r0.insert_date) as `hours` "
				+ " FROM  mail_stats_grouped r0 "
				+ " WHERE r0.insert_date > ? "
				+ " AND r0.stat_key = 'email.count' "
				+ " AND r0.sender_domain_id is not null "
				+ " AND r0.sender_domain_id !='' ";
		String dayHitsSql = "SELECT SUM(r0.amount) as `count`, DATE(r0.insert_date) as `date`, HOUR(r0.insert_date) as `hours` " 
				+" FROM mail_stats_grouped r0 " 
				+" WHERE r0.insert_date > ? "
				+" AND r0.stat_key = ? " 
				+" AND r0.stat_value = 'true' ";
		String dayHitsNoValueSql = "SELECT SUM(r0.amount) as `count`, DATE(r0.insert_date) as `date`, HOUR(r0.insert_date) as `hours` " 
				+" FROM mail_stats_grouped r0 " 
				+" WHERE r0.insert_date > ? "
				+" AND r0.stat_key = ? ";		
		if(domainId==null){
			data.setIncomming(statisticsTemplate.getJdbcOperations().queryForList(incommingQuery.concat(groupBy),new Object[]{new Timestamp(since)}));
			data.setOutgoing(statisticsTemplate.getJdbcOperations().queryForList(outgoingQuery.concat(groupBy),new Object[]{new Timestamp(since)}));
			data.setVirus(statisticsTemplate.getJdbcOperations().queryForList(dayHitsSql.concat(groupBy),new Object[]{new Timestamp(since),"virus.detected"}));
			data.setSpam(statisticsTemplate.getJdbcOperations().queryForList(dayHitsSql.concat(groupBy),new Object[]{new Timestamp(since),"spam.detected"}));
			data.setBlocked(statisticsTemplate.getJdbcOperations().queryForList(dayHitsNoValueSql.concat(groupBy),new Object[]{new Timestamp(since),"email.blocked"}));
		}else{
			data.setIncomming(statisticsTemplate.getJdbcOperations().queryForList(incommingQuery.concat(recipientDomain).concat(groupBy),new Object[]{new Timestamp(since),domainId}));
			data.setIncomming(statisticsTemplate.getJdbcOperations().queryForList(outgoingQuery.concat(senderDomain).concat(groupBy),new Object[]{new Timestamp(since),domainId}));
			data.setVirus(statisticsTemplate.getJdbcOperations().queryForList(dayHitsSql.concat(anyDomain).concat(groupBy),new Object[]{new Timestamp(since),"virus.detected",domainId,domainId}));
			data.setSpam(statisticsTemplate.getJdbcOperations().queryForList(dayHitsSql.concat(anyDomain).concat(groupBy),new Object[]{new Timestamp(since),"spam.detected",domainId,domainId}));
			data.setBlocked(statisticsTemplate.getJdbcOperations().queryForList(dayHitsNoValueSql.concat(anyDomain).concat(groupBy),new Object[]{new Timestamp(since),"email.blocked",domainId,domainId}));
		}
		
		return data;
	}
	
}
