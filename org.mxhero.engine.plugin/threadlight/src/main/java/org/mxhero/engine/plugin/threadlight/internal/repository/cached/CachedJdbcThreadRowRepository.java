package org.mxhero.engine.plugin.threadlight.internal.repository.cached;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mxhero.engine.commons.util.deepcopy.DeepCopy;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;

public class CachedJdbcThreadRowRepository implements ThreadRowRepository, Runnable{

	private static Logger log = LoggerFactory.getLogger(CachedJdbcThreadRowRepository.class);
	
	private ThreadRowRepository repository;
	private ThreadRowFinder finder;
	private Map<ThreadRowPk,ThreadRow> threads;
	private Set<ThreadRow> saveLater = new HashSet<ThreadRow>();
	private Set<ThreadRowFollower> addLater = new HashSet<ThreadRowFollower>();
	private Set<ThreadRowFollower> removeLater = new HashSet<ThreadRowFollower>();
	private Long updateTime = 5000l;
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	
	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}

	public void init(){
		threads = finder.findAll();
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
		Set<ThreadRow> oldSaveLater=null;
		Set<ThreadRowFollower> oldAddLater=null;
		Set<ThreadRowFollower> oldRemoveLater=null;
		synchronized (this) {
			oldSaveLater=this.saveLater;
			oldAddLater=this.addLater;
			oldRemoveLater=this.removeLater;
			saveLater=new HashSet<ThreadRow>();
			addLater=new HashSet<ThreadRowFollower>();
			removeLater=new HashSet<ThreadRowFollower>();
		}
		for(ThreadRow toSync : oldSaveLater){
			try{
				toSync.setId(repository.saveThread(toSync));
			}catch(ConcurrencyFailureException e){
				log.warn(e.toString());
				synchronized (this) {
					saveLater.add(toSync);
				}
			}catch(Exception e){
				log.error(e.toString());
			}
		}
		for(ThreadRowFollower toSync : oldAddLater){
			try{
				repository.addFollower(toSync.getThreadRow(),toSync.getFollower());
			}catch(ConcurrencyFailureException e){
				log.warn(e.toString());
				synchronized (this) {
					addLater.add(toSync);
				}
			}catch(Exception e){
				log.error(e.toString());
			}
		}
		for(ThreadRowFollower toSync : oldRemoveLater){
			try{
				repository.removeFollower(toSync.getThreadRow(),toSync.getFollower());
			}catch(ConcurrencyFailureException e){
				log.warn(e.toString());
				synchronized (this) {
					removeLater.add(toSync);
				}
			}catch(Exception e){
				log.error(e.toString());
			}
		}
		oldAddLater.clear();
		oldRemoveLater.clear();
		oldSaveLater.clear();
	}
	
	
	
	@Override
	public ThreadRow find(ThreadRowPk pk) {
		synchronized (this) {
			ThreadRow result = threads.get(pk);
			if(result!=null){
				result = (ThreadRow)DeepCopy.copy(result);
			}
			return result;
		}
	}
	
	@Override
	//TO DO
	public Long saveThread(ThreadRow threadRow) {
		if(threadRow!=null && threadRow.getPk()!=null 
				&& threadRow.getPk().getMessageId()!=null 
				&& threadRow.getPk().getRecipientMail()!=null 
				&& threadRow.getPk().getSenderMail()!=null){
			synchronized (this) {
				ThreadRow updatedThreadRow = threads.get(threadRow.getPk());
				//IF UPDATE
				if(updatedThreadRow!=null){
					updatedThreadRow.setReplyTime(threadRow.getReplyTime());
				//INSERT
				}else{
					updatedThreadRow = new ThreadRow();
					updatedThreadRow.setCreationTime((Timestamp) threadRow.getCreationTime().clone());
					updatedThreadRow.setPk(new ThreadRowPk(threadRow.getPk().getMessageId()
							, threadRow.getPk().getSenderMail()
							, threadRow.getPk().getRecipientMail()));
					updatedThreadRow.setSubject(threadRow.getSubject());
				}
				threads.put(updatedThreadRow.getPk(), updatedThreadRow);
				saveLater.add(updatedThreadRow);
				return updatedThreadRow.getId();
			}
		}
		return null;
	}

	@Override
	//TO DO
	public void addFollower(ThreadRow threadRow, String follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			synchronized (this) {
					if(threadRow.getFollowers()==null){
						threadRow.setFollowers(new HashSet<ThreadRowFollower>());
					}
					ThreadRowFollower threadFollower = new ThreadRowFollower(threadRow,follower);
					threadRow.getFollowers().add(threadFollower);
					addLater.add(threadFollower);
				}
			}
	}

	@Override
	//TO DO
	public void removeFollower(ThreadRow threadRow, String follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			synchronized (this) {
				if(threadRow!=null){
					ThreadRowFollower threadFollower = new ThreadRowFollower(threadRow, follower);
					threadRow.getFollowers().remove(threadFollower);
					removeLater.add(threadFollower);
				}
			}
		}
	}

}
