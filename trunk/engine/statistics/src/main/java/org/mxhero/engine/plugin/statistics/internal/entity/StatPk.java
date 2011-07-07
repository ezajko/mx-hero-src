package org.mxhero.engine.plugin.statistics.internal.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author mmarmol
 */
@Embeddable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class StatPk implements Serializable{

	private static final long serialVersionUID = 3512218688548304886L;

	@Column(name="stat_key", length=80, nullable=false)
	private String key;
	
	@Column(name="insert_date")
	private Timestamp insertDate;
	
	@Column(name="record_sequence")
	private Long sequence;
	
	private String phase;

	public Timestamp getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
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
		StatPk other = (StatPk) obj;
		if (insertDate == null) {
			if (other.insertDate != null)
				return false;
		} else if (!insertDate.equals(other.insertDate))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}

}
