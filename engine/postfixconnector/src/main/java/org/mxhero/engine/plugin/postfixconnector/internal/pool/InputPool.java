package org.mxhero.engine.plugin.postfixconnector.internal.pool;

import java.util.concurrent.BlockingQueue;

import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.pool.QueueTaskPool;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.queue.InputQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is used to create Threads that will be called to send an email 
 * to the core plugin using the InputService.
 * @author mmarmol
 */
public final class InputPool extends QueueTaskPool<MimeMail> implements PropertiesListener {

	private static Logger log = LoggerFactory.getLogger(InputPool.class);

	private InputService service;
	
	private PropertiesService properties;
	
	private LogStat logStat;
	
	private BlockingQueue<MimeMail> mailQueue= InputQueue.getInstance();
	
	/**
	 * Sets the BundleContext and creats the object.
	 * @param bc
	 */
	public InputPool(){
        super(InputQueue.getInstance());
	}
	
	/**
	 * Creates a InputTask if InputService is not null, in other case 
	 * returns null.
	 * @param mail object representing the mail to be processed.
	 * @return the InputTask or null.
	 */
	@Override
	protected Runnable createTask(MimeMail mail) {
		if(service!=null){
			return new InputTask(mail);
		}
		return null;		
	}
	
	/**
	 * Starts the tracker for the InputService, creates the ThreadPool and
	 * is is added like a listener to the PostfixConnectorProperties.
	 */
	@Override
	protected void init(){
		log.info("INIT");
        properties.addListener(this);
		updated();		
	}
	
	/**
	 * Close the tracker and remove the listener for PostfixConnectorProperties.
	 */
	@Override
	protected void clean(){
		log.info("CLEAN");
		properties.removeListener(this);
	}
	
	/**
	 * Updates CorePoolsize, MaximumPoolSize, KeepAliveTime, WaitTime
	 * @see org.mxhero.engine.domain.properties.PropertiesListener#updated()
	 */
	@Override
	public void updated() {
		log.debug("Updating.");
		setCorePoolsize(properties.getValue(PostfixConnector.OUTPUTPOOL_COREPOOLSIZE));
		setMaximumPoolSize(properties.getValue(PostfixConnector.OUTPUTPOOL_MAXIMUMPOOLSIZE));
		setKeepAliveTime(properties.getValue(PostfixConnector.OUTPUTPOOL_KEEPALIVETIME));
		setWaitTime(properties.getValue(PostfixConnector.OUTPUTPOOL_QUEUE_WAIT_TIME));
	}
	
	/**
	 * This class just calls the InputService and adds the mail.
	 * @author mmarmol
	 */
	private class InputTask implements Runnable{
		
		private MimeMail mail;
		
		/**
		 * Creates the object and sets the mail.
		 * @param mail
		 */
		public InputTask(MimeMail mail){
			this.mail = mail;
		}
		
		/**
		 * It will call the service and will add the mail.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try{
				InputPool.this.service.addMail(mail);
				InputPool.log.debug("Mail added:"+mail);
			} catch (Exception e){
				if(getLogStat()!=null){
					getLogStat().log(mail, getProperties().getValue(PostfixConnector.CORE_ERROR_STAT), e.getMessage());
				}
				InputPool.log.warn("Mail wasnt added, sending to queue again:"+mail,e);
				InputPool.this.mailQueue.add(mail);
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

	/**
	 * @return the service
	 */
	public InputService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(InputService service) {
		log.debug("inputService registered:"+service.getClass());
		this.service = service;
	}

	/**
	 * @return the logStat
	 */
	public LogStat getLogStat() {
		return logStat;
	}

	/**
	 * @param logStat the logStat to set
	 */
	public void setLogStat(LogStat logStat) {
		this.logStat = logStat;
	}

}
