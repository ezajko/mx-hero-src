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

package org.mxhero.engine.plugin.gsync.internal;

import java.util.List;

import org.mxhero.engine.plugin.gsync.internal.repository.DomainAdLdapRepository;
import org.mxhero.engine.plugin.gsync.internal.service.DomainsSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DSControllerRunner {

	private static Logger log = LoggerFactory.getLogger(DSControllerRunner.class);
	
	private static final long CHECK_TIME = 30000;
	
	private Thread runnerThread=null;
	
	@Autowired(required=true)
	private DomainAdLdapRepository repository;
	
	private DomainsSynchronizer synchronizer;
	
	private boolean keepWorking=true;
	
	public void start(){
		keepWorking=true;
		runnerThread=new Thread(new DSRunner());
		runnerThread.start();
		log.info("DSRunner started");
	}
	
	public void stop(){
		keepWorking=false;
		if(runnerThread!=null){
			try {
				runnerThread.join();
			} catch (InterruptedException e) {
				log.error("interrupted while joining",e);
			}
		}
		log.info("DSRunner stopped");
	}
	
	private class DSRunner implements Runnable{

		public void run() {
			long lastCheck = 0;
			while(keepWorking){
				if(lastCheck+CHECK_TIME-System.currentTimeMillis()<0){
					checkAndSync();
					lastCheck=System.currentTimeMillis();
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error("interrupted while waiting for next cicle",e);
				}
			}
		}
		
		private void checkAndSync(){
			try{
				List<String> domains = repository.findDomainsToSync();
				if(domains!=null){
					for(String domain : domains){
						try{
							synchronizer.synchronize(domain);
						}catch(Exception e){
							log.error("error while processing "+domain,e);
						}
					}
				}
			}catch(Exception e){
				log.error("error while processing domains",e);
			}
		}
	
	}

	public DomainsSynchronizer getSynchronizer() {
		return synchronizer;
	}

	public void setSynchronizer(DomainsSynchronizer synchronizer) {
		this.synchronizer = synchronizer;
	}

	public DomainAdLdapRepository getRepository() {
		return repository;
	}

	public void setRepository(DomainAdLdapRepository repository) {
		this.repository = repository;
	}
	
}
