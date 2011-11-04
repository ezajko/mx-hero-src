package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.Map;

import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;

public class CachedUserRepository implements UserRepository, Runnable{

	private UserRepository repository;
	
	private Map<String, User> users;
	
	private Map<String, Domain> domains;

	private Long updateTime = 60000l;
	
	private static final long CHECK_TIME = 1000;
	
	private Thread thread;
	
	private boolean keepWorking = false;

	@Override
	public Map<String, User> getUsers() {
		synchronized (this) {
			return users;
		}
	}

	@Override
	public Map<String, Domain> getDomains() {
		synchronized (this) {
			return domains;
		}
	}
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		//First time so it starts with real data.
		update();
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}

	@Override
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastUpdate+updateTime-System.currentTimeMillis()<0){
				update();
				lastUpdate=System.currentTimeMillis();
			}
		}
	}
	
	public void update(){
		Map<String, Domain> newDomains = repository.getDomains();
		Map<String, User> newUsers = repository.getUsers();
		synchronized (this) {
			domains=newDomains;
			users=newUsers;
		}
	}

	public UserRepository getRepository() {
		return repository;
	}

	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}

