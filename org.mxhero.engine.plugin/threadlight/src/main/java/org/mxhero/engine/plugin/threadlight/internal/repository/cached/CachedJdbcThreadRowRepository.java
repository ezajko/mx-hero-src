package org.mxhero.engine.plugin.threadlight.internal.repository.cached;

import java.sql.Timestamp;
import java.util.Calendar;
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

/**
 * This class just keep track of records that did not have a reply.
 * If a record has a reply it will saved and them removed from the cache.
 * @author mxhero
 *
 */
public class CachedJdbcThreadRowRepository implements ThreadRowRepository, Runnable{

	private static Logger log = LoggerFactory.getLogger(CachedJdbcThreadRowRepository.class);
	
	private ThreadRowRepository repository;
	private ThreadRowFinder finder;
	private Map<ThreadRowPk,ThreadRow> threads;
	private Set<ThreadRow> saveLater = new HashSet<ThreadRow>();
	private Set<ThreadRowFollower> addLater = new HashSet<ThreadRowFollower>();
	private Set<ThreadRowFollower> removeLater = new HashSet<ThreadRowFollower>();
	private Long updateTime = 10000l;
	private Long syncTimeInMinutes = 60l;
	private Integer sinceInDays = 30;
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	
	private Timestamp getSinceTime(){
		if(sinceInDays!=null && sinceInDays >0){
			Calendar sinceCalendar = Calendar.getInstance();
			sinceCalendar.add(Calendar.DATE, -sinceInDays);
			return new Timestamp(sinceCalendar.getTimeInMillis());
		}else{
			return null;
		}
		
	}
	
	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}

	public ThreadRowFinder getFinder() {
		return finder;
	}

	public void setFinder(ThreadRowFinder finder) {
		this.finder = finder;
	}
	
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		//First time so it starts with real data.
		threads = finder.findBySpecsMap(getSinceTime());
		log.debug("loaded "+threads.size()+" threads");
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
		persist(false);
	}

	@Override
	public void run() {
		long lastUpdate = System.currentTimeMillis();
		long lastReload = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastReload+(syncTimeInMinutes*60*1000)-System.currentTimeMillis()<0){
				persist(true);
				lastUpdate=System.currentTimeMillis();
				lastReload=System.currentTimeMillis();
			}else if(lastUpdate+updateTime-System.currentTimeMillis()<0){
				persist(false);
				lastUpdate=System.currentTimeMillis();
			}

		}
	}
	
	public void persist(boolean reaload){
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
			if(reaload){
				threads = finder.findBySpecsMap(getSinceTime());
				log.info("loaded "+threads.size()+" threads");
			}
		}
		log.debug("persisting saveLater:"+oldSaveLater.size()+" followers:"+addLater.size()+" removeLater:"+removeLater.size());
		for(ThreadRow toSync : oldSaveLater){
			try{
				toSync.setId(repository.saveThread(toSync));
			}catch(ConcurrencyFailureException e){
				log.warn(e.getMessage());
				synchronized (this) {
					saveLater.add(toSync);
				}
			}catch(Exception e){
				log.warn(e.toString());
			}
		}
		for(ThreadRowFollower toSync : oldAddLater){
			try{
				repository.addFollower(toSync.getThreadRow(),toSync);
			}catch(ConcurrencyFailureException e){
				log.warn(e.getMessage());
				synchronized (this) {
					addLater.add(toSync);
				}
			}catch(Exception e){
				log.warn(e.getMessage());
			}
		}
		for(ThreadRowFollower toSync : oldRemoveLater){
			try{
				repository.removeFollower(toSync.getThreadRow(),toSync.getFollower());
			}catch(ConcurrencyFailureException e){
				log.warn(e.getMessage());
				synchronized (this) {
					removeLater.add(toSync);
				}
			}catch(Exception e){
				log.warn(e.getMessage());
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
					//remove threads that have a reply, 
					//cacheRepository is used just for following new one 
					//and replying old ones, those that had a reply
					//should use jdbcRepository
					if(updatedThreadRow.getReplyTime()!=null){
						threads.remove(updatedThreadRow.getPk());
					}
					if(threadRow.getSnoozeTime()!=null){
						updatedThreadRow.setSnoozeTime((Timestamp) threadRow.getSnoozeTime().clone());
					}
				//INSERT
				}else{
					updatedThreadRow = new ThreadRow();
					if(threadRow.getCreationTime()!=null){
						updatedThreadRow.setCreationTime((Timestamp) threadRow.getCreationTime().clone());
					}else{
						updatedThreadRow.setCreationTime(new Timestamp(System.currentTimeMillis()));
					}					
					updatedThreadRow.setPk(new ThreadRowPk(threadRow.getPk().getMessageId()
							, threadRow.getPk().getSenderMail()
							, threadRow.getPk().getRecipientMail()));
					updatedThreadRow.setSubject(threadRow.getSubject());
					if(threadRow.getSnoozeTime()!=null){
						updatedThreadRow.setSnoozeTime((Timestamp) threadRow.getSnoozeTime().clone());
					}else{
						updatedThreadRow.setSnoozeTime(new Timestamp(System.currentTimeMillis()));
					}
				}
				threads.put(updatedThreadRow.getPk(), updatedThreadRow);
				saveLater.add(updatedThreadRow);
				return updatedThreadRow.getId();
			}
		}
		return null;
	}

	@Override
	public void addFollower(ThreadRow threadRow, ThreadRowFollower follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			synchronized (this) {
				ThreadRowFollower threadFollower = null;	
				ThreadRow savedThreadRow = threads.get(threadRow.getPk());
				if(savedThreadRow!=null){
					if(savedThreadRow.getFollowers()==null){
						savedThreadRow.setFollowers(new HashSet<ThreadRowFollower>());
					}
					threadFollower = new ThreadRowFollower(savedThreadRow,follower.getFollower());
					threadFollower.setFolowerParameters(follower.getFolowerParameters());
					savedThreadRow.getFollowers().add(threadFollower);
				}else{
					threadFollower = new ThreadRowFollower(threadRow,follower.getFollower());
					threadFollower.setFolowerParameters(follower.getFolowerParameters());
				}
				addLater.add(threadFollower);
			}
		}
	}

	@Override
	public void removeFollower(ThreadRow threadRow, String follower) {
		if(threadRow!=null && threadRow.getPk()!=null && follower!=null){
			synchronized (this) {
				ThreadRowFollower threadFollower = null;
				ThreadRow savedThreadRow = threads.get(threadRow.getPk());
				if(savedThreadRow!=null){
					threadFollower = new ThreadRowFollower(savedThreadRow, follower);
					savedThreadRow.getFollowers().remove(threadFollower);
				}else{
					threadFollower = new ThreadRowFollower(threadRow, follower);
				}
				removeLater.add(threadFollower);
			}
		}
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getSyncTimeInMinutes() {
		return syncTimeInMinutes;
	}

	public void setSyncTimeInMinutes(Long syncTimeInMinutes) {
		this.syncTimeInMinutes = syncTimeInMinutes;
	}

	public Integer getSinceInDays() {
		return sinceInDays;
	}

	public void setSinceInDays(Integer sinceInDays) {
		this.sinceInDays = sinceInDays;
	}

}
