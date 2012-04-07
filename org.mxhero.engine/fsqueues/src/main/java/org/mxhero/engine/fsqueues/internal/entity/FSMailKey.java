package org.mxhero.engine.fsqueues.internal.entity;

import java.sql.Timestamp;

/**
 * @author mmarmol
 *
 */
public class FSMailKey {

	private Long sequence;

	private Timestamp time;
	
	/**
	 * @param sequence
	 * @param time
	 */
	public FSMailKey(Long sequence, Timestamp time) {
		this.sequence = sequence;
		this.time = time;
	}

	/**
	 * @return
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		FSMailKey other = (FSMailKey) obj;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FSMailKey [sequence=").append(sequence)
				.append(", time=").append(time).append("]");
		return builder.toString();
	}
	
}
