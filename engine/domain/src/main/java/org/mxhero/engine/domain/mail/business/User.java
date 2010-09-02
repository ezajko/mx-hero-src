package org.mxhero.engine.domain.mail.business;

import java.util.Collection;

/**
 * Represents a user inside the platform so it can be used inside rules.
 * @author mmarmol
 */
public class User {

	private String mail;
	
	private Collection<UserList> lists;
	
	private Collection<String> aliases;
	

	/**
	 * @return
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param name
	 */
	public void setMail(String name) {
		this.mail = name;
	}

	/**
	 * @return
	 */
	public Collection<UserList> getLists() {
		return lists;
	}

	/**
	 * @param lists
	 */
	public void setLists(Collection<UserList> lists) {
		this.lists = lists;
	}

	/**
	 * @return
	 */
	public Collection<String> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases
	 */
	public void setAliases(Collection<String> aliases) {
		this.aliases = aliases;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [mail=").append(mail).append(", aliases=").append(
				aliases).append("]");
		return builder.toString();
	}
	
}
