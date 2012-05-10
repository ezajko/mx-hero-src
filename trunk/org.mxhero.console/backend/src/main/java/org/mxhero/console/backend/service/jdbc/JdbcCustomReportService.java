package org.mxhero.console.backend.service.jdbc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.jdbc.mapper.RecordMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.RecordStatMapper;
import org.mxhero.console.backend.service.CustomReportService;
import org.mxhero.console.backend.service.PluginReportService;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordStatVO;
import org.mxhero.console.backend.vo.RecordVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("jdbcCustomReportService")
public class JdbcCustomReportService implements CustomReportService{

	private static final String INDIVIDUAL="individual";
	private static final String GROUP="group";
	private static final String DOMAIN="domain";	
	private static final String ALLDOMAINS="alldomains";
	private static final String ANYONEELSE="anyoneelse";
	private static Logger log = LoggerFactory.getLogger(JdbcCustomReportService.class);
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcCustomReportService(@Qualifier("statisticsDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	public Collection getTopTenSenders(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		String query = "SELECT COUNT(`"+RecordMapper.RECORD_SEQUENCE+"`) as `count`, COALESCE(`"+RecordMapper.FROM_RECIPIENTS+"`,`"+RecordMapper.SENDER_ID+"`) as `label` ";
		query = query + getFromToBaseQuery(from, to);
		query = query + " GROUP BY COALESCE(`"+RecordMapper.FROM_RECIPIENTS+"`,`"+RecordMapper.SENDER_ID+"`)";
		query = query + " ORDER BY 1 DESC LIMIT 10 ";
		return template.getJdbcOperations().queryForList(query, new Object[]{new Timestamp(since),new Timestamp(until)});
	}

	@Override
	public Collection getTopTenRecipients(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		String query = "SELECT COUNT(`"+RecordMapper.RECORD_SEQUENCE+"`) as `count`, `"+RecordMapper.RECIPIENT_ID+"` as 'label' ";
		query = query + getFromToBaseQuery(from, to);
		query = query + " GROUP BY `"+RecordMapper.RECIPIENT_ID+"`";
		query = query + " ORDER BY 1 DESC LIMIT 10 ";
		return template.getJdbcOperations().queryForList(query, new Object[]{new Timestamp(since),new Timestamp(until)});
	}

	@Override
	public Collection<RecordVO> getEmails(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, final long since, final long until) {
		String query = "SELECT * "+getFromToBaseQuery(from, to) +" ORDER BY `"+RecordMapper.INSERT_DATE+"` LIMIT "+PluginReportService.MAX_RESULT;
		return template.getJdbcOperations().query(query, new Object[]{new Timestamp(since),new Timestamp(until)},new RecordMapper());
	}
	
	private String getFromToBaseQuery(final FeatureRuleDirectionVO from, final FeatureRuleDirectionVO to) {

		String query = " FROM `"+RecordMapper.DATABASE+"`.`"+RecordMapper.TABLE_NAME+"`" +
				" WHERE `"+RecordMapper.INSERT_DATE+"` BETWEEN ? AND ?";
			
		//from condition
		if(from.getDirectionType().equals(ANYONEELSE)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` <> `"+RecordMapper.RECIPIENT_DOMAIN_ID+"`";
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			query = query + " AND `"+RecordMapper.FLOW+"` <> 'in'";
		}else if(from.getDirectionType().equals(DOMAIN)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` = '"+from.getFreeValue()+"'";
		}else if(from.getDirectionType().equals(GROUP)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` = '"+from.getDomain()+"' AND `"+RecordMapper.SENDER_GROUP+"` = '"+from.getGroup()+"' ";
		}else if(from.getDirectionType().equals(INDIVIDUAL)){
			query = query + " AND (`"+RecordMapper.SENDER_ID+"` = '"+from.getFreeValue()+"' OR `"+RecordMapper.FROM_RECIPIENTS+"` = '"+from.getFreeValue()+"')";
		}
		
		//to condition
		if(to.getDirectionType().equals(ANYONEELSE)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` <> `"+RecordMapper.RECIPIENT_DOMAIN_ID+"`";
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			query = query + " AND `"+RecordMapper.FLOW+"` <> 'out'";
		} else if(to.getDirectionType().equals(DOMAIN)){
			query = query + " AND `"+RecordMapper.RECIPIENT_DOMAIN_ID+"` = '"+to.getFreeValue()+"'";
		}else if(to.getDirectionType().equals(GROUP)){
			query = query + " AND `"+RecordMapper.RECIPIENT_DOMAIN_ID+"` = '"+to.getDomain()+"' AND `"+RecordMapper.RECIPIENT_GROUP+"` = '"+to.getGroup()+"' ";
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			query = query + " AND `"+RecordMapper.RECIPIENT_ID+"` = '"+to.getFreeValue()+"'";
		}
				
		return query;
	}

	@Override
	public List<RecordStatVO> getStats(Long insertDate, Long sequence) {
		String sql = " SELECT `"+RecordStatMapper.STAT_KEY+"`,`"+RecordStatMapper.STAT_VALUE+"` " +
				" FROM `"+RecordStatMapper.DATABASE+"`.`"+RecordStatMapper.TABLE_NAME+"`" +
				" WHERE `"+RecordStatMapper.RECORD_SEQUENCE+"` = :sequence" +
				" AND `"+RecordStatMapper.INSERT_DATE+"` = :insertDate ";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("insertDate", new Timestamp(insertDate));
		source.addValue("sequence", sequence);
		List<RecordStatVO> stats = template.query(sql, source, new RecordStatMapper());
		log.debug("insertDate="+ new Timestamp(insertDate).toString()+" sequence="+sequence);
		log.debug("stats="+stats.toString());
		return stats;
	}


}
