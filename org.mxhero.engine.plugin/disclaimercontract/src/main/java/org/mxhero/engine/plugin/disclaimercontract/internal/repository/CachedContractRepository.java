package org.mxhero.engine.plugin.disclaimercontract.internal.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.mxhero.engine.plugin.disclaimercontract.entity.Contract;
import org.mxhero.engine.plugin.disclaimercontract.internal.repository.jdbc.JdbcContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CachedContractRepository implements Runnable{

	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	private Long syncTimeInMinutes = 5l;

	private JdbcContractRepository repository;
	private Map<Long, Set<String>> contracts = new HashMap<Long, Set<String>>();

	@Autowired(required=true)
	public CachedContractRepository(JdbcContractRepository repository) {
		this.repository = repository;
	}

	@PostConstruct
	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		thread.start();
	}

	@PreDestroy
	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
		reload();
	}

	@Override
	public void run() {
		long lastReload = System.currentTimeMillis();
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastReload+(syncTimeInMinutes*60*1000)-System.currentTimeMillis()<0){
				reload();
				lastReload=System.currentTimeMillis();
			}
		}
	}

	private void reload(){
		synchronized (contracts) {
			if(contracts!=null){
				contracts.clear();
			}
			contracts=new HashMap<Long, Set<String>>();	
		}
	}

	public void addContract(Contract createdContract){
		synchronized (contracts) {
			if(contracts==null){
				contracts=new HashMap<Long, Set<String>>();	
			}
			if(!contracts.containsKey(createdContract.getRuleId())){
				contracts.put(createdContract.getRuleId(),new HashSet<String>());
			}
			contracts.get(createdContract.getRuleId()).add(createdContract.getRecipient());
		}
	}
	
	
	public Long getSyncTimeInMinutes() {
		return syncTimeInMinutes;
	}

	public void setSyncTimeInMinutes(Long syncTimeInMinutes) {
		this.syncTimeInMinutes = syncTimeInMinutes;
	}

	public boolean containsfindByRuleAndRecipient(Long ruleId, String recipient) {
		Set<String> ruleContracts = null;
		synchronized (contracts) {
			ruleContracts = contracts.get(ruleId);
			if(ruleContracts==null){
				List<Contract> contractList = repository.findByRule(ruleId);
				ruleContracts= new HashSet<String>();
				if(contractList!=null && contractList.size()>0){
					for(Contract contract : contractList){
						ruleContracts.add(contract.getRecipient());
					}
				}
			}
			return ruleContracts.contains(recipient);
		}
	}


}
