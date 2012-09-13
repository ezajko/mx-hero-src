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

import org.mxhero.console.backend.vo.EmailAccountVO;
import org.springframework.jdbc.core.RowMapper;

public class EmailAccountMapper implements RowMapper<EmailAccountVO>{

	public static final String DATABASE="mxhero";
	public static final String TABLE_NAME="email_accounts";
	
	public static final String ACCOUNT = "account";
	public static final String DOMAIN_ID = "domain_id";
	public static final String CREATED = "created";
	public static final String DATA_SOURCE = "data_source";
	public static final String UPDATED = "updated";
	public static final String GROUP_NAME = "group_name";
	
	@Override
	public EmailAccountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmailAccountVO email = new EmailAccountVO();
		email.setAccount(rs.getString(ACCOUNT));
		if(rs.getTimestamp(CREATED)!=null){
			Calendar created = Calendar.getInstance();
			created.setTimeInMillis(rs.getTimestamp(CREATED).getTime());
			email.setCreatedDate(created);
		}
		email.setDataSource(rs.getString(DATA_SOURCE));
		email.setDomain(rs.getString(DOMAIN_ID));
		email.setGroup(rs.getString(GROUP_NAME));
		if(rs.getTimestamp(UPDATED)!=null){
			Calendar updatedDate = Calendar.getInstance();
			updatedDate.setTimeInMillis(rs.getTimestamp(UPDATED).getTime());
			email.setUpdatedDate(updatedDate);
		}
		return email;
	}
	
}
