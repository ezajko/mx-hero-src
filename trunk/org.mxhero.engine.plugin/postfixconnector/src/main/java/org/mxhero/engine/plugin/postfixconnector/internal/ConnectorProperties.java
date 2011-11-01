package org.mxhero.engine.plugin.postfixconnector.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectorProperties {

	private String mailSmtpHost = "127.0.0.1";
	private Integer mailSmtpPort = 5555;
	private String smtpHost = "127.0.0.1";
	private Integer smtpPort = 5556;
	private Integer deferredSize = 1048576;
	private Integer receiveBufferSize = 16384;
	private Long messageMaxSize = 200971520l;
	private String errorSuffix = ".eml";
	private String errorPrefix = "pfxc";
	private String errorFolder = "/tmp";
	private Integer maxConnections = 100;
	private Integer connectionTimeout = 60*1000;
	private String hostName = "localhost";
	private String charset = "UTF-8";
	private Integer maxRecipients = 100;

	
	public ConnectorProperties() {
		try
		{
			this.hostName = InetAddress.getLocalHost().getCanonicalHostName();
		}
		catch (UnknownHostException e)
		{
			this.hostName = "localhost";
		}
	}

	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	public void setMailSmtpHost(String mailSmtpHost) {
		if(mailSmtpHost==null || mailSmtpHost.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.mailSmtpHost = mailSmtpHost;
	}

	public Integer getMailSmtpPort() {
		return mailSmtpPort;
	}

	public void setMailSmtpPort(Integer mailSmtpPort) {
		if(mailSmtpPort==null || mailSmtpPort<1){
			throw new IllegalArgumentException();
		}
		this.mailSmtpPort = mailSmtpPort;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		if(smtpHost==null || smtpHost.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.smtpHost = smtpHost;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		if(smtpPort==null || smtpPort<1){
			throw new IllegalArgumentException();
		}
		this.smtpPort = smtpPort;
	}

	public Integer getDeferredSize() {
		return deferredSize;
	}

	public void setDeferredSize(Integer deferredSize) {
		if(deferredSize==null || deferredSize<0){
			throw new IllegalArgumentException();
		}
		this.deferredSize = deferredSize;
	}

	public Integer getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(Integer receiveBufferSize) {
		if(receiveBufferSize==null || receiveBufferSize<1024){
			throw new IllegalArgumentException();
		}
		this.receiveBufferSize = receiveBufferSize;
	}

	public Long getMessageMaxSize() {
		return messageMaxSize;
	}

	public void setMessageMaxSize(Long messageMaxSize) {
		if(messageMaxSize==null || messageMaxSize<1024*1024){
			throw new IllegalArgumentException();
		}
		this.messageMaxSize = messageMaxSize;
	}

	public String getErrorSuffix() {
		return errorSuffix;
	}

	public void setErrorSuffix(String errorSuffix) {
		if(errorSuffix==null || errorSuffix.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorSuffix = errorSuffix;
	}

	public String getErrorPrefix() {
		return errorPrefix;
	}

	public void setErrorPrefix(String errorPrefix) {
		if(errorPrefix==null || errorPrefix.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorPrefix = errorPrefix;
	}

	public String getErrorFolder() {
		return errorFolder;
	}

	public void setErrorFolder(String errorFolder) {
		if(errorFolder==null || errorFolder.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorFolder = errorFolder;
	}

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		if(maxConnections==null || maxConnections<1){
			throw new IllegalArgumentException();
		}
		this.maxConnections = maxConnections;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		if(connectionTimeout==null || connectionTimeout<1000){
			throw new IllegalArgumentException();
		}
		this.connectionTimeout = connectionTimeout;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostname) {
		if(hostname==null || hostname.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.hostName = hostname;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		if(charset==null || charset.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.charset = charset;
	}

	public Integer getMaxRecipients() {
		return maxRecipients;
	}

	public void setMaxRecipients(Integer maxRecipients) {
		if(maxRecipients==null || maxRecipients<1){
			throw new IllegalArgumentException();
		}
		this.maxRecipients = maxRecipients;
	}

}
