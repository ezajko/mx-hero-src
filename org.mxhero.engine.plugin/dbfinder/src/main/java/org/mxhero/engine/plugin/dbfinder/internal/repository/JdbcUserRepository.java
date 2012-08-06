package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mmarmol
 *
 */
@Repository("jdbcUserRepository")
public class JdbcUserRepository implements UserRepository{

	private static Logger log = LoggerFactory.getLogger(JdbcUserRepository.class);
	private NamedParameterJdbcTemplate template;
	
	/**
	 * @param ds
	 */
	@Autowired
	public JdbcUserRepository(DataSource ds){
		this.template = new NamedParameterJdbcTemplate(ds);
	}
	
	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.dbfinder.internal.repository.UserRepository#getDomains()
	 */
	@Transactional(readOnly=true)
	public Map<String, Domain> getDomains(){
		String domainSql = "SELECT domain, alias FROM mxhero.domains_aliases";
		Map<String, Domain> domainMap = new HashMap<String, Domain>();
		List<Map<String, Object>> domainsResult = template.getJdbcOperations().queryForList(domainSql);
		if(domainsResult!=null && domainsResult.size()>0){
			//map domains
			for(Map<String, Object> domainRow:domainsResult){
				Domain domain = null;
				if(domainMap.containsKey(domainRow.get("domain").toString().toLowerCase())){
					domain = domainMap.get(domainRow.get("domain"));
				}else{
					domain = new Domain();
					domain.setId(domainRow.get("domain").toString().toLowerCase());
					domain.setManaged(true);
					domain.setAliases(new HashSet<String>());
					domainMap.put(domain.getId(), domain);
				}
				domain.getAliases().add(domainRow.get("alias").toString().toLowerCase());
				domainMap.put(domainRow.get("alias").toString(), domain);
			}
		}
		return domainMap;
	}
	
	/**
	 * @param domainMap
	 * @return
	 */
	@Transactional(readOnly=true)
	private Map<String, User> getUsers(Map<String, Domain> domainMap){
		Map<String, User> aliasesMap = new HashMap<String, User>();	
		if(domainMap.size()>0){
			String accountsQuery = "SELECT aa.account, aa.domain_id, aa.account_alias, aa.domain_alias, ea.group_name " +
					" FROM mxhero.email_accounts ea INNER JOIN mxhero.account_aliases aa " +
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
						if(usersMap.containsKey(accountAlias.get("account").toString().toLowerCase())){
							user = usersMap.get(accountAlias.get("account").toString().toLowerCase());
						}else{
							user = new User();
							user.setManaged(true);
							user.setAliases(new HashSet<String>());
							user.setDomain(domain);
							user.setMail(accountAlias.get("account").toString().toLowerCase()+"@"+accountAlias.get("domain_id").toString().toLowerCase());
							user.setGroup((accountAlias.get("group_name")==null)?null:accountAlias.get("group_name").toString().toLowerCase());
							user.setProperties(new HashMap<String, String>());
							usersMap.put(accountAlias.get("account").toString(), user);
						}
						user.getAliases().add(accountAlias.get("account_alias").toString().toLowerCase()+"@"+accountAlias.get("domain_alias").toString().toLowerCase());
					}
				}
				//now load the aliasesMap
				for(Entry<String, User> entry : usersMap.entrySet()){
					for(String alias : entry.getValue().getAliases()){
						aliasesMap.put(alias, entry.getValue());
					}
				}
				//load user properties for the domain
				String accountProperties = "SELECT concat(account,'@',domain_id) as email, property_name, property_value FROM mxhero.email_accounts_properties WHERE domain_id = :domainId";
				List<Map<String, Object>> accountsPropertiesResult = template.queryForList(accountProperties, new MapSqlParameterSource("domainId", domainKey));
				log.debug("found X properties:"+accountsPropertiesResult.size()+" for domain:"+domainKey);
				if(accountsPropertiesResult!=null && accountsPropertiesResult.size()>0){
					log.debug("adding properties");
					for(Map<String, Object> accountProperty : accountsPropertiesResult){
						if(aliasesMap.containsKey(accountProperty.get("email"))){
							if(aliasesMap.get(accountProperty.get("email")).getProperties()==null){
								aliasesMap.get(accountProperty.get("email")).setProperties(new HashMap<String, String>());
							}
							log.debug("adding for email:"+accountProperty.get("email")+" key:"+accountProperty.get("property_name")+" value:"+accountProperty.get("property_value"));
							aliasesMap.get(accountProperty.get("email")).getProperties().put(accountProperty.get("property_name").toString(), accountProperty.get("property_value").toString());
						}
					}
				}
			}	
			
			
		}
		return aliasesMap;
	}

	/* (non-Javadoc)
	 * @see org.mxhero.engine.plugin.dbfinder.internal.repository.UserRepository#getUsers()
	 */
	@Override
	public Map<String, User> getUsers() {
		return this.getUsers(getDomains());
	}
	
}
