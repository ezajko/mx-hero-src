package org.mxhero.engine.plugin.statistics.internal.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;

public class CachedRecordRepository implements RecordRepository, Runnable{

	private RecordRepository repository;
	
	private Set<Record> records;
	
	private Set<Stat> stats;
	
	private Long updateTime = 15000l;
	
	private static final long CHECK_TIME = 1000;
	
	private Thread thread;
	
	private boolean keepWorking = false;
	
	@Override
	public void saveRecord(Collection<Record> records) {
		synchronized (this) {
			this.records.removeAll(records);
			this.records.addAll(records);
		}
		
	}

	@Override
	public void saveRecord(Record record) {
		synchronized (this) {
			this.records.remove(record);
			this.records.add(record);
		}
		
	}

	@Override
	public void saveStat(Collection<Stat> stats) {
		synchronized (this) {
			this.stats.removeAll(stats);
			this.stats.addAll(stats);
		}
	}

	@Override
	public void saveStat(Stat stat) {
		synchronized (this) {
			this.stats.remove(stat);
			this.stats.add(stat);
		}
	}

	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		//First time so it starts with real data.
		persist();
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
				persist();
				lastUpdate=System.currentTimeMillis();
			}
		}
	}
	
	public void persist(){
		Set<Record> oldRecords=null;
		Set<Stat> oldStats=null;
		synchronized (this) {
			oldRecords=this.records;
			oldStats=this.stats;
			this.records=new HashSet<Record>();
			this.stats=new HashSet<Stat>();
		}
		repository.saveRecord(oldRecords);
		repository.saveStat(oldStats);
	}
	
	public RecordRepository getRepository() {
		return repository;
	}

	public void setRepository(RecordRepository repository) {
		this.repository = repository;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}
