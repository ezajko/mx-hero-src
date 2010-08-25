package org.mxhero.engine.plugin.postfixconnector.internal.pool;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.pool.QueueTaskPool;
import org.mxhero.engine.domain.properties.PropertiesListener;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.statistic.LogStat;
import org.mxhero.engine.plugin.postfixconnector.internal.service.PostfixConnector;
import org.mxhero.engine.plugin.postfixconnector.queue.OutputQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Is used to create Threads to send email outside the engine using he SendMailTask.
 * @author mmarmol
 */
public final class OutputPool extends QueueTaskPool<MimeMail> implements PropertiesListener{

	private static Logger log = LoggerFactory.getLogger(OutputPool.class);
		
	private Properties props;
	
	private PropertiesService properties;
	
	private LogStat logStat;
	
	private SimpleDateFormat format;
	
	/**
	 * Creates the object and call super(OutputQueue.getInstance()).
	 */
	public OutputPool() {
		super(OutputQueue.getInstance());
	}
	
	/**
	 * Will always return a SendMailTask.
	 * @see org.mxhero.engine.domain.pool.QueueTaskPool#createTask(java.lang.Object)
	 */
	@Override
	protected Runnable createTask(MimeMail mail) {
		SendMailTask task = new SendMailTask(mail,props);
		task.setLogStat(getLogStat());
		task.setPropertiesService(properties);
		task.setFormat(getFormat());
		return task;
	}
	
	/**
	 *  Added as like a listener to the PostfixConnectorProperties and creating properties for mail tasks.
	 */
	protected void init(){
		log.info("INIT");
		updated();
		properties.addListener(this);
		props = new Properties();
	    props.put("mail.smtp.host", properties.getValue(PostfixConnector.SMTP_HOST_NAME, "localhost"));
	    props.put("mail.smtp.port", properties.getValue(PostfixConnector.SMTP_HOST_PORT, "5556"));
	}
	
	/**
	 * Just implemented to complain with parent class.
	 */
	protected void clean(){
		log.info("CLEAN");
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

	/**
	 * @return the format
	 */
	public SimpleDateFormat getFormat() {
		if(format==null){
			format = new SimpleDateFormat(getProperties().getValue(PostfixConnector.STATS_TIME_FORMAT));
		}
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}
}
