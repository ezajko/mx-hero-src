package org.mxhero.engine.domain.mail.business;

import java.util.Collection;

/**
 * Represents the Domain of a mail so it can be used in rules.
 * @author mmarmol
 *
 */
public class Domain {

	private String id;
	
	private Boolean managed;
	
	private Collection<String> aliases;
	
	private Collection<DomainList> lists;
	
	private Collection<Group> groups;

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the lists
	 */
	public Collection<DomainList> getLists() {
		return lists;
	}

	/**
	 * @param lists the lists to set
	 */
	public void setLists(Collection<DomainList> lists) {
		this.lists = lists;
	}

	/**
	 * @return
	 */
	public Collection<Group> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 */
	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}

	/**
	 * @return
	 */
	public Boolean getManaged() {
		return managed;
	}

	/**
	 * @param managed
	 */
	public void setManaged(Boolean managed) {
		this.managed = managed;
	}

	/** 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Domain [id=").append(id).append(", managed=")
				.append(managed).append(", aliases=").append(aliases)
				.append(", lists=").append(lists).append(", groups=")
				.append(groups).append("]");
		return builder.toString();
	}	

}
