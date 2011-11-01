package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Represents a Domain list so it can be used in rules.
 * @author mmarmol
 */
public class DomainList {

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
		builder.append("DomainList [name=").append(name).append(", mails=")
				.append(mails).append("]");
		return builder.toString();
	}
	
}
