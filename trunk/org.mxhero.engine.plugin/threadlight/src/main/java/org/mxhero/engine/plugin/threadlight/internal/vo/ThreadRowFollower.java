package org.mxhero.engine.plugin.threadlight.internal.vo;

public class ThreadRowFollower {

	private Long threadRowId;
	private String follower;
	
	public Long getThreadRowId() {
		return threadRowId;
	}
	
	public void setThreadRowId(Long threadRowId) {
		this.threadRowId = threadRowId;
	}
	
	public String getFollower() {
		return follower;
	}
	
	public void setFollower(String follower) {
		this.follower = follower;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((threadRowId == null) ? 0 : threadRowId.hashCode());
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
		if (threadRowId == null) {
			if (other.threadRowId != null)
				return false;
		} else if (!threadRowId.equals(other.threadRowId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRowFollower [threadRowId=").append(threadRowId)
				.append(", follower=").append(follower).append("]");
		return builder.toString();
	}

}
