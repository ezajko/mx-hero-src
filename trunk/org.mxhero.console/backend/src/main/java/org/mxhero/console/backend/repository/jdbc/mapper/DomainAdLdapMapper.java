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

import org.mxhero.console.backend.vo.DomainAdLdapVO;
import org.springframework.jdbc.core.RowMapper;

public class DomainAdLdapMapper implements RowMapper<DomainAdLdapVO> {

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain_adldap";
	
	public static final String DOMAIN = "domain";
	public static final String ADDRESS = "address";
	public static final String BASE ="base";
	public static final String DIRECTORY_TYPE = "directory_type";
	public static final String LAST_ERROR = "last_error";
	public static final String FILTER = "filter";
	public static final String LAST_UPDATE = "last_update";
	public static final String NEXT_UPDATE = "next_update";
	public static final String OVERRIDE_FLAG = "override_flag";
	public static final String PASSWORD = "password";
	public static final String PORT = "port";
	public static final String SSL_FLAG = "ssl_flag";
	public static final String USER = "user";
	public static final String DN_AUTHENTICATE = "dn_authenticate";
	
	@Override
	public DomainAdLdapVO mapRow(ResultSet rs, int param) throws SQLException {
		Calendar lastUpdate = null;
		Calendar nextUpdate = null;
		DomainAdLdapVO adladap = new DomainAdLdapVO();
		adladap.setAddres(rs.getString(ADDRESS));
		adladap.setBase(rs.getString(BASE));
		adladap.setDirectoryType(rs.getString(DIRECTORY_TYPE));
		adladap.setDnAuthenticate(rs.getString(DN_AUTHENTICATE));
		adladap.setDomainId(rs.getString(DOMAIN));
		adladap.setError(rs.getString(LAST_ERROR));
		adladap.setFilter(rs.getString(FILTER));
		if(rs.getTimestamp(LAST_UPDATE)!=null){
			lastUpdate = Calendar.getInstance();
			lastUpdate.setTimeInMillis(rs.getTimestamp(LAST_UPDATE).getTime());
		}
		adladap.setLastUpdate(lastUpdate);
		if(rs.getTimestamp(NEXT_UPDATE)!=null){
			nextUpdate = Calendar.getInstance();
			nextUpdate.setTimeInMillis(rs.getTimestamp(NEXT_UPDATE).getTime());
		}
		adladap.setNextUpdate(nextUpdate);
		adladap.setOverrideFlag(rs.getBoolean(OVERRIDE_FLAG));
		adladap.setPassword(rs.getString(PASSWORD));
		adladap.setPort(rs.getString(PORT));
		adladap.setSslFlag(rs.getBoolean(SSL_FLAG));
		adladap.setUser(rs.getString(USER));
		return adladap;
	}

}
