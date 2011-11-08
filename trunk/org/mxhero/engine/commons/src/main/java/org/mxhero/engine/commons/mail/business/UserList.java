package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Representation of a user list so it can be used inside rules.
 * @author mmarmol
 */
public class UserList {

	private String name;
	
	private Collection<String> mails;

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Collection<String> getMails() {
		return mails;
	}

	/**
	 * @param mails
	 */
	public void setMails(Collection<String> mails) {
		this.mails = mails;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserList [name=").append(name).append(", mails=")
				.append(mails).append("]");
		return builder.toString();
	}

}