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
	
}
