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
import org.mxhero.webapi.repository.DomainRepository;
import org.mxhero.webapi.repository.jdbc.mapper.DomainMapper;
import org.mxhero.webapi.repository.jdbc.mapper.AccountAliasMapper;
import org.mxhero.webapi.repository.jdbc.mapper.UserMapper;
import org.mxhero.webapi.vo.DomainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=true)
public class JdbcDomainRepository extends BaseJdbcDao<DomainVO> implements DomainRepository{

	private static final String SELECT = "SELECT `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.DOMAIN+"`," +
			" `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.CREATION+"`," +
			" `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.SERVER+"`," +
			" `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.UPDATED+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NOTIFY_MAIL+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"`" +
			" FROM `"+DomainMapper.DATABASE+"`.`"+DomainMapper.TABLE_NAME+"`" +
			" LEFT OUTER JOIN `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
			" ON `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.DOMAIN+"` = `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.DOMAIN+"`";
	
	private static final String ORDER = " ORDER BY `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.DOMAIN+"` ";
	
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcDomainRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}
	
	@Override
	public DomainVO findById(String domainId) {
		String sql = SELECT + 
				" WHERE `"+DomainMapper.TABLE_NAME+"`.`"+DomainMapper.DOMAIN+"` = :domainId "
				+ ORDER;
		List<DomainVO> domains = template.query(sql, new MapSqlParameterSource("domainId",domainId), new DomainMapper());
		if(domains!=null && domains.size()>0){
			DomainVO domain = domains.get(0);
			domain.setAliases(findAliases(domain.getDomain()));
			return domain;
		}
		return null;
	}

	@Override
	public DomainVO findByUserId(Integer userId) {
		String sql = SELECT 
				+ " WHERE `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"` = :userId "
				+ ORDER;
		List<DomainVO> domains = template.query(sql, new MapSqlParameterSource("userId",userId), new DomainMapper());
		if(domains!=null && domains.size()>0){
			DomainVO domain = domains.get(0);
			domain.setAliases(findAliases(domain.getDomain()));
			return domain;
		}
		return null;
	}

	@Override
	public List<String> findAliases(String domainId) {
		String sql = "SELECT `"+DomainMapper.ALIASES_ALIAS+"` " +
				" FROM `"+DomainMapper.DATABASE+"`.`"+DomainMapper.ALIASES_TABLE_NAME+"`" +
				" WHERE `"+DomainMapper.ALIASES_DOMAIN+"` = :domainId " +
				" ORDER BY `"+DomainMapper.ALIASES_DOMAIN+"` ";
		return template.queryForList(sql, new MapSqlParameterSource("domainId",domainId), String.class);
	}

	private void insertAlias(String domainId, String alias) {
		String sql = "INSERT INTO  `"+DomainMapper.DATABASE+"`.`"+DomainMapper.ALIASES_TABLE_NAME+"`" +
				" (`"+DomainMapper.ALIASES_DOMAIN+"`,`"+DomainMapper.ALIASES_ALIAS+"`,`"+DomainMapper.ALIASES_CREATED+"`)" +
				" VALUES (:domainId,:alias,:created);";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("alias", alias);
		source.addValue("created", Calendar.getInstance().getTime());
		template.update(sql,source);
	}

	private void deleteAlias(String domainId, String alias) {
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("alias", alias);
		
		String accountSql = "DELETE FROM `"+AccountAliasMapper.DATABASE+"`.`"+AccountAliasMapper.TABLE_NAME+"`" +
				" WHERE `"+AccountAliasMapper.DOMAIN_ALIAS+"` = :alias " +
				" AND `"+AccountAliasMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(accountSql,source);
		
		String sql = "DELETE FROM  `"+DomainMapper.DATABASE+"`.`"+DomainMapper.ALIASES_TABLE_NAME+"`" +
				" WHERE `"+DomainMapper.ALIASES_DOMAIN+"` = :domainId " +
				" AND `"+DomainMapper.ALIASES_ALIAS+"` = :alias;";

		template.update(sql,source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(DomainVO domainVO) {
		String sql = "INSERT INTO `"+DomainMapper.DATABASE+"`.`"+DomainMapper.TABLE_NAME+"`" +
				" (`"+DomainMapper.DOMAIN+"`,`"+DomainMapper.SERVER+"`,`"+DomainMapper.CREATION+"`,`"+DomainMapper.UPDATED+"`)" +
				" VALUES(:domainId,:server,:creation,:updated);";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainVO.getDomain());
		source.addValue("server", domainVO.getServer());
		source.addValue("creation", Calendar.getInstance().getTime());
		source.addValue("updated", Calendar.getInstance().getTime());
		template.update(sql, source);
		insertAlias(domainVO.getDomain(),domainVO.getDomain());
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(String domainIdd) {
		String aliasesSql = "DELETE FROM `"+DomainMapper.DATABASE+"`.`"+DomainMapper.ALIASES_TABLE_NAME+"`" +
				" WHERE `"+DomainMapper.ALIASES_DOMAIN+"` = :domainId ;";
		template.update(aliasesSql,new MapSqlParameterSource("domainId",domainIdd));
		String domainSql = "DELETE FROM `"+DomainMapper.DATABASE+"`.`"+DomainMapper.TABLE_NAME+"`" +
				" WHERE `"+DomainMapper.DOMAIN+"` = :domainId ;";
		template.update(domainSql,new MapSqlParameterSource("domainId",domainIdd));
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public DomainVO update(DomainVO domainVO) {
		String sql = "UPDATE `"+DomainMapper.DATABASE+"`.`"+DomainMapper.TABLE_NAME+"`" +
				" SET `"+DomainMapper.SERVER+"` = :server , `"+DomainMapper.UPDATED+"` = :updated" +
				" WHERE `"+DomainMapper.DOMAIN+"` = :domainId ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainVO.getDomain());
		source.addValue("updated", Calendar.getInstance().getTime());
		source.addValue("server", domainVO.getServer());
		template.update(sql, source);
		if(domainVO.getAliases()!=null){
			Set<String> insertAliases = new HashSet<String>(domainVO.getAliases());
			List<String> existsAliases = findAliases(domainVO.getDomain());
			if(existsAliases!=null && existsAliases.size()>0){
				Set<String> deleteAliases = new HashSet<String>(existsAliases);
				deleteAliases.removeAll(insertAliases);
				for(String aliasToDelete : deleteAliases){
					if(!domainVO.getDomain().equalsIgnoreCase(aliasToDelete)){
						deleteAlias(domainVO.getDomain(), aliasToDelete);
					}
				}
			}
			insertAliases.removeAll(existsAliases);
			for(String aliasToInsert : insertAliases){
				insertAlias(domainVO.getDomain(), aliasToInsert);
			}
		}
		return findById(domainVO.getDomain());
	}

	@Override
	public PageResult<DomainVO> findAll(Integer pageNo, Integer pageSize) {
		String sql = SELECT;
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		MapSqlParameterSource source = new MapSqlParameterSource();
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+DomainMapper.DOMAIN+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new DomainMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		return super.findByPage(pi);
	}

}
