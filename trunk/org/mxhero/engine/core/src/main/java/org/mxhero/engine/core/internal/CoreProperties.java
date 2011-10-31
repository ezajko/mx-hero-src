package org.mxhero.engine.core.internal;

public class CoreProperties {

	private Long resourceScannerInterval = 60000l;
	private String errorSuffix = "core";
	private String errorPrefix = ".eml";
	private String errorFolder = System.getProperty("java.io.tmpdir");
	private String topGroupId = "top";
	private String bottomGroupId = "bottom";
	private String processErrorStat = "CORE_PROCESS_ERR";
	private String connectorErrorStat = "CORE_CONCT_ERR";
	private String connectorNotFoundValue = "CONNECTOR_NOT_FOUND";
	private Long queueDelayTime = 10000l;
	private Integer queueRetries = 5;
	private String deliveredErrorPath = System.getProperty("java.io.tmpdir");

	public Long getResourceScannerInterval() {
		return resourceScannerInterval;
	}

	public void setResourceScannerInterval(Long resourceScannerInterval) {
		if(resourceScannerInterval!=null && resourceScannerInterval > 30000){
			this.resourceScannerInterval = resourceScannerInterval;
		}
	}

	public String getErrorSuffix() {
		return errorSuffix;
	}

	public void setErrorSuffix(String errorSuffix) {
		if(errorSuffix!=null && !errorSuffix.isEmpty()){
			this.errorSuffix = errorSuffix;
		}
	}

	public String getErrorPrefix() {
		return errorPrefix;
	}

	public void setErrorPrefix(String errorPrefix) {
		if(errorPrefix!=null && !errorPrefix.isEmpty()){
			this.errorPrefix = errorPrefix;
		}
	}

	public String getErrorFolder() {
		return errorFolder;
	}

	public void setErrorFolder(String errorFolder) {
		if(errorFolder!=null && !errorFolder.isEmpty()){
			this.errorFolder = errorFolder;
		}
	}

	public String getTopGroupId() {
		return topGroupId;
	}

	public void setTopGroupId(String topGroupId) {
		if(topGroupId!=null && !topGroupId.isEmpty()){
			this.topGroupId = topGroupId;
		}
	}

	public String getBottomGroupId() {
		return bottomGroupId;
	}

	public void setBottomGroupId(String bottomGroupId) {
		if(bottomGroupId!=null && !bottomGroupId.isEmpty()){
			this.bottomGroupId = bottomGroupId;
		}
	}

	public String getProcessErrorStat() {
		return processErrorStat;
	}

	public void setProcessErrorStat(String processErrorStat) {
		if(processErrorStat!=null && !processErrorStat.isEmpty()){
			this.processErrorStat = processErrorStat;
		}
	}

	public String getConnectorErrorStat() {
		return connectorErrorStat;
	}

	public void setConnectorErrorStat(String connectorErrorStat) {
		if(connectorErrorStat!=null && !connectorErrorStat.isEmpty()){
			this.connectorErrorStat = connectorErrorStat;
		}
	}

	public String getConnectorNotFoundValue() {
		return connectorNotFoundValue;
	}

	public void setConnectorNotFoundValue(String connectorNotFoundValue) {
		if(connectorNotFoundValue!=null && !connectorNotFoundValue.isEmpty()){
			this.connectorNotFoundValue = connectorNotFoundValue;
		}
	}

	public Long getQueueDelayTime() {
		return queueDelayTime;
	}

	public void setQueueDelayTime(Long queueDelayTime) {
		if(queueDelayTime!=null && queueDelayTime >1000){
			this.queueDelayTime = queueDelayTime;
		}
	}

	public Integer getQueueRetries() {
		return queueRetries;
	}

	public void setQueueRetries(Integer queueRetries) {
		if(queueRetries!=null && queueRetries >-1){
			this.queueRetries = queueRetries;
		}
	}

	public String getDeliveredErrorPath() {
		return deliveredErrorPath;
	}

	public void setDeliveredErrorPath(String deliveredErrorPath) {
		if(deliveredErrorPath!=null && !deliveredErrorPath.isEmpty()){
			this.deliveredErrorPath = deliveredErrorPath;
		}
	}

}
