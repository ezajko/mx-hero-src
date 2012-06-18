package org.mxhero.webapi.repository.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.webapi.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.webapi.repository.AccountRepository;
import org.mxhero.webapi.repository.jdbc.mapper.AccountAliasMapper;
import org.mxhero.webapi.repository.jdbc.mapper.AccountMapper;
import org.mxhero.webapi.repository.jdbc.mapper.AccountPropertyMapper;
import org.mxhero.webapi.vo.AccountAliasVO;
import org.mxhero.webapi.vo.AccountPropertyVO;
import org.mxhero.webapi.vo.AccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcAccountRepository extends BaseJdbcDao<AccountVO> implements AccountRepository{
	
	private static final String SELECT = "SELECT `"+AccountMapper.ACCOUNT+"`, `"+AccountMapper.CREATED+"`," +
			" `"+AccountMapper.DATA_SOURCE+"`, `"+AccountMapper.DOMAIN_ID+"`, `"+AccountMapper.GROUP_NAME+"`," +
			" `"+AccountMapper.UPDATED+"` " +
			" FROM `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcAccountRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String aliasSql = "DELETE FROM `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountAliasMapper.DOMAIN_ID+"` = :domainId";
		template.update(aliasSql, new MapSqlParameterSource("domainId",domainId));		
		
		String accountSql = "DELETE FROM `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId";
		template.update(accountSql, new MapSqlParameterSource("domainId",domainId));
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(String account, String domainId) {
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("account", account);
		
		String aliasSql = "DELETE FROM `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountAliasMapper.DOMAIN_ID+"` = :domainId " +
				" AND  `"+AccountAliasMapper.ACCOUNT+"` = :account ; ";
		template.update(aliasSql, source);		
		
		String accountSql = "DELETE FROM `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId AND `"+AccountMapper.ACCOUNT+"` = :account ;";
		template.update(accountSql, source);
	}

	@Override
	public PageResult<AccountVO> findAll(
			String domainId, String account, String group, Integer pageNo, Integer pageSize) {
		String sql = SELECT;
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		MapSqlParameterSource source = new MapSqlParameterSource();
		boolean hasCondition = false;
		if(domainId!=null && !domainId.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+AccountMapper.DOMAIN_ID+"` = :domainId ";
			source.addValue("domainId",domainId);
		}
		if(account!=null && !account.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+AccountMapper.ACCOUNT+"` like :account";
			source.addValue("account", account.trim()+"%");
		}
		if(group!=null && !group.trim().isEmpty()){
			if(hasCondition){
				sql = sql + " AND ";
			}else{
				sql = sql + " WHERE ";
				hasCondition = true;
			}
			sql = sql + " `"+AccountMapper.GROUP_NAME+"` like :group";
			source.addValue("group", "%"+group.trim()+"%");
		}
		
		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+AccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+AccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new AccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<AccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(AccountVO accountResult : result.getPageData()){
				List<AccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
				accountResult.setAliases(aliases);
			}
		}
		return result;
	}

	private List<AccountAliasVO> findAliases(String account, String domainId){
		String sql = "SELECT `"+AccountAliasMapper.DATA_SOURCE+"`, `"+AccountAliasMapper.DOMAIN_ALIAS+"`," +
				" `"+AccountAliasMapper.ACCOUNT_ALIAS+"` FROM `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountAliasMapper.DOMAIN_ID+"` = :domainId " +
				" AND  `"+AccountAliasMapper.ACCOUNT+"` = :account ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("account", account);
		return template.query(sql, source, new AccountAliasMapper());
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insertAlias(String domain, String account, AccountAliasVO aliasVO){
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("account", account);
		source.addValue("alias", aliasVO.getName());
		source.addValue("created", Calendar.getInstance().getTime());
		source.addValue("dataSource", AccountVO.MANUAL);
		source.addValue("domainAlias", aliasVO.getDomain());
		source.addValue("domainId", domain);
		String sql = "INSERT INTO `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				" (`"+AccountAliasMapper.ACCOUNT+"`,`"+AccountAliasMapper.ACCOUNT_ALIAS+"`,`"+AccountAliasMapper.CREATED+"`,`"+AccountAliasMapper.DATA_SOURCE+"`,`"+AccountAliasMapper.DOMAIN_ALIAS+"`,`"+AccountAliasMapper.DOMAIN_ID+"`)" +
				" VALUES (:account,:alias,:created,:dataSource,:domainAlias,:domainId);";
		template.update(sql, source);
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteAlias(AccountAliasVO aliasVO){
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",aliasVO.getDomain());
		source.addValue("account", aliasVO.getName());
		String sql = "DELETE FROM `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				"  WHERE `"+AccountAliasMapper.DOMAIN_ALIAS+"` = :domainId " +
				" AND  `"+AccountAliasMapper.ACCOUNT_ALIAS+"` = :account ;";
		template.update(sql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(AccountVO accountVO) {
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",accountVO.getDomain());
		source.addValue("account", accountVO.getAccount());
		source.addValue("updated", Calendar.getInstance().getTime());
		source.addValue("group", accountVO.getGroup());
		
		String sql = "UPDATE `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"` " +
				" SET `"+AccountMapper.UPDATED+"` = :updated ," +
				" `"+AccountMapper.GROUP_NAME+"` = :group " +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId AND `"+AccountMapper.ACCOUNT+"` = :account ;";
		
		template.update(sql, source);
		
		List<AccountAliasVO> databaseAliases = findAliases(accountVO.getAccount(),accountVO.getDomain());
		
		if(databaseAliases!=null && databaseAliases.size()>0 && accountVO.getAliases()!=null){
			Set<AccountAliasVO> toRemove = new HashSet<AccountAliasVO>(databaseAliases);
			toRemove.removeAll(accountVO.getAliases());
			for(AccountAliasVO removeAlias : toRemove){
				deleteAlias(removeAlias);
			}
		}
		if(accountVO.getAliases()!=null && accountVO.getAliases().size()>0){
			Set<AccountAliasVO> toInsert = new HashSet<AccountAliasVO>(accountVO.getAliases());
			toInsert.removeAll(databaseAliases);
			for(AccountAliasVO addAlias : toInsert){
				insertAlias(accountVO.getDomain(),accountVO.getAccount(),addAlias);
			}
		}
	}

	@Override
	public AccountVO findById(String account, String domain) {
		String sql = SELECT + " WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId AND `"+AccountMapper.ACCOUNT+"` = :account ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domain);
		source.addValue("account", account);
		List<AccountVO> accounts = template.query(sql, source, new AccountMapper());
		if(accounts!=null && accounts.size()>0){
			AccountVO returnAccount = accounts.get(0);
			returnAccount.setAliases(findAliases(account, domain));
			return returnAccount;
		}
		return null;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(AccountVO accountVO) {
		String accountSql = "INSERT INTO `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" (`"+AccountMapper.ACCOUNT+"`, `"+AccountMapper.CREATED+"`," +
				" `"+AccountMapper.DATA_SOURCE+"`, `"+AccountMapper.DOMAIN_ID+"`," +
				" `"+AccountMapper.GROUP_NAME+"`, `"+AccountMapper.UPDATED+"`)" +
				" VALUES(:account,:created,:dataSource,:domainId,:groupName,:updated);";
		MapSqlParameterSource accountSource = new MapSqlParameterSource("domainId",accountVO.getDomain());
		accountSource.addValue("account", accountVO.getAccount());
		accountSource.addValue("created", Calendar.getInstance().getTime());
		accountSource.addValue("dataSource", AccountVO.MANUAL);
		accountSource.addValue("groupName", accountVO.getGroup());
		accountSource.addValue("updated", Calendar.getInstance().getTime());
		template.update(accountSql, accountSource);
		boolean hasPrincipal = false;
		if(accountVO.getAliases()!=null && accountVO.getAliases().size()>0){
			for(AccountAliasVO alias : accountVO.getAliases()){
				if(alias.getName().equalsIgnoreCase(accountVO.getAccount()) && alias.getDomain().equals(accountVO.getDomain())){
					hasPrincipal=true;
				}
				insertAlias(accountVO.getDomain(),accountVO.getAccount(),alias);
			}
		}
		if(!hasPrincipal){
			AccountAliasVO alias = new AccountAliasVO();
			alias.setName(accountVO.getAccount());
			alias.setDomain(accountVO.getDomain());
			alias.setDataSource(AccountVO.MANUAL);
			insertAlias(accountVO.getDomain(),accountVO.getAccount(),alias);
		}
	}
	
	@Override
	public PageResult<AccountVO> findMembersByGroupId(String domainId, String groupName, Integer pageNo, Integer pageSize){
		String sql = SELECT + " WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+AccountMapper.GROUP_NAME+"` = :groupName ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);
		source.addValue("groupName", groupName);
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+AccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+AccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new AccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<AccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(AccountVO accountResult : result.getPageData()){
				List<AccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
				accountResult.setAliases(aliases);
			}
		}
		return result;
	}

	@Override
	public PageResult<AccountVO> findMembersByDomainIdWithoutGroup(String domainId, Integer pageNo, Integer pageSize){
		String sql = SELECT + " WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+AccountMapper.GROUP_NAME+"` IS NULL ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+AccountMapper.DOMAIN_ID+"`");
		pi.getOrderByList().add("`"+AccountMapper.ACCOUNT+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new AccountMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		PageResult<AccountVO> result = super.findByPage(pi);
		
		if(result!=null && result.getPageData().size()>0){
			for(AccountVO accountResult : result.getPageData()){
				List<AccountAliasVO> aliases = findAliases(accountResult.getAccount(),accountResult.getDomain());
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

	@Override
	public void deleteProperties(String account, String domainId) {
			String deleteSql = " DELETE FROM email_accounts_properties WHERE account=:account AND domain_id = :domainId ";
			MapSqlParameterSource deleteSource = new MapSqlParameterSource("account",account);
			deleteSource.addValue("domainId", domainId);
			template.update(deleteSql, deleteSource);
	}
	
}
