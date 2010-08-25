package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.io.File;
import java.io.IOException;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;
import org.mxhero.engine.plugin.xmlfinder.internal.service.XMLFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author mmarmol
 */
public class XMLTimeLoader implements Runnable, PropertiesListener{

	private static Logger log = LoggerFactory.getLogger(XMLTimeLoader.class);
	
	private XMLProcessor processor;
	
	private boolean working = false;
	
	private static final long DEFAULT_CHECK_TIME = 1000;
	
	private static final long MIM_CHECH_TIME = 100;
	
	private static final long DEFAULT_LOAD_TIME = 60000;
	
	private static final long MIM_LOAD_TIME = 9999;
	
	private long checkTime = DEFAULT_CHECK_TIME;
	
	private long loadtime = DEFAULT_LOAD_TIME;
		
	private Thread thread;
	
	private String fileName;
	
	private PropertiesService properties;
	
	/**
	 * Creates the XMLProcessor.
	 * @throws IOException
	 * @throws MappingException
	 */
	public XMLTimeLoader() throws IOException, MappingException {
		this.processor = new XMLProcessor();
	}
	
	/**
	 * Starts a thread with this object as runnable.
	 */
	public void start(){
		log.debug("started");
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
		properties.addListener(this);
		this.updated();
		log.info("Running");
		while(working){
			try {
				if ((System.currentTimeMillis()-lastLoadTime)>=loadtime){
					lastLoadTime = System.currentTimeMillis();
					log.debug("ill try to read file");
					if (fileName!=null){
						log.debug("File is not null");
						File file = new File(fileName);
						DomainListXML list = null;
						try {
							if (file.canRead()){
								log.debug("File found ready to read");
								list = processor.loadXML(fileName);
								log.debug("Ready to load maps");
								MapLoader.createMap(list);
							} else {
								log.warn("cant read file "+this.fileName);
							}
						} catch (SecurityException e){
							log.error("We can't acesses to the file",e);
						} catch (MarshalException e) {
							log.error("Error while trying to marshal",e);
						} catch (ValidationException e) {
							log.error("Error validating",e);
						} catch (IOException e) {
							log.error("Unexpected IO error",e);
						}

					}
				}
				Thread.sleep(checkTime);
			} catch (InterruptedException e) {
				log.warn("interrupted while waiting",e);
			}
		}
		log.info("Stopping");
		properties.removeListener(this);
	}

	/**
	 * Stop the thread and waits for him.
	 */
	public void stop(){
		log.debug("stopped");
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
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		String newCheckTime = properties.getValue(XMLFinder.LOADER_CHECK_TIME);
		String newLoadTime = properties.getValue(XMLFinder.LOADER_LOAD_TIME);
		this.fileName = properties.getValue(XMLFinder.DOMAINS_FILE_PATH);
		
		log.debug("updating");
		log.debug("newCheckTime:"+newCheckTime);
		log.debug("newLoadTime:"+newLoadTime);
		log.debug("fileName:"+fileName);
		if (newCheckTime!=null){
			try{
				if (Long.parseLong(newCheckTime)>MIM_CHECH_TIME){
					this.checkTime = Long.parseLong(newCheckTime);
				} else {
					log.warn("value to low ("+newCheckTime+") "+XMLFinder.LOADER_CHECK_TIME);
				}
			} catch (NumberFormatException e){
				log.warn("error while geting property "+XMLFinder.LOADER_CHECK_TIME,e);
			}
		}
	
		if (newLoadTime!=null){
			try{
				if (Long.parseLong(newLoadTime)>MIM_LOAD_TIME){
					this.loadtime = Long.parseLong(newLoadTime);
				} else {
					log.warn("value to low ("+newLoadTime+") while geting property "+XMLFinder.LOADER_LOAD_TIME);
				}
			} catch (NumberFormatException e){
				log.warn("error while geting property "+XMLFinder.LOADER_LOAD_TIME,e);
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
