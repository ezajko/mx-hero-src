package org.mxhero.engine.domain.mail.business;

import java.util.Collection;

/**
 * Represents a group of users so it can be used in rules.
 * @author mmarmol
 */
public class Group {

	private String name;
	
	private Collection<String> mails;
	
	private Collection<String> aliases;

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
	
}
