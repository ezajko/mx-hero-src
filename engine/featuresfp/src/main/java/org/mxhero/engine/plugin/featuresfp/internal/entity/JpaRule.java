package org.mxhero.engine.plugin.featuresfp.internal.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.mxhero.engine.domain.feature.Rule;
/*CHECK SECONDARY TABLE MAPPING*/
@Entity
@Table(name="features_rules")
public class JpaRule implements Rule {

	@Id
	private Integer id;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
	@JoinColumn(name="domain_id",nullable=true)
	private JpaDomain domainEntity;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="from_direction_id")
	private JpaRuleDirection fromDirection;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	@JoinColumn(name="to_direction_id")
	private JpaRuleDirection toDirection;

	@Column(name="admin_order",  length=20)
	private String adminOrder;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
	@JoinColumn(name="feature_id",nullable=false)
	private JpaFeature feature;
	
	@OneToMany(mappedBy="rule", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<JpaRuleProperty> properties;

	@Column(name="enabled", nullable=false)
	private Boolean enabled;

	@Column(name="two_ways", nullable=false)
	private Boolean twoWays;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDomain() {
		if (domainEntity!=null){
			return domainEntity.getDomain();
		}
		return null;
	}

	public JpaRuleDirection getFromDirection() {
		return fromDirection;
	}

	public void setFromDirection(JpaRuleDirection fromDirection) {
		this.fromDirection = fromDirection;
	}

	public JpaRuleDirection getToDirection() {
		return toDirection;
	}

	public void setToDirection(JpaRuleDirection toDirection) {
		this.toDirection = toDirection;
	}

	public String getAdminOrder() {
		return adminOrder;
	}

	public void setAdminOrder(String adminOrder) {
		this.adminOrder = adminOrder;
	}

	public Set<JpaRuleProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<JpaRuleProperty> properties) {
		this.properties = properties;
	}

	public JpaFeature getFeature() {
		return feature;
	}

	public void setFeature(JpaFeature feature) {
		this.feature = feature;
	}

	public JpaDomain getDomainEntity() {
		return domainEntity;
	}

	public void setDomainEntity(JpaDomain domainEntity) {
		this.domainEntity = domainEntity;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getTwoWays() {
		return twoWays;
	}

	public void setTwoWays(Boolean twoWays) {
		this.twoWays = twoWays;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((domainEntity == null) ? 0 : domainEntity.hashCode());
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
		JpaRule other = (JpaRule) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
