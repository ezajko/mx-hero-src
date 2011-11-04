package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcUserRepository")
@Transactional(readOnly=true)
public class JdbcUserRepository implements UserRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcUserRepository(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	
	public Map<String, Domain> getDomains(){
		String domainSql = "SELECT domain, alias FROM domains_aliases";
		Map<String, Domain> domainMap = new HashMap<String, Domain>();
		List<Map<String, Object>> domainsResult = template.getJdbcOperations().queryForList(domainSql);
		if(domainsResult!=null && domainsResult.size()>0){
			//map domains
			for(Map<String, Object> domainRow:domainsResult){
				Domain domain = null;
				if(domainMap.containsKey(domainRow.get("domain"))){
					domain = domainMap.get(domainRow.get("domain"));
				}else{
					domain = new Domain();
					domain.setId(domainRow.get("domain").toString());
					domain.setManaged(true);
					domain.setAliases(new HashSet<String>());
					domainMap.put(domain.getId(), domain);
				}
				domain.getAliases().add(domainRow.get("alias").toString());
			}
		}
		return domainMap;
	}
	
	private Map<String, User> getUsers(Map<String, Domain> domainMap){
		Map<String, User> aliasesMap = new HashMap<String, User>();	
		if(domainMap.size()>0){
			String accountsQuery = "SELECT aa.account, aa.domain_id, aa.account_alias, aa.domain_alias, ea.group_name " +
					" FROM email_accounts ea INNER JOIN account_aliases aa " +
					" ON aa.domain_id = ea.domain_id AND aa.account = ea.account" +
					" WHERE aa.domain_id = :domainId";
			for(String domainKey : domainMap.keySet()){
				Domain domain = domainMap.get(domainKey);
				Map<String, User> usersMap = new HashMap<String, User>();	
				List<Map<String, Object>> accountsResult = template.queryForList(accountsQuery, new MapSqlParameterSource("domainId", domainKey));
				//map accounts
				if(accountsResult!=null & accountsResult.size()>0){
					for(Map<String, Object> accountAlias : accountsResult){
						User user = null;
						if(usersMap.containsKey(accountAlias.get("account"))){
							user = usersMap.get(accountAlias.get("account"));
						}else{
							user = new User();
							user.setManaged(true);
							user.setAliases(new HashSet<String>());
							user.setDomain(domain);
							user.setMail(accountAlias.get("account").toString()+"@"+accountAlias.get("domain_id").toString());
							user.setGroup((accountAlias.get("group_name")==null)?null:accountAlias.get("group_name").toString());
							usersMap.put(accountAlias.get("account").toString(), user);
						}
						user.getAliases().add(accountAlias.get("account_alias").toString()+"@"+accountAlias.get("domain_alias").toString());
					}
				}
				//now load the aliasesMap
				for(Entry<String, User> entry : usersMap.entrySet()){
					for(String alias : entry.getValue().getAliases()){
						aliasesMap.put(alias, entry.getValue());
					}
				}
			}	
		}
		return aliasesMap;
	}

	@Override
	public Map<String, User> getUsers() {
		return this.getUsers(getDomains());
	}
	
}
