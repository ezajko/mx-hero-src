package org.mxhero.engine.plugin.threadlight.internal.repository.cached;

import java.util.HashSet;
import java.util.Map;

import org.mxhero.engine.commons.util.deepcopy.DeepCopy;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowFinder;
import org.mxhero.engine.plugin.threadlight.internal.repository.ThreadRowRepository;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRow;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowFollower;
import org.mxhero.engine.plugin.threadlight.internal.vo.ThreadRowPk;

public class CachedJdbcThreadRowRepository implements ThreadRowRepository{

	private ThreadRowRepository repository;
	private ThreadRowFinder finder;
	private Map<ThreadRowPk,ThreadRow> threads;
	private Map<ThreadRowPk,ThreadRow> saveLater;
	private Map<ThreadRowPk,ThreadRow> removeLater;
	
	public ThreadRowRepository getRepository() {
		return repository;
	}

	public void setRepository(ThreadRowRepository repository) {
		this.repository = repository;
	}

	public void init(){
		threads = finder.findAll();
	}

	@Override
	public ThreadRow find(ThreadRowPk pk) {
		synchronized (threads) {
			ThreadRow result = threads.get(pk);
			if(result!=null){
				result = (ThreadRow)DeepCopy.copy(result);
			}
			return result;
		}
	}
	
	@Override
	//TO DO
	public void saveThread(ThreadRow threadRow) {
		if(threadRow!=null && threadRow.getPk()!=null){
			synchronized (threads) {
				repository.saveThread(threadRow);
				if(threads.containsKey(threadRow.getPk())){
					threads.put(threadRow.getPk(), threadRow);
				}else{
					threadRow=repository.find(threadRow.getPk());
					threads.put(threadRow.getPk(), threadRow);
				}
			}
		}
	}

	@Override
	//TO DO
	public void addFollower(ThreadRowPk pk, String follower) {
		if(pk!=null && follower!=null){
			synchronized (threads) {
				repository.addFollower(pk, follower);
				ThreadRow threadRow = threads.get(pk);
				if(threadRow.getFollowers()==null){
					threadRow.setFollowers(new HashSet<ThreadRowFollower>());
				}
				threadRow.getFollowers().add(new ThreadRowFollower(threadRow.getId(),threadRow.getPk(),follower));
			}
		}
	}

	@Override
	//TO DO
	public void removeFollower(ThreadRowPk pk, String follower) {
		if(pk!=null && follower!=null){
			synchronized (threads) {
				
			}
		}
	}

}
