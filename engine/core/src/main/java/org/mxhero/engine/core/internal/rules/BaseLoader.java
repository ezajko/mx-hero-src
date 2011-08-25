package org.mxhero.engine.core.internal.rules;

import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created to replace KnowledgeAgent that does not work properlly is osgi at this point.
 * It will load using time intervals the knowledgeBase so it can be updated. Do not use
 * short intervals because this is time expensive.
 * @author mmarmol
 */
public class BaseLoader implements Runnable, PropertiesListener{

	private static Logger log = LoggerFactory.getLogger(BaseLoader.class);
	
	private static final long DEFAULT_CHECK_TIME = 1000;
	
	private static final long DEFAULT_LOAD_TIME = 60000;
	
	private static final long MIM_LOAD_TIME = 9999;

	private long checkTime = DEFAULT_CHECK_TIME;
	
	private long loadtime = DEFAULT_LOAD_TIME;
	
	private BaseBuilder builder;
	
	private boolean working = false;
	
	private PropertiesService properties;
	
	private Thread thread;
	
	/**
	 * Creates a thread and pass this object as runnable, after that starts the thread.
	 */
	public void start(){
		log.info("started");
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long lastLoadTime = 0;
		working=true;
		getProperties().addListener(this);
		this.updated();
		log.info("Running");
		
		while(working){
			try {
				if ((System.currentTimeMillis()-lastLoadTime)>=loadtime){
						lastLoadTime = System.currentTimeMillis();
						builder.buildBase();
						if(builder.getBase()!=null){
							log.info("base:"+builder.getBase()+", groups:" + builder.getBase().getGroups()+", rules:"+builder.getBase().getRules());
						}else{
							log.info("base empty");
						}
					}
				Thread.sleep(checkTime);
			} catch (InterruptedException e) {
				log.warn("interrupted while waiting",e);
			} catch (RuntimeException e){
				log.warn("error while building knowledge base",e);
			}
		}
		log.info("Stopping");
		getProperties().removeListener(this);
		
	}
	
	/**
	 * Stop the thread.
	 */
	public void stop(){
		log.info("stopped");
		working = false;
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {
				log.warn("interrupted",e);
			}
			thread=null;
		}
	}

	/**
	 * @return
	 */
	public boolean isWorking(){
		return working;
	}
	
	/**
	 * @return the checkTime
	 */
	public long getCheckTime() {
		return checkTime;
	}

	/**
	 * @param checkTime the checkTime to set
	 */
	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	/**
	 * @return the loadtime
	 */
	public long getLoadtime() {
		return loadtime;
	}

	/**
	 * @param loadtime the loadtime to set
	 */
	public void setLoadtime(long loadtime) {
		this.loadtime = loadtime;
	}

	/**
	 * @return the builder
	 */
	public BaseBuilder getBuilder() {
		return builder;
	}

	/**
	 * @param builder the builder to set
	 */
	public void setBuilder(BaseBuilder builder) {
		this.builder = builder;
	}

	/**
	 * Updates times interval
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		String newLoadTime = getProperties().getValue(Core.SCAN_INTERVAL);
		
		log.debug("updating");
		log.debug("newLoadTime:"+newLoadTime);

		if (newLoadTime!=null){
			try{
				if (Long.parseLong(newLoadTime)>MIM_LOAD_TIME){
					this.loadtime = Long.parseLong(newLoadTime);
				} else {
					log.warn("value to low ("+newLoadTime+") while geting property "+Core.SCAN_INTERVAL);
				}
			} catch (NumberFormatException e){
				log.warn("error while geting property "+Core.SCAN_INTERVAL,e);
			}
		}
		
	}

	/**
	 * @return the properties
	 */
	public PropertiesService getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}
