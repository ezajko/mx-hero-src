package org.mxhero.engine.plugin.gsync.internal.service.jdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mxhero.engine.commons.connector.InputService;
import org.mxhero.engine.plugin.gsync.internal.domain.DomainAdLdap;
import org.mxhero.engine.plugin.gsync.internal.gdata.GDSource;
import org.mxhero.engine.plugin.gsync.internal.gdata.GDSource.Account;
import org.mxhero.engine.plugin.gsync.internal.repository.DomainAdLdapRepository;
import org.mxhero.engine.plugin.gsync.internal.service.DomainsSynchronizer;
import org.mxhero.engine.plugin.gsync.internal.service.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class JdbcDomainsSynchronizer implements DomainsSynchronizer {

	public static final String SENDER_MAIL = "adsync@mxhero.com";
	public static final String OUTPUT_SERVICE = "org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService";

	private static Logger log = LoggerFactory
			.getLogger(JdbcDomainsSynchronizer.class);

	private String recipientMail;
	private InputService inputService;
	private String senderMail = SENDER_MAIL;
	private String outputService = OUTPUT_SERVICE;
	private DomainAdLdapRepository repository;
	private Boolean useAliases = false;

	@Autowired(required = true)
	public JdbcDomainsSynchronizer(DomainAdLdapRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public void synchronize(String domain) {
		DomainAdLdap domainAd = null;
		try {
			if (repository == null) {
				log.error("repository is empty");
				return;
			}
			domainAd = repository.findDomainAdLdap(domain);
			if (domainAd == null) {
				log.error("domain " + domain + " not found");
				return;
			}
			List<String> domainAliases = null;
			if(useAliases){
				domainAliases = repository.findDomainAliases(domain);
			}
			GDSource source;
			source = new GDSource(domainAd.getUser(), domainAd.getPassword(),
					domain, domainAliases);
			List<Account> accounts = null;
			accounts = source.getAllPersonNames();

			if (accounts != null) {
				List<String> managedList = repository
						.getManagedAccounts(domain);
				List<String> notManagedList = repository
						.getNotManagedAccounts(domain);
				Set<String> managedSet = new HashSet<String>();
				Set<String> deleteSet = new HashSet<String>();
				Set<String> notManagedSet = new HashSet<String>();
				if (managedList != null) {
					managedSet.addAll(managedList);
					deleteSet.addAll(managedList);
				}
				if (notManagedList != null) {
					notManagedSet.addAll(notManagedList);
				}

				for (Account account : accounts) {
					if (deleteSet.contains(account.getUid())) {
						deleteSet.remove(account.getUid());
					}
				}
				for (String deleteAccount : deleteSet) {
					repository.deleteAccount(deleteAccount, domain);
				}
				for (Account account : accounts) {
					if (!account.getMails().contains(
							account.getUid() + "@" + domain)) {
						account.getMails().add(account.getUid() + "@" + domain);
					}
					try{
						if (managedSet.contains(account.getUid())) {
							repository.updateAliasesAccount(account.getUid(),
									domain,
									new ArrayList<String>(account.getMails()));
							managedSet.remove(account.getUid());
						} else if (notManagedSet.contains(account.getUid())) {
							if (domainAd.getOverrideFlag()) {
								repository.updateAliasesAccount(account.getUid(),
										domain,
										new ArrayList<String>(account.getMails()));
							}
						} else {
							repository.insertAccount(account.getUid(), domain,
									new ArrayList<String>(account.getMails()));
						}
					}catch(Exception e){
						log.warn("Error sync account "+account.toString(),e);
					}
				}
			}
			repository.updateNextAdLdapCheck(domain);
		} catch (Exception e) {
			String message = e.toString();
			if (message.length() > 250) {
				message = message.substring(0, 250);
			}
			repository.updateErrorAdLdapCheck(domain, message);
			sendMail(domainAd, e.getMessage());
			throw new RuntimeException(e.getMessage(),e);
		}
	}

	private void sendMail(DomainAdLdap domainAd, String errorMessage) {
		if (inputService != null && domainAd != null
				&& domainAd.getNotifyEmail() != null) {
			if(recipientMail!=null && !recipientMail.isEmpty()){
				MailSender.sendMail(inputService, recipientMail,
						"domain: "+domainAd.getDomainId()+"\n\n"+errorMessage, senderMail, outputService);
			}else{
				MailSender.sendMail(inputService, domainAd.getNotifyEmail(),
						errorMessage, senderMail, outputService);
			}
		}
	}

	public InputService getInputService() {
		return inputService;
	}

	public void setInputService(InputService inputService) {
		this.inputService = inputService;
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

	public Boolean getUseAliases() {
		return useAliases;
	}

	public void setUseAliases(Boolean useAliases) {
		this.useAliases = useAliases;
	}

	public String getRecipientMail() {
		return recipientMail;
	}

	public void setRecipientMail(String recipientMail) {
		this.recipientMail = recipientMail;
	}

}
