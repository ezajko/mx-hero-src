package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedContractRepository implements Runnable{
	
	private static Logger log = LoggerFactory.getLogger(CachedContractRepository.class);
	
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	private Long syncTimeInMinutes = 60l;
	private Long updateTime = 10000l;
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		log.debug("loaded");
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
		persist(false);
	}

	@Override
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		long lastReload = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastReload+(syncTimeInMinutes*60*1000)-System.currentTimeMillis()<0){
				persist(true);
				lastUpdate=System.currentTimeMillis();
				lastReload=System.currentTimeMillis();
			}else if(lastUpdate+updateTime-System.currentTimeMillis()<0){
				persist(false);
				lastUpdate=System.currentTimeMillis();
			}
		}
	}
	
	private void persist(boolean reaload){
		
	}

	public Long getSyncTimeInMinutes() {
		return syncTimeInMinutes;
	}

	public void setSyncTimeInMinutes(Long syncTimeInMinutes) {
		this.syncTimeInMinutes = syncTimeInMinutes;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}
