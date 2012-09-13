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
