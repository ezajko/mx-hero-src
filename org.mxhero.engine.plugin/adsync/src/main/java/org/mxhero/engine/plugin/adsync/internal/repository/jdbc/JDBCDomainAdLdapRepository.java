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

package org.mxhero.engine.plugin.adsync.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.mxhero.engine.plugin.adsync.internal.domain.DomainAdLdap;
import org.mxhero.engine.plugin.adsync.internal.repository.DomainAdLdapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "jdbcRepository")
public class JDBCDomainAdLdapRepository implements DomainAdLdapRepository {
	
	public static final String SYNC_TYPE="adladp";
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
				" FROM mxhero.domain_adldap da LEFT OUTER JOIN mxhero.app_users au ON da.domain = au.domain " +
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
			DomainAdLdap domainAdLdap = domainsAdLdap.get(0);
			String propertiesSql = " SELECT property_name, property_key FROM mxhero.domain_adldap_properties WHERE domain = :domainId"; 
			domainAdLdap.setAccountProperties(template.query(propertiesSql,  new MapSqlParameterSource("domainId", domainId), new ResultSetExtractor<Map<String,String>>() {

				@Override
				public Map<String,String> extractData(ResultSet rs) throws SQLException,
						DataAccessException {
					Map<String,String> properties = new HashMap<String, String>();
					while(rs.next()){
						properties.put(rs.getString("property_name"), rs.getString("property_key"));
					}
					return properties;
				}
			}));
			
			return domainAdLdap;
		}
		return null;
	}
	
	public List<String> findDomainsToSync(){
		String sql = " SELECT domain " +
				" FROM mxhero.domain_adldap " +
				" WHERE next_update < now() " +
				" AND directory_type IN ('zimbra','exchange','ldap')";
		return template.getJdbcOperations().queryForList(sql, String.class);
	}

	public void updateNextAdLdapCheck(String domainId){
		String sql = " UPDATE mxhero.domain_adldap " +
				" SET last_update = now() , last_error = null, " +
				" next_update = DATE_ADD(now(), interval 1 hour) " +
				" WHERE domain = :domainId ";
		template.update(sql, new MapSqlParameterSource("domainId", domainId));
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void updateErrorAdLdapCheck(String domainId, String lastError){
		String sql = " UPDATE mxhero.domain_adldap " +
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

		String rulesSql = " SELECT id FROM  mxhero.features_rules " +
				" WHERE EXISTS (SELECT rule_id " +
							" FROM mxhero.features_rules_directions b " +
							" WHERE b.rule_id = features_rules.id " +
							" AND b.domain = :domainId " +
							" AND b.account = :account ) ";
		Map<String, Object> rulesParams = new HashMap<String, Object>();
		rulesParams.put("domainId", domainId);
		rulesParams.put("account", account);
		List<Long> rulesId = template.queryForList(rulesSql, rulesParams,Long.class);
		if(rulesId!=null){
			for(Long ruleId : rulesId){
				template.getJdbcOperations().update("DELETE FROM mxhero.features_rules_properties WHERE rule_id = ?"
						,new Object[]{ruleId});
				template.getJdbcOperations().update("DELETE FROM mxhero.features_rules_directions WHERE rule_id = ?"
						,new Object[]{ruleId});
				template.getJdbcOperations().update("DELETE FROM mxhero.features_rules WHERE id = ?"
						,new Object[]{ruleId});
			}
		}

		String aliasesSql = " DELETE FROM mxhero.account_aliases " +
				" WHERE domain_id = :domainId " +
				" AND account = :account ";
		Map<String, Object> aliasesParams = new HashMap<String, Object>();
		aliasesParams.put("domainId", domainId);
		aliasesParams.put("account", account);
		template.update(aliasesSql, aliasesParams);
		
		String accountSql = " DELETE FROM mxhero.email_accounts " +
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
				" FROM mxhero.email_accounts " +
				" WHERE domain_id = :domainId " +
				" AND data_source = '"+SYNC_TYPE+"' ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		return template.queryForList(sql, paramMap, String.class);
	}
	
	public List<String> getNotManagedAccounts(String domainId){
		String sql = " SELECT account " +
				" FROM mxhero.email_accounts " +
				" WHERE domain_id = :domainId " +
				" AND data_source <> '"+SYNC_TYPE+"' ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		return template.queryForList(sql, paramMap, String.class);
	}
	
	public void updateAliasesAccount(String account, String domainId, List<String> aliases){
		String sql = " DELETE FROM mxhero.account_aliases " +
				" WHERE account = :account " +
				" AND domain_id = :domainId ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("domainId", domainId);
		paramMap.put("account", account);
		template.update(sql, paramMap);
		String aliasSql = " INSERT INTO mxhero.account_aliases (account_alias,domain_alias,created,data_source,account,domain_id) " +
						" VALUES (?,?,NOW(),?,?,?) " +
						" ON DUPLICATE KEY UPDATE " +
						" created=VALUES(created), data_source=VALUES(data_source), account=VALUES(account), domain_id=VALUES(domain_id)";
		for(String mail : aliases){
			String accountName = mail.split("@")[0].trim();
			String domainName = mail.split("@")[1].trim();
			try{
				template.getJdbcOperations().update(aliasSql, new Object[]{accountName,domainName,SYNC_TYPE,account,domainId});
			}catch(Exception e){
				log.warn("error alias for accountName:"+accountName+" domainName:"+domainName+" account:"+account+" domainId: "+domainId);
			}
		}
		
		String updateAccount = " UPDATE mxhero.email_accounts " +
							" SET updated = NOW()," +
							" data_source = '"+SYNC_TYPE+"' " +
							" WHERE account = :account " +
							" AND domain_id = :domainId";
		Map<String, Object> accountParamMap = new HashMap<String, Object>();
		accountParamMap.put("domainId", domainId);
		accountParamMap.put("account", account);
		template.update(updateAccount, accountParamMap);
	}
	
	
	public List<String> findDomainAliases(String domainId){
		return template.getJdbcOperations().queryForList("SELECT alias FROM mxhero.domains_aliases WHERE domain = ?",new Object[]{domainId}, String.class);	
	}
	
	public void insertAccount(String account, String domainId, List<String> aliases){
		String sql = " INSERT INTO mxhero.email_accounts (account, domain_id, created, data_source, updated, group_name) " +
				" VALUES (?,?,NOW(),?,NOW(),null) ";
		
		try{
			template.getJdbcOperations().update(sql, new Object[]{account, domainId, SYNC_TYPE});
		}catch(DataIntegrityViolationException e){
			log.warn("duplicate account:"+account+" domain:"+domainId);
		}
		
		String aliasSql = " INSERT INTO mxhero.account_aliases (account_alias,domain_alias,created,data_source,account,domain_id) " +
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

	@Override
	public void refreshProperties(String account, String domainId,
			Map<String, String> properties) {
		try{
			String deleteSql = " DELETE FROM mxhero.email_accounts_properties WHERE account=:account AND domain_id = :domainId ";
			MapSqlParameterSource deleteSource = new MapSqlParameterSource("account",account);
			deleteSource.addValue("domainId", domainId);
			template.update(deleteSql, deleteSource);
			String refreshSql = " INSERT INTO mxhero.email_accounts_properties (account,domain_id,property_name,property_value) " +
					" VALUES (:account,:domainId,:propertyName,:propertyValue) ";
			if(properties!=null && properties.size()>0){
				for(Entry<String, String> entry : properties.entrySet()){
					MapSqlParameterSource insertSource = new MapSqlParameterSource("account",account);
					insertSource.addValue("domainId", domainId);
					insertSource.addValue("propertyName", entry.getKey().toLowerCase());
					insertSource.addValue("propertyValue", entry.getValue());
					template.update(refreshSql, insertSource);
				}
			}
		}catch(Exception e){
			log.warn("error while sync of account properties:",e);
		}
	}
	
}
