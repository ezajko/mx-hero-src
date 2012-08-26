package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class Entry.
 */
public class Entry {

	/** The type. */
	private String type;
	
	/** The id. */
	private String id;
	
	/** The shared_link. */
	private SharedLink shared_link;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the shared_link.
	 *
	 * @return the shared_link
	 */
	public SharedLink getShared_link() {
		return shared_link;
	}

	/**
	 * Sets the shared_link.
	 *
	 * @param shared_link the new shared_link
	 */
	public void setShared_link(SharedLink shared_link) {
		this.shared_link = shared_link;
	}

	
}
