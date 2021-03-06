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

package org.mxhero.engine.plugin.statistics.internal.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;

public class CachedRecordRepository implements RecordRepository, Runnable{

	private static Logger log = LoggerFactory.getLogger(CachedRecordRepository.class);
	
	private RecordRepository repository;
	
	private Set<Record> records;
	
	private Set<Stat> stats;
	
	private Long updateTime = 5000l;
	
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
		try{
			repository.saveRecord(oldRecords);
			repository.saveStat(oldStats);
		}catch(ConcurrencyFailureException e){
			log.warn(e.toString());
			synchronized (this) {
				this.records.addAll(oldRecords);
				this.stats.addAll(oldStats);
			}
		}catch(Exception e){
			log.error(e.toString());
		}
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
