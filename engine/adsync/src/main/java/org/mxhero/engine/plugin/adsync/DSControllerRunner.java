package org.mxhero.engine.plugin.adsync;

import java.util.Calendar;
import java.util.List;

import org.mxhero.engine.plugin.adsync.dao.DomainAdLdapDao;
import org.mxhero.engine.plugin.adsync.service.DomainsSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DSControllerRunner {

	private static Logger log = LoggerFactory.getLogger(DSControllerRunner.class);
	
	private static final long CHECK_TIME = 30000;
	
	private Thread runnerThread=null;
	
	private DomainAdLdapDao dao;
	
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
			while(keepWorking){
				checkAndSync();
				try {
					Thread.sleep(CHECK_TIME);
				} catch (InterruptedException e) {
					log.error("interrupted while waiting for next cicle",e);
				}
			}
		}
		
		private void checkAndSync(){
			try{
				List<String> domains = dao.findDomainsToSync(Calendar.getInstance());
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

	public DomainAdLdapDao getDao() {
		return dao;
	}

	public void setDao(DomainAdLdapDao dao) {
		this.dao = dao;
	}

	public DomainsSynchronizer getSynchronizer() {
		return synchronizer;
	}

	public void setSynchronizer(DomainsSynchronizer synchronizer) {
		this.synchronizer = synchronizer;
	}
	
}
