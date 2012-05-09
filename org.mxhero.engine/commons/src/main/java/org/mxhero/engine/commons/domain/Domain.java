package org.mxhero.engine.commons.domain;

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
	 * @param alias
	 * @return
	 */
	public boolean hasAlias(String alias){
		if(this.getAliases()==null){
			return this.getId().equalsIgnoreCase(alias);
		}
		return this.getAliases().contains(alias.toLowerCase());
	}
	
	/**
	 * @param aliases
	 * @return
	 */
	public boolean hasAlias(String[] aliases){
		if(this.getAliases()==null){
			for(String alias : aliases){
				if(this.getId().equalsIgnoreCase(alias)){
					return true;
				}
			}
		}else{
			for(String alias : aliases){
				if(this.getAliases().contains(alias.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param aliases
	 * @return
	 */
	public boolean hasAlias(Collection<String> aliases){
		if(this.getAliases()==null){
			for(String alias : aliases){
				if(this.getId().equalsIgnoreCase(alias)){
					return true;
				}
			}
		}else{
			for(String alias : aliases){
				if(this.getAliases().contains(alias.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Domain other = (Domain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Domain [id=").append(id).append(", managed=")
				.append(managed).append(", aliases=").append(aliases)
				.append("]");
		return builder.toString();
	}

}
