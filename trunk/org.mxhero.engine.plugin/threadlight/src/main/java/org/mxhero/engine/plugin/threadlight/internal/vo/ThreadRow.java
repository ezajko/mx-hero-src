package org.mxhero.engine.plugin.threadlight.internal.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

public class ThreadRow implements Serializable{

	private static final long serialVersionUID = 5579718318550125496L;
	
	private Long id;
	private ThreadRowPk pk;
	private Timestamp creationTime;
	private String subject;
	private Timestamp replyTime;
	private Set<ThreadRowFollower> followers;
	private Timestamp snoozeTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ThreadRowPk getPk() {
		return pk;
	}

	public void setPk(ThreadRowPk pk) {
		this.pk = pk;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Timestamp getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Timestamp replyTime) {
		this.replyTime = replyTime;
	}

	public Set<ThreadRowFollower> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<ThreadRowFollower> followers) {
		this.followers = followers;
	}

	public Timestamp getSnoozeTime() {
		return snoozeTime;
	}

	public void setSnoozeTime(Timestamp snoozeTime) {
		this.snoozeTime = snoozeTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
		ThreadRow other = (ThreadRow) obj;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThreadRow [id=").append(id).append(", pk=").append(pk)
				.append(", creationTime=").append(creationTime)
				.append(", subject=").append(subject).append(", replyTime=")
				.append(replyTime).append(", followers=").append(followers)
				.append(", snoozeTime=").append(snoozeTime).append("]");
		return builder.toString();
	}

}
