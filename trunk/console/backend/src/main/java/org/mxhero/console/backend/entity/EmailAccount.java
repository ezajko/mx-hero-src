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
@Table(name="email_accounts",schema="mxhero")
public class EmailAccount {

	public static final String MANUAL="manual";
	
	@EmbeddedId
	private EmailAccountPk id;
	
	@Column(name="created", nullable=false)
	private Calendar createdDate;
	
	@Column(name="data_source", nullable=false)
	private String dataSource;
	
	@Column(name="updated", nullable=false)
	private Calendar updatedDate;
	
	@MapsId("domainId")
	@ManyToOne(fetch=FetchType.EAGER,optional=false,cascade= {CascadeType.MERGE})
	@JoinColumns({
		@JoinColumn(name="domain_id", referencedColumnName="domain")
	})
	private Domain domain;


	@ManyToOne(fetch=FetchType.EAGER,cascade= {CascadeType.MERGE})
	@JoinColumns({
		@JoinColumn(name="group_domain_id", referencedColumnName="domain_id"),
		@JoinColumn(name="group_name", referencedColumnName="name")
	})
	private Group group;
	
	@OneToMany(mappedBy="account", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<AliasAccount> aliases;
	
	public EmailAccountPk getId() {
		return id;
	}

	public void setId(EmailAccountPk id) {
		this.id = id;
	}
	
	public Set<AliasAccount> getAliases() {
		return aliases;
	}

	public void setAliases(Set<AliasAccount> aliases) {
		this.aliases = aliases;
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

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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
		EmailAccount other = (EmailAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
