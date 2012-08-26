package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The Class ItemCollection.
 */
public class ItemCollection {
	
	/** The entries. */
	private List<Item> entries;

	/**
	 * Gets the entries.
	 *
	 * @return the entries
	 */
	public List<Item> getEntries() {
		return entries;
	}

	/**
	 * Sets the entries.
	 *
	 * @param entries the new entries
	 */
	public void setEntries(List<Item> entries) {
		this.entries = entries;
	}

	/**
	 * Gets the mx hero folder.
	 *
	 * @return the mx hero folder
	 */
	public Item getMxHeroFolder() {
		if(getEntries()==null)return null;
		return (Item) CollectionUtils.find(getEntries(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Item item = (Item) arg0;
				return Pattern.matches("(?i)mxhero", item.getName());
			}
		});
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}


}
