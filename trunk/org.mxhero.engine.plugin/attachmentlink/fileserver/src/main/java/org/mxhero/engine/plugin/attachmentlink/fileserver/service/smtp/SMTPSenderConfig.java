package org.mxhero.engine.plugin.attachmentlink.fileserver.service.smtp;

public class SMTPSenderConfig {

	private String host;
	private Integer port;
	private String adminMail;
	private Boolean auth;
	private Boolean ssl;
	private String user;
	private String password;
	private String signature;
	private String signaturePlain;
	private String externalUrl;
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getAdminMail() {
		return adminMail;
	}
	
	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}
	
	public Boolean getAuth() {
		return auth;
	}
	
	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	public Boolean getSsl() {
		return ssl;
	}

	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getSignaturePlain() {
		return signaturePlain;
	}

	public void setSignaturePlain(String signaturePlain) {
		this.signaturePlain = signaturePlain;
	}

}
