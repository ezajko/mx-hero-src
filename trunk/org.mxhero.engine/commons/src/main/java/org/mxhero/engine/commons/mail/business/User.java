package org.mxhero.engine.commons.mail.business;

import java.util.Collection;

/**
 * Represents a user inside the platform so it can be used inside rules.
 * @author mmarmol
 */
public class User {

	private String mail;

	private Collection<String> aliases;
	
	private Domain domain;
	
	private String group;
	
	private Boolean managed;

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

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		return result;
	}

	public boolean hasAlias(String email){
		if(this.getAliases()==null){
			if(this.getMail()!=null){
				this.getMail().equalsIgnoreCase(email);
			}
			return false;
		}
		return this.getAliases().contains(email);
	}
	
	public boolean hasAlias( String[] emails){
		if(this.getAliases()==null){
			for(String email : emails){
				if(this.getMail().equalsIgnoreCase(email)){
					return true;
				}
			}
			return false;
		}else{
			for(String email : emails){
				if(this.getAliases().contains(email)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasAlias(Collection<String> emails){
		if(this.getAliases()==null){
			for(String email : emails){
				if(this.getMail().equalsIgnoreCase(email)){
					return true;
				}
			}
			return false;
		}else{
			for(String email : emails){
				if(this.getAliases().contains(email)){
					return true;
				}
			}
		}
		return false;
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