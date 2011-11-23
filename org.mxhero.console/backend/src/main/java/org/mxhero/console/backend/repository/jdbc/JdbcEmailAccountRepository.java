package org.mxhero.console.backend.repository.jdbc;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.EmailAccountRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountAliasMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountMapper;
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
public class JdbcEmailAccountRepository implements EmailAccountRepository{
	
	private static final String SELECT = "SELECT `"+EmailAccountMapper.ACCOUNT+"`, `"+EmailAccountMapper.CREATED+"`," +
			" `"+EmailAccountMapper.DATA_SOURCE+"`, `"+EmailAccountMapper.DOMAIN_ID+"`, `"+EmailAccountMapper.GROUP_NAME+"`," +
			" `"+EmailAccountMapper.UPDATED+"` " +
			" FROM `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`";
	
	private static final String ORDER = " ORDER BY `"+EmailAccountMapper.DOMAIN_ID+"`, `"+EmailAccountMapper.ACCOUNT+"` ";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcEmailAccountRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
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
	public List<EmailAccountVO> findAll(
			String domainId, String account, String group) {
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
			source.addValue("account", "%"+account.trim()+"%");
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
		
		sql = sql + ORDER;
		
		List<EmailAccountVO> result = template.query(sql, source, new EmailAccountMapper());
		if(result!=null && result.size()>0){
			for(EmailAccountVO accountResult : result){
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
		MapSqlParameterSource accountDource = new MapSqlParameterSource("domainId",accountVO.getDomain());
		accountDource.addValue("account", accountVO.getAccount());
		accountDource.addValue("created", Calendar.getInstance().getTime());
		accountDource.addValue("dataSource", EmailAccountVO.MANUAL);
		accountDource.addValue("groupName", accountVO.getGroup());
		accountDource.addValue("updated", Calendar.getInstance().getTime());
		template.update(accountSql, accountDource);
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
	public List<EmailAccountVO> findMembersByGroupId(String domainId, String groupName){
		String sql = SELECT + " WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` = :groupName ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);
		source.addValue("groupName", groupName);
		List<EmailAccountVO> result = template.query(sql, source, new EmailAccountMapper());
		return result;
	}

	@Override
	public List<EmailAccountVO> findMembersByDomainIdWithoutGroup(String domainId){
		String sql = SELECT + " WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` IS NULL ";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);
		List<EmailAccountVO> result = template.query(sql, source, new EmailAccountMapper());
		return result;
	}
}
