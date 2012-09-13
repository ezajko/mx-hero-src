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

import java.util.List;

import javax.sql.DataSource;

import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.repository.jdbc.mapper.AuthorityMapper;
import org.mxhero.console.backend.repository.jdbc.mapper.UserMapper;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value="mxhero",readOnly=false)
public class JdbcUserRepository implements UserRepository{

	private NamedParameterJdbcTemplate template;
	
	private String SELECT = " SELECT" +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.CREATION+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LAST_NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LOCALE+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NOTIFY_MAIL+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.SOUNDS_ENABLED+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.USER_NAME+"`" +
			" FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`";

	@Autowired
	public JdbcUserRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public ApplicationUserVO insert(ApplicationUserVO applicationUserVO, String password) {
		String sql = " INSERT INTO `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` (`"+UserMapper.CREATION+"`," +
				" `"+UserMapper.ENABLED+"`, `"+UserMapper.LAST_NAME+"`," +
				" `"+UserMapper.LOCALE+"`," +
				" `"+UserMapper.NAME+"`,`"+UserMapper.NOTIFY_MAIL+"`," +
				" `"+UserMapper.PASSWORD+"`,`"+UserMapper.SOUNDS_ENABLED+"`," +
				" `"+UserMapper.USER_NAME+"`,`"+UserMapper.DOMAIN+"`) VALUES" +
				" (:creation,:enabled,:lastName,:locale,:name," +
				" :notifyEmail,:password,:soundsEnabled,:userName,:domain);";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("creation", applicationUserVO.getCreationDate().getTime());
		source.addValue("enabled", true);
		source.addValue("lastName", applicationUserVO.getLastName());
		source.addValue("locale", applicationUserVO.getLocale());
		source.addValue("name", applicationUserVO.getUserName());
		source.addValue("notifyEmail", applicationUserVO.getNotifyEmail());
		source.addValue("password", password);
		source.addValue("soundsEnabled", applicationUserVO.getSoundsEnabled());
		source.addValue("userName", applicationUserVO.getUserName());
		source.addValue("domain", applicationUserVO.getDomain().getDomain());
		KeyHolder userKey = new GeneratedKeyHolder();
		template.update(sql, source,userKey);
		int roleAdminId = template.getJdbcOperations().queryForInt("SELECT `"+AuthorityMapper.ID+"` FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"` WHERE `"+AuthorityMapper.AUTHORITY+"` = '"+AuthorityVO.ROLE_DOMAIN_ADMIN+"';");
		MapSqlParameterSource aUsource = new MapSqlParameterSource();
		aUsource.addValue("authId", roleAdminId);
		aUsource.addValue("userId", userKey.getKey().intValue());
		template.update("INSERT INTO `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"` (`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"`,`"+AuthorityMapper.MTM_USERS_USER_ID+"`) VALUES (:authId, :userId);", aUsource);
		return finbById(userKey.getKey().intValue());
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(ApplicationUserVO applicationUserVO) {
		String userUpdateSql = 
				"UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.LAST_NAME+"` = :userLastName ," +
				" `"+UserMapper.NAME+"` = :userName, " +
				" `"+UserMapper.NOTIFY_MAIL+"` = :notifyEmail, " +
				" `"+UserMapper.LOCALE+"` = :locale, " +
				" `"+UserMapper.SOUNDS_ENABLED+"` = :soundsEnabled " +
				" WHERE `"+UserMapper.ID+"` = :userId;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("userLastName", applicationUserVO.getLastName());
		source.addValue("userName", applicationUserVO.getUserName());
		source.addValue("notifyEmail", applicationUserVO.getNotifyEmail());
		source.addValue("locale", applicationUserVO.getLocale());
		source.addValue("soundsEnabled", applicationUserVO.getSoundsEnabled());
		source.addValue("userId",applicationUserVO.getId());
		template.update(userUpdateSql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(Integer id) {
		String authoritiesSql = "DELETE FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" WHERE `"+AuthorityMapper.MTM_USERS_USER_ID+"` = :userId;";
		template.update(authoritiesSql,new MapSqlParameterSource("userId",id));
		String userSql = "DELETE FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
				" WHERE `"+UserMapper.ID+"` = :userId;";
		template.update(userSql,new MapSqlParameterSource("userId",id));
	}
	
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public boolean changePassword(String oldPassword, String newPassword, Integer id) {
		String sql = "UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.PASSWORD+"` = :newPassword " +
				" WHERE `"+UserMapper.ID+"` = :userId AND `"+UserMapper.PASSWORD+"` = :oldPassword;";
		MapSqlParameterSource source = new MapSqlParameterSource("userId",id);
		source.addValue("newPassword", newPassword);
		source.addValue("oldPassword", oldPassword);
		return template.update(sql, source) > 0;
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void setPassword(String newPassword, Integer id) {
		String sql = "UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.PASSWORD+"` = :newPassword " +
				" WHERE `"+UserMapper.ID+"` = :userId ;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("newPassword", newPassword);
		source.addValue("userId", id);
		template.update(sql, source);
	}
	
	@Override
	public ApplicationUserVO finbByNotifyEmail(String email) {
		String sql = SELECT + " WHERE `"+UserMapper.NOTIFY_MAIL+"` = :email ;";
		ApplicationUserVO user = null;
		List<ApplicationUserVO> users = template.query(sql, new MapSqlParameterSource("email",email), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getId()));
		}
		return user;
	}

	@Override
	public ApplicationUserVO finbByUserName(String userName) {
		String sql = SELECT + " WHERE `"+UserMapper.USER_NAME+"` = :userName ;";
		ApplicationUserVO user = null;
		List<ApplicationUserVO> users = template.query(sql, new MapSqlParameterSource("userName",userName), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getId()));
		}
		return user;
	}
	
	@Override
	public ApplicationUserVO finbById(Integer id) {
		String sql = SELECT + " WHERE `"+UserMapper.ID+"` = :id ;";
		ApplicationUserVO user = null;
		List<ApplicationUserVO> users = template.query(sql, new MapSqlParameterSource("id",id), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getId()));
		}
		return user;
	}

	private List<AuthorityVO> getAuthorities(Integer userId){
		String sql = "SELECT `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"`," +
				" `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`" +
				" INNER JOIN `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" ON `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"` = `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"` " +
				" WHERE  `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_USER_ID+"` = :userId;";
		return template.query(sql, new MapSqlParameterSource("userId",userId), new AuthorityMapper());
	}

	public List<AuthorityVO> findAllAuthorities(){
		String sql = "SELECT `"+AuthorityMapper.ID+"`, `"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`;";
		return template.getJdbcOperations().query(sql, new AuthorityMapper());
	}
}
