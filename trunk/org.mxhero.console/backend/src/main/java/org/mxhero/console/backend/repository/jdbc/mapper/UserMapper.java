package org.mxhero.console.backend.repository.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<ApplicationUserVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="app_users";
	
	public static final String LAST_NAME="last_name";
	public static final String CREATION="creation";
	public static final String ID="id";
	public static final String DOMAIN="domain";
	public static final String LOCALE="locale";
	public static final String NAME="name";
	public static final String NOTIFY_MAIL="notify_email";
	public static final String SOUNDS_ENABLED="sounds_enabled";
	public static final String USER_NAME="user_name";
	public static final String ENABLED="enabled";
	public static final String LAST_PASSWORD_UPDATE="last_password_update";
	public static final String PASSWORD="password";
	
	
	@Override
	public ApplicationUserVO mapRow(ResultSet rs, int param) throws SQLException {
		ApplicationUserVO user = new ApplicationUserVO();
		Calendar creation = null;
		if(rs.getTimestamp(CREATION)!=null){
			creation = Calendar.getInstance();
			creation.setTimeInMillis(rs.getTimestamp(CREATION).getTime());
		}
		user.setCreationDate(creation);
		user.setId(rs.getInt(ID));
		user.setLastName(rs.getString(LAST_NAME));
		user.setLocale(rs.getString(LOCALE));
		user.setName(rs.getString(NAME));
		user.setNotifyEmail(rs.getString(NOTIFY_MAIL));
		user.setSoundsEnabled(rs.getBoolean(SOUNDS_ENABLED));
		user.setUserName(rs.getString(USER_NAME));
		return user;
	}

}
