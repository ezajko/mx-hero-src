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

package org.mxhero.console.backend.repository.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.AccountPropertyMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountAliasMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountMapper;
import org.mxhero.console.backend.vo.AccountPropertyVO;
import org.mxhero.console.backend.vo.EmailAccountAliasVO;
import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcEmailAccountRepository extends BaseJdbcDao<EmailAccountVO> implements EmailAccountRepository{
	
	private static final String SELECT = "SELECT `"+EmailAccountMapper.ACCOUNT+"`, `"+EmailAccountMapper.CREATED+"`," +
			" `"+EmailAccountMapper.DATA_SOURCE+"`, `"+EmailAccountMapper.DOMAIN_ID+"`, `"+EmailAccountMapper.GROUP_NAME+"`," +
			" `"+EmailAccountMapper.UPDATED+"` " +
			" FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcEmailAccountRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String aliasSql = "DELETE FROM `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+EmailAccountAliasMapper.DOMAIN_ID+"` = :domainId";
		template.update(aliasSql, new MapSqlParameterSource("domainId",domainId));		
		
		String accountSql = "DELETE FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId";
		template.update(accountSql, new MapSqlParameterSource("domainId",domainId));
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(String account, String domainId) {
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("account", account);
		
		String aliasSql = "DELETE FROM `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+EmailAccountAliasMapper.DOMAIN_ID+"` = :domainId " +
				" AND  `"+EmailAccountAliasMapper.ACCOUNT+"` = :account ; ";
		template.update(aliasSql, source);		
		
		String accountSql = "DELETE FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId AND `"+EmailAccountMapper.ACCOUNT+"` = :account ;";
		template.update(accountSql, source);
	}

	@Override
	public PageResult<EmailAccountVO> findAll(
			String domainId, String account, String group, int pageNo, int pageSize) {
		String sql = SELECT;
		MapSqlParameterSource source = new MapSqlParameterSource();
		boolean hasCondition = false;
		if(domainId!=null && !domainId.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId ";
			source.addValue("domainId",domainId);
		}
		if(account!=null && !account.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+EmailAccountMapper.ACCOUNT+"` like :account";
			source.addValue("account", account.trim()+"%");
		}
		if(group!=null && !group.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+EmailAccountMapper.GROUP_NAME+"` like :group";
			source.addValue("group", "%"+group.trim()+"%");
		}
		
		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+EmailAccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+EmailAccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new EmailAccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<EmailAccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(EmailAccountVO accountResult : result.getPageData()){
				List<EmailAccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
				if(aliases!=null && aliases.size()>0){
					for(EmailAccountAliasVO alias : aliases){
						alias.setAccount(accountResult);
					}
				}
				accountResult.setAliases(aliases);
			}
		}
		return result;
	}

	private List<EmailAccountAliasVO> findAliases(String account, String domainId){
		String sql = "SELECT `"+EmailAccountAliasMapper.DATA_SOURCE+"`, `"+EmailAccountAliasMapper.DOMAIN_ALIAS+"`," +
				" `"+EmailAccountAliasMapper.ACCOUNT_ALIAS+"` FROM `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+EmailAccountAliasMapper.DOMAIN_ID+"` = :domainId " +
				" AND  `"+EmailAccountAliasMapper.ACCOUNT+"` = :account ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("account", account);
		return template.query(sql, source, new EmailAccountAliasMapper());
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insertAlias(EmailAccountAliasVO aliasVO){
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("account", aliasVO.getAccount().getAccount());
		source.addValue("alias", aliasVO.getName());
		source.addValue("created", Calendar.getInstance().getTime());
		source.addValue("dataSource", EmailAccountVO.MANUAL);
		source.addValue("domainAlias", aliasVO.getDomain());
		source.addValue("domainId", aliasVO.getAccount().getDomain());
		String sql = "INSERT INTO `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				" (`"+EmailAccountAliasMapper.ACCOUNT+"`,`"+EmailAccountAliasMapper.ACCOUNT_ALIAS+"`,`"+EmailAccountAliasMapper.CREATED+"`,`"+EmailAccountAliasMapper.DATA_SOURCE+"`,`"+EmailAccountAliasMapper.DOMAIN_ALIAS+"`,`"+EmailAccountAliasMapper.DOMAIN_ID+"`)" +
				" VALUES (:account,:alias,:created,:dataSource,:domainAlias,:domainId);";
		template.update(sql, source);
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteAlias(EmailAccountAliasVO aliasVO){
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",aliasVO.getDomain());
		source.addValue("account", aliasVO.getName());
		String sql = "DELETE FROM `"+EmailAccountAliasMapper.DATABASE+"`.`"+EmailAccountAliasMapper.TABLE_NAME+"`" +
				"  WHERE `"+EmailAccountAliasMapper.DOMAIN_ALIAS+"` = :domainId " +
				" AND  `"+EmailAccountAliasMapper.ACCOUNT_ALIAS+"` = :account ;";
		template.update(sql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(EmailAccountVO accountVO) {
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",accountVO.getDomain());
		source.addValue("account", accountVO.getAccount());
		source.addValue("updated", Calendar.getInstance().getTime());
		source.addValue("group", accountVO.getGroup());
		
		String sql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"` " +
				" SET `"+EmailAccountMapper.UPDATED+"` = :updated ," +
				" `"+EmailAccountMapper.GROUP_NAME+"` = :group " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId AND `"+EmailAccountMapper.ACCOUNT+"` = :account ;";
		
		template.update(sql, source);
		
		List<EmailAccountAliasVO> databaseAliases = findAliases(accountVO.getAccount(),accountVO.getDomain());
		
		if(databaseAliases!=null && databaseAliases.size()>0 && accountVO.getAliases()!=null){
			Set<EmailAccountAliasVO> toRemove = new HashSet<EmailAccountAliasVO>(databaseAliases);
			toRemove.removeAll(accountVO.getAliases());
			for(EmailAccountAliasVO removeAlias : toRemove){
				removeAlias.setAccount(accountVO);
				deleteAlias(removeAlias);
			}
		}
		if(accountVO.getAliases()!=null && accountVO.getAliases().size()>0){
			Set<EmailAccountAliasVO> toInsert = new HashSet<EmailAccountAliasVO>(accountVO.getAliases());
			toInsert.removeAll(databaseAliases);
			for(EmailAccountAliasVO addAlias : toInsert){
				addAlias.setAccount(accountVO);
				insertAlias(addAlias);
			}
		}
	}

	@Override
	public EmailAccountVO findById(String account, String domain) {
		String sql = SELECT + " WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId AND `"+EmailAccountMapper.ACCOUNT+"` = :account ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domain);
		source.addValue("account", account);
		List<EmailAccountVO> accounts = template.query(sql, source, new EmailAccountMapper());
		if(accounts!=null && accounts.size()>0){
			EmailAccountVO returnAccount = accounts.get(0);
			returnAccount.setAliases(findAliases(account, domain));
			return returnAccount;
		}
		return null;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(EmailAccountVO accountVO) {
		String accountSql = "INSERT INTO `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" (`"+EmailAccountMapper.ACCOUNT+"`, `"+EmailAccountMapper.CREATED+"`," +
				" `"+EmailAccountMapper.DATA_SOURCE+"`, `"+EmailAccountMapper.DOMAIN_ID+"`," +
				" `"+EmailAccountMapper.GROUP_NAME+"`, `"+EmailAccountMapper.UPDATED+"`)" +
				" VALUES(:account,:created,:dataSource,:domainId,:groupName,:updated);";
		MapSqlParameterSource accountSource = new MapSqlParameterSource("domainId",accountVO.getDomain());
		accountSource.addValue("account", accountVO.getAccount());
		accountSource.addValue("created", Calendar.getInstance().getTime());
		accountSource.addValue("dataSource", EmailAccountVO.MANUAL);
		accountSource.addValue("groupName", accountVO.getGroup());
		accountSource.addValue("updated", Calendar.getInstance().getTime());
		template.update(accountSql, accountSource);
		boolean hasPrincipal = false;
		if(accountVO.getAliases()!=null && accountVO.getAliases().size()>0){
			for(EmailAccountAliasVO alias : accountVO.getAliases()){
				if(alias.getName().equalsIgnoreCase(accountVO.getAccount()) && alias.getDomain().equals(accountVO.getDomain())){
					hasPrincipal=true;
				}
				alias.setAccount(accountVO);
				insertAlias(alias);
			}
		}
		if(!hasPrincipal){
			EmailAccountAliasVO alias = new EmailAccountAliasVO();
			alias.setAccount(accountVO);
			alias.setName(accountVO.getAccount());
			alias.setDomain(accountVO.getDomain());
			alias.setDataSource(EmailAccountVO.MANUAL);
			insertAlias(alias);
		}
	}
	
	@Override
	public PageResult<EmailAccountVO> findMembersByGroupId(String domainId, String groupName,  int pageNo, int pageSize){
		String sql = SELECT + " WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` = :groupName ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);
		source.addValue("groupName", groupName);
		
		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+EmailAccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+EmailAccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new EmailAccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<EmailAccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(EmailAccountVO accountResult : result.getPageData()){
				List<EmailAccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
				if(aliases!=null && aliases.size()>0){
					for(EmailAccountAliasVO alias : aliases){
						alias.setAccount(accountResult);
					}
				}
				accountResult.setAliases(aliases);
			}
		}
		return result;
	}

	@Override
	public PageResult<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId, int pageNo, int pageSize){
		String sql = SELECT + " WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` IS NULL ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);

		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+EmailAccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+EmailAccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new EmailAccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<EmailAccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(EmailAccountVO accountResult : result.getPageData()){
				List<EmailAccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
				if(aliases!=null && aliases.size()>0){
					for(EmailAccountAliasVO alias : aliases){
						alias.setAccount(accountResult);
					}
				}
				accountResult.setAliases(aliases);
			}
		}
		return result;
	}
	
	public void refreshProperties(String account, String domainId,
			List<AccountPropertyVO> properties) {
		String deleteSql = " DELETE FROM email_accounts_properties WHERE account=:account AND domain_id = :domainId ";
		MapSqlParameterSource deleteSource = new MapSqlParameterSource("account",account);
		deleteSource.addValue("domainId", domainId);
		template.update(deleteSql, deleteSource);
		String refreshSql = " INSERT INTO email_accounts_properties (account,domain_id,property_name,property_value) " +
				" VALUES (:account,:domainId,:propertyName,:propertyValue) ";
		if(properties!=null && properties.size()>0){
			for(AccountPropertyVO property : properties){
				MapSqlParameterSource insertSource = new MapSqlParameterSource("account",account);
				insertSource.addValue("domainId", domainId);
				insertSource.addValue("propertyName", property.getName().toLowerCase());
				insertSource.addValue("propertyValue", property.getValue());
				template.update(refreshSql, insertSource);
			}
		}
	}
	
	public List<AccountPropertyVO> readProperties(String account, String domainId) {
			String sql = " SELECT * FROM email_accounts_properties WHERE account=:account AND domain_id = :domainId ";
			MapSqlParameterSource source = new MapSqlParameterSource("account",account);
			source.addValue("domainId", domainId);
			return template.query(sql, source, new AccountPropertyMapper());
	}

	public void deleteProperties(String account, String domainId) {
			String deleteSql = " DELETE FROM email_accounts_properties WHERE account=:account AND domain_id = :domainId ";
			MapSqlParameterSource deleteSource = new MapSqlParameterSource("account",account);
			deleteSource.addValue("domainId", domainId);
			template.update(deleteSql, deleteSource);
	}
}
