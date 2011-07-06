package org.mxhero.console.backend.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="groups",schema="mxhero")
public class Group {
	
	@EmbeddedId
	private GroupPk id;
	
	@Column(name="description", length=200)
	private String description;
	
	@OneToMany(mappedBy="group", cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private Set<EmailAccount> members;

	@MapsId("domainId")
	@ManyToOne(fetch=FetchType.EAGER,optional=false,cascade= {CascadeType.MERGE})
	@JoinColumns({
		@JoinColumn(name="domain_id", referencedColumnName="domain")
	})
	private Domain domain;
	
	@Column(name="created", nullable=false)
	private Calendar createdDate;
	
	@Column(name="updated", nullable=false)
	private Calendar updatedDate;

	public GroupPk getId() {
		return id;
	}

	public void setId(GroupPk id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<EmailAccount> getMembers() {
		return members;
	}

	public void setMembers(Set<EmailAccount> members) {
		this.members = members;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Calendar getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Calendar updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.getDomainId().hashCode());
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
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
