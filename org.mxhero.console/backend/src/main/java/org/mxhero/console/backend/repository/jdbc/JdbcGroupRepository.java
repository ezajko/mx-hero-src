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

import javax.sql.DataSource;

import org.mxhero.console.backend.infrastructure.pagination.common.PageResult;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.console.backend.infrastructure.pagination.jdbc.JdbcPageInfo;
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
		String accountsSql = "UPDATE `"+EmailAccountMapper.DATABASE+"`.`"+EmailAccountMapper.TABLE_NAME+"`" +
				" SET `"+EmailAccountMapper.GROUP_NAME+"` = NULL " +
				" WHERE `"+EmailAccountMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(accountsSql, new MapSqlParameterSource("domainId",domainId));
		
		String groupSql = "DELETE FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId ;";
		template.update(groupSql, new MapSqlParameterSource("domainId",domainId));
	}

	@Override
	public PageResult<GroupVO> findByDomain(String domainId, int pageNo, int pageSize) {
		String sql = "SELECT `"+GroupMapper.DOMAIN_ID+"`,`"+GroupMapper.NAME+"`," +
				" `"+GroupMapper.CREATED+"`,`"+GroupMapper.DESCRIPTION+"`," +
				" `"+GroupMapper.UPDATED+"`" +
				" FROM `"+GroupMapper.DATABASE+"`.`"+GroupMapper.TABLE_NAME+"`" +
				" WHERE `"+GroupMapper.DOMAIN_ID+"` = :domainId";
		
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
