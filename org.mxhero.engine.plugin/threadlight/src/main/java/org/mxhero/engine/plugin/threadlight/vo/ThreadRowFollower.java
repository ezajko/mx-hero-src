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

package org.mxhero.engine.plugin.threadlight.vo;

import java.io.Serializable;

public class ThreadRowFollower implements Serializable{

	private static final long serialVersionUID = -5636897231403470608L;

	private ThreadRow threadRow;
	private String follower;
	private String folowerParameters;
	
	public ThreadRowFollower(){
	}
	
	public ThreadRowFollower(ThreadRow threadRow, String follower) {
		this.threadRow = threadRow;
		this.follower = follower;
	}
	
	public String getFollower() {
		return follower;
	}
	
	public void setFollower(String follower) {
		this.follower = follower;
	}

	public ThreadRow getThreadRow() {
		return threadRow;
	}

	public void setThreadRow(ThreadRow threadRow) {
		this.threadRow = threadRow;
	}

	public String getFolowerParameters() {
		return folowerParameters;
	}

	public void setFolowerParameters(String folowerParameters) {
		this.folowerParameters = folowerParameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((threadRow == null) ? 0 : threadRow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThreadRowFollower other = (ThreadRowFollower) obj;
		if (follower == null) {
			if (other.follower != null)
				return false;
		} else if (!follower.equals(other.follower))
			return false;
		if (threadRow == null) {
			if (other.threadRow != null)
				return false;
		} else if (!threadRow.equals(other.threadRow))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRowFollower [follower=").append(follower)
				.append("]");
		return builder.toString();
	}

}
