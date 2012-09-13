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

import org.mxhero.engine.plugin.threadlight.vo.ThreadRowFollower;
import org.springframework.jdbc.core.RowMapper;

public class ThreadRowFollowerMapper implements RowMapper<ThreadRowFollower>{

	public static final String DATABASE="threadlight";
	public static final String TABLE_NAME="thread_rows_followers";
	
	public static final String THREAD_ID="thread_id";
	public static final String FOLLOWER="follower";
	public static final String PARAMETERS="follower_parameters";

	@Override
	public ThreadRowFollower mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		ThreadRowFollower follower = new ThreadRowFollower();
		follower.setFollower(rs.getString(FOLLOWER));
		follower.setFolowerParameters(rs.getString(PARAMETERS));
		return follower;
	}
}
