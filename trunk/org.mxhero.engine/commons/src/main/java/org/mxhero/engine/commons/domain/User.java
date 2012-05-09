package org.mxhero.engine.commons.domain;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a user inside the platform so it can be used inside rules.
 * @author mmarmol
 */
public class User {

	private String mail;

	private Collection<String> aliases;
	
	private Domain domain;
	
	private String group;
	
	private Collection<String> addressBook;
	
	private Boolean managed;
	
	private Map<String, String> properties;

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
	 * @return
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * @return
	 */
	public Collection<String> getAddressBook() {
		return addressBook;
	}

	/**
	 * @param addressBook
	 */
	public void setAddressBook(Collection<String> addressBook) {
		this.addressBook = addressBook;
	}
	
	/**
	 * @param emails
	 * @return
	 */
	public boolean hasAlias(String... emails){
		if(emails == null || emails.length<1){
			return false;
		}
		
		if(this.getAliases()==null){
			for(String email : emails){
				if(this.getMail().equalsIgnoreCase(email)){
					return true;
				}
			}
			return false;
		}else{
			for(String email : emails){
				if(this.getAliases().contains(email.toLowerCase())){
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * @param emails
	 * @return
	 */
	public boolean hasEmailOnAddressBook(String... emails){
		if(emails == null || emails.length<1){
			return false;
		}
		
		if(this.getAddressBook()!=null){
			for(String email : emails){
				if(this.getAddressBook().contains(email.toLowerCase())){
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * @return
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		return true;
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
