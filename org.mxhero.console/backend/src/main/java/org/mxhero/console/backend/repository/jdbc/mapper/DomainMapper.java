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

import org.mxhero.console.backend.vo.DomainVO;
import org.mxhero.console.backend.vo.OwnerVO;
import org.springframework.jdbc.core.RowMapper;

public class DomainMapper implements RowMapper<DomainVO>{

	public static final String DATABASE = "mxhero";
	public static final String TABLE_NAME = "domain";
	
	public static final String DOMAIN = "domain";
	public static final String CREATION = "creation";
	public static final String SERVER = "server";
	public static final String UPDATED = "updated";
	
	public static final String ALIASES_TABLE_NAME = "domains_aliases";
	public static final String ALIASES_ALIAS = "alias";
	public static final String ALIASES_CREATED = "created";
	public static final String ALIASES_DOMAIN = "domain";
	
	@Override
	public DomainVO mapRow(ResultSet rs, int param) throws SQLException {
		DomainVO domain = new DomainVO();
		if(rs.getString(UserMapper.NOTIFY_MAIL)!=null){
			domain.setOwner(new OwnerVO());
			domain.getOwner().setEmail(rs.getString(UserMapper.NOTIFY_MAIL));
			domain.getOwner().setId(rs.getInt(UserMapper.ID));
		}
		if(rs.getTimestamp(CREATION)!=null){
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTimeInMillis(rs.getTimestamp(CREATION).getTime());
			domain.setCreationDate(creationDate);
		}
		domain.setDomain(rs.getString(DOMAIN));
		domain.setServer(rs.getString(SERVER));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			domain.setUpdatedDate(updatedDate);
		}
		
		domain.setUserName(UserMapper.USER_NAME);
		return domain;
	}

}
