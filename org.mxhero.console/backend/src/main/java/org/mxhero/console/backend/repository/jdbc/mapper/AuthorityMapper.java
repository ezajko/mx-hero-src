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

import org.mxhero.console.backend.vo.AuthorityVO;
import org.springframework.jdbc.core.RowMapper;

public class AuthorityMapper implements RowMapper<AuthorityVO>{

	public static final String MTM_USERS_TABLE = "app_users_authorities";
	public static final String MTM_USERS_USER_ID = "app_users_id";
	public static final String MTM_USERS_AUTHORITY_ID = "authorities_id";
	
	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="authorities";
	public static final String ID="id";
	public static final String AUTHORITY="authority";
	
	@Override
	public AuthorityVO mapRow(ResultSet rs, int param) throws SQLException {
		AuthorityVO authority = new AuthorityVO();
		authority.setAuthority(rs.getString(AUTHORITY));
		authority.setId(rs.getInt(ID));
		return authority;
	}

}
