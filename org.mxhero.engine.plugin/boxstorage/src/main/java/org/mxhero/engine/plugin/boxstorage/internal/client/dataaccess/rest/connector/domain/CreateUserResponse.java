package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * The Class CreateUserResponse.
 */
public class CreateUserResponse extends AbstractResponse{
	
	/** The type. */
	private String type;
	
	/** The token. */
	private String token;
	
	/** The item. */
	private Item item;
	
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
	
	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Sets the item.
	 *
	 * @param item the new item
	 */
	public void setItem(Item item) {
		this.item = item;
	}
	
	/**
	 * Already exist.
	 *
	 * @return true, if successful
	 */
	public boolean alreadyExist() {
		return CodeResponse.USER_ALREADY_EXIST.equals(getResponse());
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}


}
