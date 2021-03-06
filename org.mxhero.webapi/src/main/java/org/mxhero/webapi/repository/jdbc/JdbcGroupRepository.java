package org.mxhero.webapi.repository.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.webapi.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.webapi.repository.GroupRepository;
import org.mxhero.webapi.repository.jdbc.mapper.AccountMapper;
import org.mxhero.webapi.repository.jdbc.mapper.GroupMapper;
import org.mxhero.webapi.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcGroupRepository extends BaseJdbcDao<GroupVO> implements GroupRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcGroupRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String accountsSql = "UPDATE `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" SET `"+AccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(accountsSql, new MapSqlParameterSource("domainId",domainId));
		
		String groupSql = "DELETE FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(groupSql, new MapSqlParameterSource("domainId",domainId));
	}

	@Override
	public PageResult<GroupVO> findByDomain(String domainId, Integer pageNo, Integer pageSize) {
		String sql = "SELECT `"+GroupMapper.DOMAIN_ID+"`,`"+GroupMapper.NAME+"`," +
				" `"+GroupMapper.CREATED+"`,`"+GroupMapper.DESCRIPTION+"`," +
				" `"+GroupMapper.UPDATED+"`" +
				" FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId";
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		MapSqlParameterSource source = new MapSqlParameterSource("domainId", domainId);

		//new Page Sistem Add
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+GroupMapper.NAME+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new GroupMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		return super.findByPage(pi);
	}

	@Override
	public GroupVO find(String domain, String name) {
		String sql = "SELECT `"+GroupMapper.DOMAIN_ID+"`,`"+GroupMapper.NAME+"`," +
				" `"+GroupMapper.CREATED+"`,`"+GroupMapper.DESCRIPTION+"`," +
				" `"+GroupMapper.UPDATED+"`" +
				" FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domain " +
				" AND `"+GroupMapper.NAME+"` = :name ";
		
		MapSqlParameterSource source = new MapSqlParameterSource("domain", domain);
		source.addValue("name", name);
		List<GroupVO> result = template.query(sql, source, new GroupMapper());
		if(result!=null && result.size()>0){
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void insert(GroupVO group) {
		String sql = "INSERT INTO `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"` " +
				" (`"+GroupMapper.DOMAIN_ID+"`,`"+GroupMapper.NAME+"`," +
				" `"+GroupMapper.CREATED+"`,`"+GroupMapper.DESCRIPTION+"`, `"+GroupMapper.UPDATED+"`)" +
				" VALUES(:domainId,:name,:created,:description,:updated) ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",group.getDomain());
		source.addValue("name", group.getName());
		source.addValue("created", Calendar.getInstance().getTime());
		source.addValue("description", group.getDescription());
		source.addValue("updated", Calendar.getInstance().getTime());
		template.update(sql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(GroupVO group) {
		String sql = "UPDATE `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"` " +
				" SET `"+GroupMapper.DESCRIPTION+"` = :description, `"+GroupMapper.UPDATED+"` = :updated" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId AND `"+GroupMapper.NAME+"` = :group;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",group.getDomain());
		source.addValue("group", group.getName());
		source.addValue("description", group.getDescription());
		source.addValue("updated", Calendar.getInstance().getTime());
		template.update(sql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(String group, String domainId) {
		String accountsSql = "UPDATE `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" SET `"+AccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+AccountMapper.GROUP_NAME+"` = :group ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("group", group);
		template.update(accountsSql, source);
		
		String groupSql = "DELETE FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+GroupMapper.NAME+"` = :group;";
		template.update(groupSql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void releaseMembers(String group, String domainId) {
		String accountsSql = "UPDATE `"+AccountMapper.DATABASE+"`.`"+AccountMapper.TABLE_NAME+"`" +
				" SET `"+AccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+AccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+AccountMapper.GROUP_NAME+"` = :group ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("group", group);
		template.update(accountsSql, source);
	}

}
