package org.mxhero.engine.plugin.postfixconnector.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author mmarmol
 *
 */
public class ConnectorProperties {

	public final static String RET_HEADER="X-mxHero-Dsn-Ret";
	public final static String NOTIFY_HEADER="X-mxHero-Dsn-Notify";
	public final static String ID_HEADER="X-mxHero-Id";
	
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
	private static ConnectorProperties instance=null;

	/**
	 * 
	 */
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

	/**
	 * 
	 */
	public void init(){
		instance=this;
	}
	
	/**
	 * @return
	 */
	public static ConnectorProperties getInstance(){
		return instance;
	}
	
	/**
	 * @return
	 */
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	/**
	 * @param mailSmtpHost
	 */
	public void setMailSmtpHost(String mailSmtpHost) {
		if(mailSmtpHost==null || mailSmtpHost.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.mailSmtpHost = mailSmtpHost;
	}

	/**
	 * @return
	 */
	public Integer getMailSmtpPort() {
		return mailSmtpPort;
	}

	/**
	 * @param mailSmtpPort
	 */
	public void setMailSmtpPort(Integer mailSmtpPort) {
		if(mailSmtpPort==null || mailSmtpPort<1){
			throw new IllegalArgumentException();
		}
		this.mailSmtpPort = mailSmtpPort;
	}

	/**
	 * @return
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * @param smtpHost
	 */
	public void setSmtpHost(String smtpHost) {
		if(smtpHost==null || smtpHost.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.smtpHost = smtpHost;
	}

	/**
	 * @return
	 */
	public Integer getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort
	 */
	public void setSmtpPort(Integer smtpPort) {
		if(smtpPort==null || smtpPort<1){
			throw new IllegalArgumentException();
		}
		this.smtpPort = smtpPort;
	}

	/**
	 * @return
	 */
	public Integer getDeferredSize() {
		return deferredSize;
	}

	/**
	 * @param deferredSize
	 */
	public void setDeferredSize(Integer deferredSize) {
		if(deferredSize==null || deferredSize<0){
			throw new IllegalArgumentException();
		}
		this.deferredSize = deferredSize;
	}

	/**
	 * @return
	 */
	public Integer getReceiveBufferSize() {
		return receiveBufferSize;
	}

	/**
	 * @param receiveBufferSize
	 */
	public void setReceiveBufferSize(Integer receiveBufferSize) {
		if(receiveBufferSize==null || receiveBufferSize<1024){
			throw new IllegalArgumentException();
		}
		this.receiveBufferSize = receiveBufferSize;
	}

	/**
	 * @return
	 */
	public Long getMessageMaxSize() {
		return messageMaxSize;
	}

	/**
	 * @param messageMaxSize
	 */
	public void setMessageMaxSize(Long messageMaxSize) {
		if(messageMaxSize==null || messageMaxSize<1024*1024){
			throw new IllegalArgumentException();
		}
		this.messageMaxSize = messageMaxSize;
	}

	/**
	 * @return
	 */
	public String getErrorSuffix() {
		return errorSuffix;
	}

	/**
	 * @param errorSuffix
	 */
	public void setErrorSuffix(String errorSuffix) {
		if(errorSuffix==null || errorSuffix.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorSuffix = errorSuffix;
	}

	/**
	 * @return
	 */
	public String getErrorPrefix() {
		return errorPrefix;
	}

	/**
	 * @param errorPrefix
	 */
	public void setErrorPrefix(String errorPrefix) {
		if(errorPrefix==null || errorPrefix.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorPrefix = errorPrefix;
	}

	/**
	 * @return
	 */
	public String getErrorFolder() {
		return errorFolder;
	}

	/**
	 * @param errorFolder
	 */
	public void setErrorFolder(String errorFolder) {
		if(errorFolder==null || errorFolder.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.errorFolder = errorFolder;
	}

	/**
	 * @return
	 */
	public Integer getMaxConnections() {
		return maxConnections;
	}

	/**
	 * @param maxConnections
	 */
	public void setMaxConnections(Integer maxConnections) {
		if(maxConnections==null || maxConnections<1){
			throw new IllegalArgumentException();
		}
		this.maxConnections = maxConnections;
	}

	/**
	 * @return
	 */
	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(Integer connectionTimeout) {
		if(connectionTimeout==null || connectionTimeout<1000){
			throw new IllegalArgumentException();
		}
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostname
	 */
	public void setHostName(String hostname) {
		if(hostname==null || hostname.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.hostName = hostname;
	}

	/**
	 * @return
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 */
	public void setCharset(String charset) {
		if(charset==null || charset.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.charset = charset;
	}

	/**
	 * @return
	 */
	public Integer getMaxRecipients() {
		return maxRecipients;
	}

	/**
	 * @param maxRecipients
	 */
	public void setMaxRecipients(Integer maxRecipients) {
		if(maxRecipients==null || maxRecipients<1){
			throw new IllegalArgumentException();
		}
		this.maxRecipients = maxRecipients;
	}

}
