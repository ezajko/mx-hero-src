package org.mxhero.engine.plugin.gsync.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.gsync.internal.domain.DomainAdLdap;
import org.mxhero.engine.plugin.gsync.internal.repository.DomainAdLdapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "jdbcRepository")
public class JDBCDomainAdLdapRepository implements DomainAdLdapRepository {
	
	public static final String SYNC_TYPE="gapps";
	private static final Logger log = LoggerFactory.getLogger(JDBCDomainAdLdapRepository.class);
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JDBCDomainAdLdapRepository(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	public DomainAdLdap findDomainAdLdap(String domainId){
		String sql = " SELECT da.domain, da.address, da.base, da.directory_type, " +
				" da.last_error, da.filter, da.last_update, da.next_update, da.override_flag, " +
				" da.password, da.port, da.ssl_flag, da.user, da.dn_authenticate, au.notify_email " +
				" FROM domain_adldap da LEFT OUTER JOIN app_users au ON da.domain = au.domain " +
				" WHERE da.domain = :domainId ";
		List<DomainAdLdap> domainsAdLdap = template.query(sql, new MapSqlParameterSource("domainId", domainId), new RowMapper<DomainAdLdap>() {
			
			@Override
			public DomainAdLdap mapRow(ResultSet rs, int rowNum) throws SQLException {
				DomainAdLdap domainAdLdap = new DomainAdLdap();
				domainAdLdap.setAddres(rs.getString("address"));
				domainAdLdap.setBase(rs.getString("base"));
				domainAdLdap.setDirectoryType(rs.getString("directory_type"));
				domainAdLdap.setDomainId(rs.getString("domain"));
				domainAdLdap.setError(rs.getString("last_error"));
				domainAdLdap.setFilter(rs.getString("filter"));
				domainAdLdap.setLastUpdate(dateToCalendar(rs.getTimestamp("last_update")));
				domainAdLdap.setNextUpdate(dateToCalendar(rs.getTimestamp("next_update")));
				domainAdLdap.setOverrideFlag(rs.getBoolean("override_flag"));
				domainAdLdap.setPassword(rs.getString("password"));
				domainAdLdap.setPort(rs.getString("port"));
				domainAdLdap.setSslFlag(rs.getBoolean("ssl_flag"));
				domainAdLdap.setUser(rs.getString("user"));		
				domainAdLdap.setNotifyEmail(rs.getString("notify_email"));
				return domainAdLdap;
			}
		});
		if(domainsAdLdap!=null && domainsAdLdap.size()>0){
			return domainsAdLdap.get(0);
		}
		return null;
	}
	
	public List<String> findDomainAliases(String domainId){
		String sql = " SELECT alias FROM domains_aliases WHERE domain = :domainId";
		return template.queryForList(sql, new MapSqlParameterSource("domainId", domainId),String.class);
	}
	
	public List<String> findDomainsToSync(){
		String sql = " SELECT domain " +
				" FROM domain_adldap " +
				" WHERE next_update < now() " +
				" AND directory_type = '"+SYNC_TYPE+"'";
		return template.getJdbcOperations().queryForList(sql, String.class);
	}

	public void updateNextAdLdapCheck(String domainId){
		String sql = " UPDATE domain_adldap " +
				" SET last_update = now() , last_error = null, " +
				" next_update = DATE_ADD(now(), interval 1 hour) " +
				" WHERE domain = :domainId ";
		template.update(sql, new MapSqlParameterSource("domainId", domainId));
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void updateErrorAdLdapCheck(String domainId, String lastError){
		String sql = " UPDATE domain_adldap " +
				" SET last_update = now() , last_error = null, " +
				" next_update = DATE_ADD(now(), interval 1 hour), " +
				" last_error = :lastError " +
				" WHERE domain = :domainId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("domainId", domainId);
		params.put("lastError", lastError);
		template.update(sql, params);
	}

	public void deleteAccount(String account, String domainId){

		String rulesSql = " SELECT id FROM  features_rules " +
				" WHERE EXISTS (SELECT rule_id " +
							" FROM features_rules_directions b " +
							" WHERE b.rule_id = features_rules.id " +
							" AND b.domain = :domainId " +
							" AND b.account = :account ) ";
		Map<String, Object> rulesParams = new HashMap<String, Object>();
		rulesParams.put("domainId", domainId);
		rulesParams.put("account", account);
		List<Long> rulesId = template.queryForList(rulesSql, rulesParams,Long.class);
		if(rulesId!=null){
			for(Long ruleId : rulesId){
				template.getJdbcOperations().update("DELETE FROM features_rules_properties WHERE rule_id = ?"
						,new Object[]{ruleId});
				template.getJdbcOperations().update("DELETE FROM features_rules_directions WHERE rule_id = ?"
						,new Object[]{ruleId});
				template.getJdbcOperations().update("DELETE FROM features_rules WHERE id = ?"
						,new Object[]{ruleId});
			}
		}

		String aliasesSql = " DELETE FROM account_aliases " +
				" WHERE domain_id = :domainId " +
				" AND account = :account ";
		Map<String, Object> aliasesParams = new HashMap<String, Object>();
		aliasesParams.put("domainId", domainId);
		aliasesParams.put("account", account);
		template.update(aliasesSql, aliasesParams);
		
		String accountSql = " DELETE FROM email_accounts " +
				" WHERE domain_id = :domainId " +
				" AND account = :account ";
		Map<String, Object> accountParams = new HashMap<String, Object>();
		accountParams.put("domainId", domainId);
		accountParams.put("account", account);
		template.update(accountSql, accountParams);
		
	}
	
	@Transactional(readOnly=true)
	public List<String> getManagedAccounts(String domainId){
		String sql = " SELECT account " +
				" FROM email_accounts " +
				" WHERE domain_id = :domainId " +
				" AND data_source = '"+SYNC_TYPE+"' ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		return template.queryForList(sql, paramMap, String.class);
	}
	
	public List<String> getNotManagedAccounts(String domainId){
		String sql = " SELECT account " +
				" FROM email_accounts " +
				" WHERE domain_id = :domainId " +
				" AND data_source <> '"+SYNC_TYPE+"' ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		return template.queryForList(sql, paramMap, String.class);
	}
	
	public void updateAliasesAccount(String account, String domainId, List<String> aliases){
		String sql = " DELETE FROM account_aliases " +
				" WHERE account = :account " +
				" AND domain_id = :domainId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		paramMap.put("account", account);
		template.update(sql, paramMap);
		String aliasSql = " INSERT INTO account_aliases (account_alias,domain_alias,created,data_source,account,domain_id) " +
						" VALUES (?,?,NOW(),?,?,?) " +
						" ON DUPLICATE KEY UPDATE " +
						" created=VALUES(created), data_source=VALUES(data_source), account=VALUES(account), domain_id=VALUES(domain_id)";
		for(String mail : aliases){
			String accountName = mail.split("@")[0].trim();
			String domainName = mail.split("@")[1].trim();
			template.getJdbcOperations().update(aliasSql, new Object[]{accountName,domainName,SYNC_TYPE,account,domainId});
		}
		
		String updateAccount = " UPDATE email_accounts " +
							" SET updated = NOW()," +
							" data_source = '"+SYNC_TYPE+"' " +
							" WHERE account = :account " +
							" AND domain_id = :domainId";
		Map<String, Object> accountParamMap = new HashMap<String, Object>();
		accountParamMap.put("domainId", domainId);
		accountParamMap.put("account", account);
		template.update(updateAccount, accountParamMap);
	}
	
	public void insertAccount(String account, String domainId, List<String> aliases){
		String sql = " INSERT INTO email_accounts (account, domain_id, created, data_source, updated, group_name) " +
				" VALUES (?,?,NOW(),?,NOW(),null) ";
		
		template.getJdbcOperations().update(sql, new Object[]{account, domainId, SYNC_TYPE});
		String aliasSql = " INSERT INTO account_aliases (account_alias,domain_alias,created,data_source,account,domain_id) " +
						" VALUES (?,?,NOW(),?,?,?) " +
						" ON DUPLICATE KEY UPDATE " +
						" created=VALUES(created), data_source=VALUES(data_source), account=VALUES(account), domain_id=VALUES(domain_id)";
		for(String mail : aliases){
			String accountName = mail.split("@")[0].trim();
			String domainName = mail.split("@")[1].trim();
			try{
				template.getJdbcOperations().update(aliasSql, new Object[]{accountName,domainName,SYNC_TYPE,account,domainId});
			}catch(DataIntegrityViolationException e){
				log.warn("while inserting aliases "+Arrays.deepToString(aliases.toArray()));
				log.warn("duplicate alias for accountName:"+accountName+" domainName:"+domainName+" account:"+account+" domainId: "+domainId);
			}
		}
	}
	
	
	private Calendar dateToCalendar(Date date){
		if(date==null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
}
