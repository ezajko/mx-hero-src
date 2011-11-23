package org.mxhero.console.backend.repository.jdbc;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.GroupRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.EmailAccountMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.GroupMapper;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcGroupRepository implements GroupRepository{

	private NamedParameterJdbcTemplate template;
	
	@Autowired
	public JdbcGroupRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template =  new NamedParameterJdbcTemplate(ds);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void deleteByDomainId(String domainId) {
		String accountsSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(accountsSql, new MapSqlParameterSource("domainId",domainId));
		
		String groupSql = "DELETE FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(groupSql, new MapSqlParameterSource("domainId",domainId));
	}

	@Override
	public List<GroupVO> findByDomain(String domainId) {
		String sql = "SELECT `"+GroupMapper.DOMAIN_ID+"`,`"+GroupMapper.NAME+"`," +
				" `"+GroupMapper.CREATED+"`,`"+GroupMapper.DESCRIPTION+"`," +
				" `"+GroupMapper.UPDATED+"`" +
				" FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId" +
				" ORDER BY `"+GroupMapper.NAME+"` ;";
		return template.query(sql, new MapSqlParameterSource("domainId",domainId),new GroupMapper());
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
		String accountsSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` = :group ;";
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
		String accountsSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId " +
				" AND `"+EmailAccountMapper.GROUP_NAME+"` = :group ;";
		MapSqlParameterSource source = new MapSqlParameterSource("domainId",domainId);
		source.addValue("group", group);
		template.update(accountsSql, source);
	}

}
