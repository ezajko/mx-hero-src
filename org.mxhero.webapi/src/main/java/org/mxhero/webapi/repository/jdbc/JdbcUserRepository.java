package org.mxhero.webapi.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.mxhero.webapi.infrastructure.pagination.common.PageResult;
import org.mxhero.webapi.infrastructure.pagination.jdbc.BaseJdbcDao;
import org.mxhero.webapi.infrastructure.pagination.jdbc.JdbcPageInfo;
import org.mxhero.webapi.repository.UserRepository;
import org.mxhero.webapi.repository.jdbc.mapper.AuthorityMapper;
import org.mxhero.webapi.repository.jdbc.mapper.UserMapper;
import org.mxhero.webapi.vo.UserVO;
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
public class JdbcUserRepository extends BaseJdbcDao<UserVO> implements UserRepository{
	
	private NamedParameterJdbcTemplate template;
	
	private String SELECT = " SELECT" +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.CREATION+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LAST_NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.LOCALE+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.NOTIFY_MAIL+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.SOUNDS_ENABLED+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.USER_NAME+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.DOMAIN+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.PASSWORD+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ACCOUNT+"`," +
			" `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ACCOUNT_DOMAIN+"`" +
			" FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`";

	@Autowired
	public JdbcUserRepository(@Qualifier("mxheroDataSource")DataSource ds) {
		this.template = new NamedParameterJdbcTemplate(ds);
		super.setNamedParameterJdbcTemplate(template);
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public PageResult<UserVO> findall(String domain, Integer pageNo, Integer pageSize) {
		String sql = SELECT;
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=20;
		}
		MapSqlParameterSource source = new MapSqlParameterSource();
		if(domain!=null && !domain.trim().isEmpty()){
			sql = sql + " WHERE `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.DOMAIN+"` = :domain ";
			source.addValue("domain",domain);
		}
		JdbcPageInfo pi = new JdbcPageInfo();
		pi.setOrderByList(new ArrayList<String>());
		pi.getOrderByList().add("`"+UserMapper.DOMAIN+"`");
		pi.setPageNo(pageNo);
		pi.setPageSize(pageSize);
		pi.putRowMapper(new UserMapper());
		pi.putSql(sql);
		pi.putExampleModel(source.getValues());
		return super.findByPage(pi);
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public UserVO insert(UserVO applicationUser, String rol, String password) {
		String sql = " INSERT INTO `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` (`"+UserMapper.CREATION+"`," +
				" `"+UserMapper.ENABLED+"`, `"+UserMapper.LAST_NAME+"`," +
				" `"+UserMapper.LOCALE+"`," +
				" `"+UserMapper.NAME+"`,`"+UserMapper.NOTIFY_MAIL+"`," +
				" `"+UserMapper.PASSWORD+"`,`"+UserMapper.SOUNDS_ENABLED+"`," +
				" `"+UserMapper.USER_NAME+"`,`"+UserMapper.DOMAIN+"`," +
				" `"+UserMapper.ACCOUNT+"`,`"+UserMapper.ACCOUNT_DOMAIN+"`) VALUES" +
				" (:creation,:enabled,:lastName,:locale,:name," +
				" :notifyEmail,:password,:soundsEnabled,:userName,:domain,:account,:account_domain);";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("creation", applicationUser.getCreated().getTime());
		source.addValue("enabled", true);
		source.addValue("lastName", applicationUser.getLastName());
		source.addValue("locale", applicationUser.getLocale());
		source.addValue("name", applicationUser.getUserName());
		source.addValue("notifyEmail", applicationUser.getNotifyEmail());
		source.addValue("password", password);
		source.addValue("soundsEnabled", applicationUser.getSoundsEnabled());
		source.addValue("userName", applicationUser.getUserName());
		source.addValue("domain", applicationUser.getDomain());
		source.addValue("account", applicationUser.getAccount());
		source.addValue("account_domain", applicationUser.getDomain());
		KeyHolder userKey = new GeneratedKeyHolder();
		template.update(sql, source,userKey);
		
		int roleAdminId =-1;
		if(rol==null){
			roleAdminId = template.getJdbcOperations().queryForInt("SELECT `"+AuthorityMapper.ID+"` FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"` WHERE `"+AuthorityMapper.AUTHORITY+"` = '"+UserVO.ROLE_DOMAIN_ADMIN+"';");
		}else if(rol.equalsIgnoreCase(UserVO.ROLE_ADMIN)){
			roleAdminId = template.getJdbcOperations().queryForInt("SELECT `"+AuthorityMapper.ID+"` FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"` WHERE `"+AuthorityMapper.AUTHORITY+"` = '"+UserVO.ROLE_ADMIN+"';");
		}else if (rol.equalsIgnoreCase(UserVO.ROLE_DOMAIN_ADMIN)){
			roleAdminId = template.getJdbcOperations().queryForInt("SELECT `"+AuthorityMapper.ID+"` FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"` WHERE `"+AuthorityMapper.AUTHORITY+"` = '"+UserVO.ROLE_DOMAIN_ADMIN+"';");
		}else if (rol.equalsIgnoreCase(UserVO.ROLE_DOMAIN_ACCOUNT)){
			roleAdminId = template.getJdbcOperations().queryForInt("SELECT `"+AuthorityMapper.ID+"` FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"` WHERE `"+AuthorityMapper.AUTHORITY+"` = '"+UserVO.ROLE_DOMAIN_ACCOUNT+"';");
		}
		
		MapSqlParameterSource aUsource = new MapSqlParameterSource();
		aUsource.addValue("authId", roleAdminId);
		aUsource.addValue("userId", userKey.getKey().intValue());
		template.update("INSERT INTO `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"` (`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"`,`"+AuthorityMapper.MTM_USERS_USER_ID+"`) VALUES (:authId, :userId);", aUsource);
		return finbById(userKey.getKey().intValue());
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void update(UserVO applicationUser) {
		String userUpdateSql = 
				"UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.LAST_NAME+"` = :userLastName ," +
				" `"+UserMapper.NAME+"` = :userName, " +
				" `"+UserMapper.NOTIFY_MAIL+"` = :notifyEmail, " +
				" `"+UserMapper.LOCALE+"` = :locale, " +
				" `"+UserMapper.SOUNDS_ENABLED+"` = :soundsEnabled " +
				" WHERE `"+UserMapper.USER_NAME+"` = :username;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("userLastName", applicationUser.getLastName());
		source.addValue("userName", applicationUser.getName());
		source.addValue("notifyEmail", applicationUser.getNotifyEmail());
		source.addValue("locale", applicationUser.getLocale());
		source.addValue("soundsEnabled", applicationUser.getSoundsEnabled());
		source.addValue("username",applicationUser.getUserName());
		template.update(userUpdateSql, source);
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void delete(String username) {
		MapSqlParameterSource source = new MapSqlParameterSource("username",username);
		String userSql = "DELETE FROM `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
				" WHERE `"+UserMapper.USER_NAME+"` = :username";
		template.update(userSql,source);
	}
	
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public boolean changePassword(String oldPassword, String newPassword, String username) {
		String sql = "UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.PASSWORD+"` = :newPassword " +
				" WHERE `"+UserMapper.USER_NAME+"` = :username AND `"+UserMapper.PASSWORD+"` = :oldPassword;";
		MapSqlParameterSource source = new MapSqlParameterSource("username",username);
		source.addValue("newPassword", newPassword);
		source.addValue("oldPassword", oldPassword);
		return template.update(sql, source) > 0;
	}
	
	@Override
	@Transactional(value="mxhero",readOnly=false)
	public boolean setPassword(String newPassword, String username) {
		String sql = "UPDATE `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"` " +
				" SET `"+UserMapper.PASSWORD+"` = :newPassword " +
				" WHERE `"+UserMapper.USER_NAME+"` = :username ;";
		MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue("newPassword", newPassword);
		source.addValue("username", username);
		return template.update(sql, source) > 0;
	}
	
	@Override
	public UserVO finbByNotifyEmail(String email) {
		String sql = SELECT + " WHERE `"+UserMapper.NOTIFY_MAIL+"` = :email ;";
		UserVO user = null;
		List<UserVO> users = template.query(sql, new MapSqlParameterSource("email",email), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getUserName()));
		}
		return user;
	}

	@Override
	public UserVO finbByUserName(String userName) {
		String sql = SELECT + " WHERE `"+UserMapper.USER_NAME+"` = :userName ;";
		UserVO user = null;
		List<UserVO> users = template.query(sql, new MapSqlParameterSource("userName",userName), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getUserName()));
		}
		return user;
	}
	
	@Override
	public UserVO finbByAccount(String domain, String account) {
		String sql = SELECT + " WHERE `"+UserMapper.ACCOUNT+"` = :account  AND `"+UserMapper.DOMAIN+"` = :domain";
		UserVO user = null;
		MapSqlParameterSource source = new MapSqlParameterSource("account",account);
		source.addValue("domain", domain);
		List<UserVO> users = template.query(sql,source, new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getUserName()));
		}
		return user;
	}
	
	private UserVO finbById(Integer id) {
		String sql = SELECT + " WHERE `"+UserMapper.ID+"` = :id ;";
		UserVO user = null;
		List<UserVO> users = template.query(sql, new MapSqlParameterSource("id",id), new UserMapper());
		if(users!=null && users.size()>0){
			user = users.get(0);
			user.setAuthorities(getAuthorities(user.getUserName()));
		}
		return user;
	}

	private List<String> getAuthorities(String username){
		String sql = "SELECT `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"`," +
				" `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.AUTHORITY+"`" +
				" FROM `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.TABLE_NAME+"`" +
				" INNER JOIN `"+AuthorityMapper.DATABASE+"`.`"+AuthorityMapper.MTM_USERS_TABLE+"`" +
				" ON `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_AUTHORITY_ID+"` = `"+AuthorityMapper.TABLE_NAME+"`.`"+AuthorityMapper.ID+"` " +
				" INNER JOIN `"+UserMapper.DATABASE+"`.`"+UserMapper.TABLE_NAME+"`" +
				" ON `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.ID+"` = `"+AuthorityMapper.MTM_USERS_TABLE+"`.`"+AuthorityMapper.MTM_USERS_USER_ID+"`" +
				" WHERE  `"+UserMapper.TABLE_NAME+"`.`"+UserMapper.USER_NAME+"` = :username;";
		return template.query(sql, new MapSqlParameterSource("username",username), new AuthorityMapper());
	}

}
