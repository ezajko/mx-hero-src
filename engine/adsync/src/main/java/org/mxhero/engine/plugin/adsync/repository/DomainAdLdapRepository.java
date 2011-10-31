package org.mxhero.engine.plugin.adsync.repository;

import java.util.List;

import org.mxhero.engine.plugin.adsync.domain.DomainAdLdap;

public interface DomainAdLdapRepository {

	public DomainAdLdap findDomainAdLdap(String domainId);
	
	public List<String> findDomainsToSync();
	
	public void updateNextAdLdapCheck(String domainId);
	
	public void updateErrorAdLdapCheck(String domainId, String lastError);
	
	public void deleteAccount(String account, String domainId);
	
	public List<String> getManagedAccounts(String domainId);
	
	public List<String> getNotManagedAccounts(String domainId);
	
	public void updateAliasesAccount(String account, String domainId, List<String> aliases);
	
	public void insertAccount(String account, String domainId, List<String> aliases);
}