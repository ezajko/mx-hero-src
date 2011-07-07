package org.mxhero.engine.mqueues.internal.queue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.h2.jdbcx.JdbcConnectionPool;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.queue.MimeMailQueueService;
import org.mxhero.engine.mqueues.internal.queue.QueueId;
import org.mxhero.engine.mqueues.internal.queue.blocking.AbstractMimeMailBlockingQueue;
import org.mxhero.engine.mqueues.internal.queue.blocking.H2MimeMailBlockingQueue;
import org.mxhero.engine.mqueues.internal.queue.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class H2MimeMailQueueService implements MimeMailQueueService{

	private static Logger log = LoggerFactory.getLogger(H2MimeMailQueueService.class);
	
	@Autowired
	private JdbcConnectionPool pool;
	
	@SuppressWarnings("rawtypes")
	private static Map<QueueId, AbstractMimeMailBlockingQueue> queues = new Hashtable<QueueId, AbstractMimeMailBlockingQueue>();
	
	@Autowired
	private RecordRepository repository;

	private int capacity = 1000;
	
	private long addRequeueTime = 5000;

	private String backupFolder;
	
	private Boolean doBackup = true;
	
	private String scriptFolder;

	@Override
	public void put(String module, String phase, MimeMail e)
			throws InterruptedException {
		getQueue(module,phase).put(e);
	}

	@Override
	public boolean offer(String module, String phase, MimeMail e, long timeout,
			TimeUnit unit) throws InterruptedException {
		return getQueue(module,phase).offer(e,timeout,unit);
	}

	@Override
	public boolean offer(String module, String phase, MimeMail e) {
		return getQueue(module,phase).offer(e);
	}

	@Override
	public MimeMail take(String module, String phase)
			throws InterruptedException {
		return getQueue(module,phase).take();
	}

	@Override
	public MimeMail poll(String module, String phase, long timeout,
			TimeUnit unit) throws InterruptedException {
		return getQueue(module,phase).poll(timeout,unit);
	}

	@Override
	public MimeMail poll(String module, String phase) {
		return getQueue(module,phase).poll();
	}

	@Override
	public boolean remove(String module, String phase, MimeMail o) throws InterruptedException{
		return getQueue(module,phase).remove(o);
	}

	@Override
	public void removeAddTo(String fromModule, String fromPhase, MimeMail o,
			MimeMail z, String toModule, String toPhase) throws InterruptedException {
		getQueue(fromModule,fromPhase).removeAddTo(o, z, getQueue(toModule,toPhase));
	}
	
	@Override
	public void reEnqueue(String module, String phase, MimeMail o)
			throws InterruptedException {
		getQueue(module,phase).reEnqueue(o,addRequeueTime);
		
	}
	
	private AbstractMimeMailBlockingQueue<MimeMail> getQueue(String module,
			String phase) {
		AbstractMimeMailBlockingQueue<MimeMail> queue=null;
		QueueId key=new QueueId();
		key.setModule(module);
		key.setPhase(phase);
		queue=getOrCreateQueue(key);
		return queue;
	}

	public void start(){
		try{
			String scriptFile=null;
			
			if(scriptFolder!=null){
				File bF = new File(scriptFolder);
				if(bF.exists() && bF.isDirectory()){
					scriptFile=bF.getAbsolutePath();
				}
			}
			if(scriptFile==null){
				scriptFile=System.getProperty("java.io.tmpdir");
			}
			File realFile = new File(scriptFile,"create.sql");
			scriptFile = realFile.getAbsolutePath();
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("create.sql");
			OutputStream out=new FileOutputStream(realFile);
			byte buf[]=new byte[1024];
			int len;
			while((len=inputStream.read(buf))>0)
			out.write(buf,0,len);
			out.close();
			inputStream.close();
			log.info("script file saved in "+scriptFile);
			
		pool.getConnection().createStatement().execute("RUNSCRIPT FROM '"+scriptFile+"'");
		}catch (Exception e){
			log.error("error while doing backup",e);
		}

		logState();
		repository.resetQueues();
		logState();
		queues.clear();
	}
	
	public void stop(){
		logState();
		if(doBackup){
			try{
				String backupFile=null;
				
				if(backupFolder!=null){
					File bF = new File(backupFolder);
					if(bF.exists() && bF.isDirectory()){
						backupFile=bF.getAbsolutePath();
					}
				}
				if(backupFile==null){
					backupFile=System.getProperty("java.io.tmpdir");
				}
				backupFile = new File(backupFile,"mqueues"+System.currentTimeMillis()+".sql").getAbsolutePath();
				log.info("backup for mqueues in "+backupFile);
			pool.getConnection().createStatement().execute("SCRIPT TO '"+backupFile+"'");
			}catch (Exception e){
				log.error("error while doing backup",e);
			}
		}
	}
	
	public void logState(){
		log.info("Total Dequeued mails: " +repository.countDequeued());
		log.info("Total Enqueued mails: " +repository.countEnqueued());
		log.info("Total per Queues:\n"+Arrays.deepToString(repository.getCountByQueues().toArray()));
		log.info("In memory queues:" +getQueuesCount().toString());
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, String> getQueuesCount() {
		Map<String,String> queuesCount = new Hashtable<String,String>();
		for(Entry<QueueId,AbstractMimeMailBlockingQueue> entry : queues.entrySet()){
			queuesCount.put(entry.getKey().toString(), "[size="+new Integer(entry.getValue().size())+", enqueued="+new Integer(entry.getValue().enqueued())+"]");
		}
		return queuesCount;
	}
	
	@SuppressWarnings("unchecked")
	private AbstractMimeMailBlockingQueue<MimeMail> getOrCreateQueue(QueueId key){
		AbstractMimeMailBlockingQueue<MimeMail> queue = null;
		synchronized (queues) {
			queue = queues.get(key);
			if(queue==null){
				int count = repository.count(key.getModule(), key.getPhase()).intValue();
				queue = new H2MimeMailBlockingQueue(key, capacity, count, repository);
				queues.put(key, queue);
			}
		}
		return queue;
	}
	
	public RecordRepository getRepository() {
		return repository;
	}

	public void setRepository(RecordRepository repository) {
		this.repository = repository;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public long getAddRequeueTime() {
		return addRequeueTime;
	}

	public void setAddRequeueTime(long addRequeueTime) {
		if(addRequeueTime<0)throw new IllegalArgumentException();
		this.addRequeueTime = addRequeueTime;
	}

	public String getBackupFolder() {
		return backupFolder;
	}

	public void setBackupFolder(String backupFolder) {
		this.backupFolder = backupFolder;
	}

	public JdbcConnectionPool getPool() {
		return pool;
	}

	public void setPool(JdbcConnectionPool pool) {
		this.pool = pool;
	}

	public Boolean getDoBackup() {
		return doBackup;
	}

	public void setDoBackup(Boolean doBackup) {
		this.doBackup = doBackup;
	}

	public String getScriptFolder() {
		return scriptFolder;
	}

	public void setScriptFolder(String scriptFolder) {
		this.scriptFolder = scriptFolder;
	}

}
