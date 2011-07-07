package org.mxhero.engine.mqueues.internal.queue.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author mmarmol
 */
@Embeddable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RecordPropertyPk implements Serializable{

	private static final long serialVersionUID = -5940612862055946600L;

	@Column(name="property_key", length=80, nullable=false)
	private String key;
	
	private RecordPk recordId;

	public RecordPropertyPk(){
		
	}
	
	public RecordPropertyPk(String key, RecordPk recordId) {
		super();
		this.key = key;
		this.recordId = recordId;
	}

	/**
	 * @return the recordId
	 */
	public RecordPk getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(RecordPk recordId) {
		this.recordId = recordId;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((recordId == null) ? 0 : recordId.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (!(obj instanceof RecordPropertyPk)) {
			return false;
		}
		RecordPropertyPk other = (RecordPropertyPk) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (recordId == null) {
			if (other.recordId != null) {
				return false;
			}
		} else if (!recordId.equals(other.recordId)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatPk [key=").append(key).append(", recordId=")
				.append(recordId).append("]");
		return builder.toString();
	}
	
}
