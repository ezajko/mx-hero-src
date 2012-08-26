package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * The Class ItemResponse.
 */
public class ItemResponse extends AbstractResponse{
	
	/** The item_collection. */
	private ItemCollection item_collection;

	/**
	 * Gets the item_collection.
	 *
	 * @return the item_collection
	 */
	public ItemCollection getItem_collection() {
		return item_collection;
	}

	/**
	 * Sets the item_collection.
	 *
	 * @param item_collection the new item_collection
	 */
	public void setItem_collection(ItemCollection item_collection) {
		this.item_collection = item_collection;
	}

	/**
	 * Gets the mx hero folder.
	 *
	 * @return the mx hero folder
	 */
	public Item getMxHeroFolder() {
		if(getItem_collection()==null)return null;
		return getItem_collection().getMxHeroFolder();
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}


}
