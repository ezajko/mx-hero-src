package org.mxhero.engine.plugin.threadlight.internal.vo;

import java.io.Serializable;

public class ThreadRowFollower implements Serializable{

	private static final long serialVersionUID = -5636897231403470608L;
	
	private Long threadRowId;
	private ThreadRowPk threadPk;
	private String follower;
	
	public ThreadRowFollower(){
	}
	
	public ThreadRowFollower(Long threadRowId, ThreadRowPk threadPk, String follower) {
		this.threadRowId = threadRowId;
		this.threadPk = threadPk;
		this.follower = follower;
	}

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

	public ThreadRowPk getThreadPk() {
		return threadPk;
	}

	public void setThreadPk(ThreadRowPk threadPk) {
		this.threadPk = threadPk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((threadPk == null) ? 0 : threadPk.hashCode());
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
		if (threadPk == null) {
			if (other.threadPk != null)
				return false;
		} else if (!threadPk.equals(other.threadPk))
			return false;
		return true;
	}
	


}
