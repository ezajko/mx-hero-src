package org.mxhero.engine.plugin.threadlight.internal;

public class ThreadLightConfig {
	
	private Long updateTime = 10000l;
	private Long syncTimeInMinutes = 60l;
	private Integer sinceInDays = 30;
	
	
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public Long getSyncTimeInMinutes() {
		return syncTimeInMinutes;
	}
	public void setSyncTimeInMinutes(Long syncTimeInMinutes) {
		this.syncTimeInMinutes = syncTimeInMinutes;
	}
	public Integer getSinceInDays() {
		return sinceInDays;
	}
	public void setSinceInDays(Integer sinceInDays) {
		this.sinceInDays = sinceInDays;
	}
	
	
}
