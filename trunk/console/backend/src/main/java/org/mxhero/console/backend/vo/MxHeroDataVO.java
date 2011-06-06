package org.mxhero.console.backend.vo;

public class MxHeroDataVO {

	private Long domains;
	
	private Long accounts;
	
	private Long groups;
	
	private Long disableRules;
	
	private Long enabledRules;

	public Long getDomains() {
		return domains;
	}

	public void setDomains(Long domains) {
		this.domains = domains;
	}

	public Long getAccounts() {
		return accounts;
	}

	public void setAccounts(Long accounts) {
		this.accounts = accounts;
	}

	public Long getGroups() {
		return groups;
	}

	public void setGroups(Long groups) {
		this.groups = groups;
	}

	public Long getDisableRules() {
		return disableRules;
	}

	public void setDisableRules(Long disableRules) {
		this.disableRules = disableRules;
	}

	public Long getEnabledRules() {
		return enabledRules;
	}

	public void setEnabledRules(Long enabledRules) {
		this.enabledRules = enabledRules;
	}

}
