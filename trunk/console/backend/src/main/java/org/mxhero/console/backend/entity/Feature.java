package org.mxhero.console.backend.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="features",
		uniqueConstraints={@UniqueConstraint(columnNames={"component","version"})})
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name="label_key", nullable=false, length=50)
	private String labelKey;
	
	@Column(name="description_key", nullable=false, length=50)
	private String descriptionKey;
	
	@Column(name="component", nullable=false, length=100)
	private String component;
	
	@Column(name="version", nullable=false)
	private Integer version;
	
	@Column(name="explain_key", length=50)
	private String explainKey;
	
	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name="category_id")
	private Category category;
	
	@Column(name="module_url", nullable=false, length=100)
	private String moduleUrl;
	
	@Column(name="module_report_url", nullable=false, length=100)
	private String moduleReportUrl;
	
	@Column(name="base_priority", nullable=false)
	private Long basePpriority;
	
	@Column(name="default_admin_order",  length=20)
	private String defaultAdminOrder;
	
	@OneToMany(mappedBy="feature", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<FeatureRule> rules;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}

	public void setDescriptionKey(String descriptionKey) {
		this.descriptionKey = descriptionKey;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<FeatureRule> getRules() {
		return rules;
	}

	public void setRules(Set<FeatureRule> rules) {
		this.rules = rules;
	}
	
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getExplainKey() {
		return explainKey;
	}

	public void setExplainKey(String explainKey) {
		this.explainKey = explainKey;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getBasePpriority() {
		return basePpriority;
	}

	public void setBasePpriority(Long basePpriority) {
		this.basePpriority = basePpriority;
	}

	public String getModuleReportUrl() {
		return moduleReportUrl;
	}

	public void setModuleReportUrl(String moduleReportUrl) {
		this.moduleReportUrl = moduleReportUrl;
	}
	
	public String getDefaultAdminOrder() {
		return defaultAdminOrder;
	}

	public void setDefaultAdminOrder(String defaultAdminOrder) {
		this.defaultAdminOrder = defaultAdminOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
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
		Feature other = (Feature) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
