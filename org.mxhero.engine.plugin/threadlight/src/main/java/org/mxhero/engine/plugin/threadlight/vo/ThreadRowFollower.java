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
