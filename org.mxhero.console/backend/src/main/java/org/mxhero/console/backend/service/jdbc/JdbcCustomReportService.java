package org.mxhero.console.backend.service.jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.RecordMapper;
import org.mxhero.console.backend.service.CustomReportService;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.mxhero.console.backend.vo.RecordVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service("customReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JdbcCustomReportService implements CustomReportService{

	private static final String INDIVIDUAL="individual";
	private static final String GROUP="group";
	private static final String DOMAIN="domain";	
	private static final String ALLDOMAINS="alldomains";
	private static final String ANYONEELSE="anyoneelse";
	
	private NamedParameterJdbcTemplate template;
	private EmailAccountRepository accountRepository;
	
	@Autowired
	public JdbcCustomReportService(@Qualifier("statisticsDataSource")DataSource ds,
			EmailAccountRepository accountRepository) {
		this.template = new NamedParameterJdbcTemplate(ds);
		this.accountRepository = accountRepository;
	}

	@Override
	public Collection getTopTenSenders(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {

		String query = "SELECT COUNT(`"+RecordMapper.RECORD_SEQUENCE+"`), COALESCE(`"+RecordMapper.FROM_RECIPIENTS+"`,`"+RecordMapper.SENDER_ID+"`)";
		query = query + getFromToBaseQuery(from, to);
		query = query + " GROUP BY COALESCE(`"+RecordMapper.FROM_RECIPIENTS+"`,`"+RecordMapper.SENDER_ID+"`)";
		query = query + " ORDER BY 1 DESC";
		return template.getJdbcOperations().queryForList(query, new Object[]{new Timestamp(since),new Timestamp(until)});

	}

	@Override
	public Collection getTopTenRecipients(FeatureRuleDirectionVO from,
			FeatureRuleDirectionVO to, long since, long until) {
		
		String query = "SELECT COUNT(`"+RecordMapper.RECORD_SEQUENCE+"`), `"+RecordMapper.RECIPIENT_ID+"`";
		query = query + getFromToBaseQuery(from, to);
		query = query + " GROUP BY `"+RecordMapper.RECIPIENT_ID+"`";
		query = query + " ORDER BY 1 DESC";
		return template.getJdbcOperations().queryForList(query, new Object[]{new Timestamp(since),new Timestamp(until)});

	}

	@Override
	public Collection<RecordVO> getEmails(final FeatureRuleDirectionVO from,
			final FeatureRuleDirectionVO to, final long since, final long until) {
		
		String query = "SELECT *"+getFromToBaseQuery(from, to) +" ORDER BY `"+RecordMapper.INSERT_DATE+"`";
		return template.getJdbcOperations().query(query, new Object[]{new Timestamp(since),new Timestamp(until)},new RecordMapper());
	}
	
	private String getFromToBaseQuery(final FeatureRuleDirectionVO from, final FeatureRuleDirectionVO to) {

		String query = " FROM `"+RecordMapper.DATABASE+"`.`"+RecordMapper.TABLE_NAME+"`" +
				" WHERE `"+RecordMapper.INSERT_DATE+"` BETWEEN ? AND ?";
		
		Collection<EmailAccountVO> groupFrom = (from.getDirectionType().equals(GROUP))?accountRepository.findMembersByGroupId(from.getDomain(), from.getGroup()):null;
		Collection<EmailAccountVO> groupTo = (to.getDirectionType().equals(GROUP))?accountRepository.findMembersByGroupId(to.getDomain(), to.getGroup()):null;

		final Collection<String> groupFromEmails = new ArrayList<String>();
		final Collection<String> groupToEmails = new ArrayList<String>();
		
		if(groupFrom!=null && groupFrom.size()>0){
			for(EmailAccountVO email : groupFrom){
				groupFromEmails.add("'"+email.getAccount()+"@"+email.getDomain()+"'");
			}
		}
		if(groupTo!=null && groupTo.size()>0){
			for(EmailAccountVO email : groupTo){
				groupToEmails.add("'"+email.getAccount()+"@"+email.getDomain()+"'");
			}
		}
			
		//from condition
		if(from.getDirectionType().equals(ANYONEELSE)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` <> `"+RecordMapper.RECIPIENT_DOMAIN_ID+"`";
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			query = query + " AND `"+RecordMapper.FLOW+"` <> 'in'";
		}else if(from.getDirectionType().equals(DOMAIN)){
			query = query + " AND `"+RecordMapper.SENDER_DOMAIN_ID+"` = '"+from.getFreeValue()+"'";
		}else if(from.getDirectionType().equals(GROUP)){
			if(groupFromEmails.size()>0){
				query = query + " AND `"+RecordMapper.SENDER_ID+"` IN("+groupFromEmails.toString().substring(1,groupFromEmails.toString().length()-1)+")";
			}else{
				//if got here, query should had an empty value, is asking for people in a group that is empty
				query = query + " AND true = false";
			}
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
			if(groupToEmails.size()>0){
				query = query + " AND `"+RecordMapper.RECIPIENT_ID+"` IN("+groupToEmails.toString().substring(1,groupToEmails.toString().length()-1)+")";
			}else{
				//if got here, query should had an empty value, is asking for people in a group that is empty
				query = query + " AND true = false";
			}
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			query = query + " AND `"+RecordMapper.RECIPIENT_ID+"` = '"+to.getFreeValue()+"'";
		}
				
		return query;
	}


}
