package org.mxhero.engine.mqueues.internal.queue.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author mmarmol
 */
@Embeddable
public class RecordPk implements Serializable{

	private static final long serialVersionUID = 7754450603404771614L;

	@Column(name="insert_date")
	private Timestamp insertDate;
	
	@Column(name="record_sequence")
	private Long sequence;
	
	/**
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the insertDate
	 */
	public Timestamp getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RecordPk)) {
			return false;
		}
		RecordPk other = (RecordPk) obj;
		if (insertDate == null) {
			if (other.insertDate != null) {
				return false;
			}
		} else if (!insertDate.equals(other.insertDate)) {
			return false;
		}
		if (sequence == null) {
			if (other.sequence != null) {
				return false;
			}
		} else if (!sequence.equals(other.sequence)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordPk [insertDate=").append(insertDate).append(
				", sequence=").append(sequence).append("]");
		return builder.toString();
	}

}
