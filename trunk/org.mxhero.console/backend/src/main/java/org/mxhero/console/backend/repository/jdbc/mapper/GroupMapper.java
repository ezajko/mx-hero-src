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

import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.jdbc.core.RowMapper;

public class GroupMapper implements RowMapper<GroupVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="groups";
	
	public static final String DOMAIN_ID="domain_id";
	public static final String NAME="name";
	public static final String CREATED="created";
	public static final String DESCRIPTION="description";
	public static final String UPDATED="updated";
	
	@Override
	public GroupVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		GroupVO group = new GroupVO();
		if(rs.getTimestamp(CREATED)!=null){
			Calendar createdDate = Calendar.getInstance();
			createdDate.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			group.setCreatedDate(createdDate);
		}
		group.setDescription(rs.getString(DESCRIPTION));
		group.setDomain(rs.getString(DOMAIN_ID));
		group.setName(rs.getString(NAME));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			group.setUpdatedDate(updatedDate);
		}
		return group;
	}
	
}
