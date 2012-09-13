/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.core.internal;

import java.util.Map;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreProperties extends Observable {

	private static Logger log = LoggerFactory.getLogger(CoreProperties.class);
	
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
	private Integer maximumPoolSize = 10;
	private Long waitTime = 1000l;
	private Long keepAliveTime = 2000l;
	private Integer corePoolsize = 10;

	public Long getResourceScannerInterval() {
		return resourceScannerInterval;
	}

	public void setResourceScannerInterval(Long resourceScannerInterval) {
		if (resourceScannerInterval != null && resourceScannerInterval > 30000) {
			this.resourceScannerInterval = resourceScannerInterval;
		}
	}

	public String getErrorSuffix() {
		return errorSuffix;
	}

	public void setErrorSuffix(String errorSuffix) {
		if (errorSuffix != null && !errorSuffix.isEmpty()) {
			this.errorSuffix = errorSuffix;
		}
	}

	public String getErrorPrefix() {
		return errorPrefix;
	}

	public void setErrorPrefix(String errorPrefix) {
		if (errorPrefix != null && !errorPrefix.isEmpty()) {
			this.errorPrefix = errorPrefix;
		}
	}

	public String getErrorFolder() {
		return errorFolder;
	}

	public void setErrorFolder(String errorFolder) {
		if (errorFolder != null && !errorFolder.isEmpty()) {
			this.errorFolder = errorFolder;
		}
	}

	public String getTopGroupId() {
		return topGroupId;
	}

	public void setTopGroupId(String topGroupId) {
		if (topGroupId != null && !topGroupId.isEmpty()) {
			this.topGroupId = topGroupId;
		}
	}

	public String getBottomGroupId() {
		return bottomGroupId;
	}

	public void setBottomGroupId(String bottomGroupId) {
		if (bottomGroupId != null && !bottomGroupId.isEmpty()) {
			this.bottomGroupId = bottomGroupId;
		}
	}

	public String getProcessErrorStat() {
		return processErrorStat;
	}

	public void setProcessErrorStat(String processErrorStat) {
		if (processErrorStat != null && !processErrorStat.isEmpty()) {
			this.processErrorStat = processErrorStat;
		}
	}

	public String getConnectorErrorStat() {
		return connectorErrorStat;
	}

	public void setConnectorErrorStat(String connectorErrorStat) {
		if (connectorErrorStat != null && !connectorErrorStat.isEmpty()) {
			this.connectorErrorStat = connectorErrorStat;
		}
	}

	public String getConnectorNotFoundValue() {
		return connectorNotFoundValue;
	}

	public void setConnectorNotFoundValue(String connectorNotFoundValue) {
		if (connectorNotFoundValue != null && !connectorNotFoundValue.isEmpty()) {
			this.connectorNotFoundValue = connectorNotFoundValue;
		}
	}

	public Long getQueueDelayTime() {
		return queueDelayTime;
	}

	public void setQueueDelayTime(Long queueDelayTime) {
		if (queueDelayTime != null && queueDelayTime > 1000) {
			this.queueDelayTime = queueDelayTime;
		}
	}

	public Integer getQueueRetries() {
		return queueRetries;
	}

	public void setQueueRetries(Integer queueRetries) {
		if (queueRetries != null && queueRetries > -1) {
			this.queueRetries = queueRetries;
		}
	}

	public String getDeliveredErrorPath() {
		return deliveredErrorPath;
	}

	public void setDeliveredErrorPath(String deliveredErrorPath) {
		if (deliveredErrorPath != null && !deliveredErrorPath.isEmpty()) {
			this.deliveredErrorPath = deliveredErrorPath;
		}
	}

	public Integer getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(Integer maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public Long getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public Integer getCorePoolsize() {
		return corePoolsize;
	}

	public void setCorePoolsize(Integer corePoolsize) {
		this.corePoolsize = corePoolsize;
	}

	public void updateCallback(Map<String, ?> properties) {
		this.setChanged();
		this.notifyObservers();
		log.debug("UPDATED has "+this.countObservers());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CoreProperties [resourceScannerInterval=")
				.append(resourceScannerInterval).append(", errorSuffix=")
				.append(errorSuffix).append(", errorPrefix=")
				.append(errorPrefix).append(", errorFolder=")
				.append(errorFolder).append(", topGroupId=").append(topGroupId)
				.append(", bottomGroupId=").append(bottomGroupId)
				.append(", processErrorStat=").append(processErrorStat)
				.append(", connectorErrorStat=").append(connectorErrorStat)
				.append(", connectorNotFoundValue=")
				.append(connectorNotFoundValue).append(", queueDelayTime=")
				.append(queueDelayTime).append(", queueRetries=")
				.append(queueRetries).append(", deliveredErrorPath=")
				.append(deliveredErrorPath).append("]");
		return builder.toString();
	}

}
