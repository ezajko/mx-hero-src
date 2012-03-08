package org.mxhero.engine.plugin.adsync.internal.service.jdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.plugin.adsync.internal.domain.DomainAdLdap;
import org.mxhero.engine.plugin.adsync.internal.repository.DomainAdLdapRepository;
import org.mxhero.engine.plugin.adsync.internal.service.DomainsSynchronizer;
import org.mxhero.engine.plugin.adsync.internal.service.MailSender;
import org.mxhero.engine.plugin.adsync.internal.source.ADSource;
import org.mxhero.engine.plugin.adsync.internal.source.ADSource.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class JdbcDomainsSynchronizer implements DomainsSynchronizer{

	public static final String SYNC_TYPE="adladp";
	
	public static final String ZIMBRA_TYPE="zimbra";
	public static final String EXCHANGE_TYPE="exchange";
	public static final String ZIMBRA_FILTER="(&(|(objectclass=zimbraAccount)(objectclass=zimbraDistributionList))(mail=*))";
	public static final String EXCHANGE_FILTER="(&(objectclass=user)(mail=*))";
	public static final String SENDER_MAIL = "adsync@mxhero.com";
	public static final String OUTPUT_SERVICE = "org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService";
	
	private static Logger log = LoggerFactory.getLogger(JdbcDomainsSynchronizer.class);
	
	private InputService inputService;
	
	private String zimbraFilter=ZIMBRA_FILTER;
	
	private String exchangeFilter=EXCHANGE_FILTER;
	
	private String senderMail=SENDER_MAIL;
	
	private String outputService=OUTPUT_SERVICE;
	
	private Boolean useMailAlternateAddress = false;
	
	private DomainAdLdapRepository repository;

	@Autowired(required=true)
	public JdbcDomainsSynchronizer(DomainAdLdapRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void synchronize(String domain) {
		DomainAdLdap domainAd = null;
		try{
			if(repository==null){
				log.error("repository is empty");
				return;
			}
			domainAd=repository.findDomainAdLdap(domain);
			if(domainAd==null){
				log.error("domain "+domain+" not found");
				return;
			}
			String filter = domainAd.getFilter();
			if(domainAd.getDirectoryType().equalsIgnoreCase(ZIMBRA_TYPE)){
				filter = zimbraFilter;
			}else if(domainAd.getDirectoryType().equalsIgnoreCase(EXCHANGE_TYPE)){
				filter = exchangeFilter;
			}
			
			ADSource source;
			try {
				source = new ADSource(domainAd.getAddres(), domainAd.getPort(), domainAd.getUser(), domainAd.getPassword(), domainAd.getSslFlag(), domainAd.getBase());
				source.setUseMailAlternateAddress(this.getUseMailAlternateAddress());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			List<Account> accounts = source.getAllPersonNames(filter);
			if(accounts!=null){
				List<String> managedList = repository.getManagedAccounts(domain);
				List<String> notManagedList = repository.getNotManagedAccounts(domain);
				Set<String> managedSet = new HashSet<String>();
				Set<String> deleteSet = new HashSet<String>();
				Set<String> notManagedSet = new HashSet<String>();
				if(managedList!=null){
					managedSet.addAll(managedList);
					deleteSet.addAll(managedList);
				}
				if(notManagedList!=null){
					notManagedSet.addAll(notManagedList);
				}
				
				for(Account account : accounts){
					if(deleteSet.contains(account.getUid())){
						deleteSet.remove(account.getUid());
					}
				}
				for(String deleteAccount : deleteSet){
					repository.deleteAccount(deleteAccount, domain);
				}

				for(Account account : accounts){
					if(!account.getMails().contains(account.getUid()+"@"+domain)){
						account.getMails().add(account.getUid()+"@"+domain);
					}

					if(managedSet.contains(account.getUid())){
						repository.updateAliasesAccount(account.getUid(), domain, new ArrayList<String>(account.getMails()));
						managedSet.remove(account.getUid());
					}else if(notManagedSet.contains(account.getUid())){
						if(domainAd.getOverrideFlag()){
							repository.updateAliasesAccount(account.getUid(), domain, new ArrayList<String>(account.getMails()));
						}
					}else{
						repository.insertAccount(account.getUid(), domain, new ArrayList<String>(account.getMails()));
					}
				}
			}
			repository.updateNextAdLdapCheck(domain);
		}catch(RuntimeException e){
			String message = e.toString();
			if(message.length()>250){
				message=message.substring(0,250);
			}
			repository.updateErrorAdLdapCheck(domain, message);
			sendMail(domainAd, e.getMessage());
			throw e;
		} 
	}

	private void sendMail(DomainAdLdap domainAd,String errorMessage){
		if(inputService!=null && domainAd!=null && domainAd.getNotifyEmail()!=null){
			MailSender.sendMail(inputService, domainAd.getNotifyEmail(), errorMessage, senderMail, outputService);
		}
	}

	public InputService getInputService() {
		return inputService;
	}

	public void setInputService(InputService inputService) {
		this.inputService = inputService;
	}

	public String getZimbraFilter() {
		return zimbraFilter;
	}

	public void setZimbraFilter(String zimbraFilter) {
		this.zimbraFilter = zimbraFilter;
	}

	public String getExchangeFilter() {
		return exchangeFilter;
	}

	public void setExchangeFilter(String exchangeFilter) {
		this.exchangeFilter = exchangeFilter;
	}

	public String getSenderMail() {
		return senderMail;
	}

	public void setSenderMail(String senderMail) {
		this.senderMail = senderMail;
	}

	public String getOutputService() {
		return outputService;
	}

	public void setOutputService(String outputService) {
		this.outputService = outputService;
	}

	public Boolean getUseMailAlternateAddress() {
		return useMailAlternateAddress;
	}

	public void setUseMailAlternateAddress(Boolean useMailAlternateAddress) {
		this.useMailAlternateAddress = useMailAlternateAddress;
	}
	
}
