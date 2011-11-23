package org.mxhero.console.backend.vo;

public class EmailAccountAliasVO {

	private String name;
	
	private String domain;

	private String dataSource;
	
	private EmailAccountVO account;
	
	public EmailAccountAliasVO(){
	}
	
	public EmailAccountAliasVO(String name, String domain, String dataSource,EmailAccountVO account) {
		super();
		this.name = name;
		this.domain = domain;
		this.dataSource=dataSource;
		this.account=account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public EmailAccountVO getAccount() {
		return account;
	}

	public void setAccount(EmailAccountVO account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		EmailAccountAliasVO other = (EmailAccountAliasVO) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
