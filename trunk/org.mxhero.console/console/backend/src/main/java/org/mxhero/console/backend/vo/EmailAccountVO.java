package org.mxhero.console.backend.vo;

import java.util.Calendar;
import java.util.Collection;

public class EmailAccountVO {

	private String account;
	
	private String domain;
	
	private Calendar createdDate;
	
	private Calendar updatedDate;
	
	private String group;
	
	private Collection<EmailAccountAliasVO> aliases;
	
	private String dataSource;
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Collection<EmailAccountAliasVO> getAliases() {
		return aliases;
	}

	public void setAliases(Collection<EmailAccountAliasVO> aliases) {
		this.aliases = aliases;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

}
