package org.mxhero.engine.plugin.statistics.internal.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * Represents a stat for a record, a key can't be repeated for the same record.
 * @author mmarmol
 */
@Entity
@Table(name="mail_stats")
@Cacheable
public class Stat implements Serializable{

	private static final long serialVersionUID = 2157349611125482911L;

	@EmbeddedId
	private StatPk id;
	
	@MapsId("recordId")
	@ManyToOne(fetch=FetchType.EAGER,optional=false)
	@JoinColumns({
		@JoinColumn(name="insert_date", referencedColumnName="insert_date"),
		@JoinColumn(name="record_sequence", referencedColumnName="record_sequence")
	})
	private Record record;

	@Column(name="stat_value", length=1024)
	private String value;

	public Stat() {
	}

	/**
	 * @return the id
	 */
	public StatPk getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(StatPk id) {
		this.id = id;
	}
	
	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Stat)) {
			return false;
		}
		Stat other = (Stat) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
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
		builder.append("Stat [id=").append(id).append(", value=").append(value)
				.append("]");
		return builder.toString();
	}

}
