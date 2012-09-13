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

package org.mxhero.engine.plugin.threadlight.internal.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mxhero.engine.plugin.threadlight.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.vo.ThreadRowPk;
import org.springframework.jdbc.core.RowMapper;

public class ThreadRowMapper implements RowMapper<ThreadRow>{


	public static final String DATABASE="threadlight";
	public static final String TABLE_NAME="thread_rows";
	
	public static final String ID="id";
	public static final String MESSAGE_ID="message_id";
	public static final String CREATION_TIME="creation_time";
	public static final String RECIPIENT_MAIL="recipient_mail";
	public static final String SENDER_MAIL="sender_mail";
	public static final String SUBJECT="message_subject";
	public static final String REPLY_TIME="reply_time";
	public static final String SNOOZE_TIME="snooze_time";
	
	@Override
	public ThreadRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		ThreadRow row = new ThreadRow();
		
		if(rs.getTimestamp(CREATION_TIME)!=null){
			row.setCreationTime(rs.getTimestamp(CREATION_TIME));
		}
		if(rs.getTimestamp(REPLY_TIME)!=null){
			row.setReplyTime(rs.getTimestamp(REPLY_TIME));
		}
		if(rs.getTimestamp(SNOOZE_TIME)!=null){
			row.setSnoozeTime(rs.getTimestamp(SNOOZE_TIME));
		}
		row.setId(rs.getLong(ID));
		row.setPk(new ThreadRowPk(rs.getString(MESSAGE_ID), rs.getString(SENDER_MAIL), rs.getString(RECIPIENT_MAIL)));
		row.setSubject(rs.getString(SUBJECT));
		
		return row;
	}

}
