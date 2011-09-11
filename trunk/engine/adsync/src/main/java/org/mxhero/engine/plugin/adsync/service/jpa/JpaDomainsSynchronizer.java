package org.mxhero.engine.plugin.adsync.service.jpa;


import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.mxhero.engine.domain.connector.InputService;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.plugin.adsync.dao.DomainAdLdapDao;
import org.mxhero.engine.plugin.adsync.dao.DomainAliasDao;
import org.mxhero.engine.plugin.adsync.dao.EmailAccountDao;
import org.mxhero.engine.plugin.adsync.dao.FeatureRuleDao;
import org.mxhero.engine.plugin.adsync.entity.AliasAccount;
import org.mxhero.engine.plugin.adsync.entity.AliasAccountPk;
import org.mxhero.engine.plugin.adsync.entity.Domain;
import org.mxhero.engine.plugin.adsync.entity.DomainAdLdap;
import org.mxhero.engine.plugin.adsync.entity.DomainAlias;
import org.mxhero.engine.plugin.adsync.entity.EmailAccount;
import org.mxhero.engine.plugin.adsync.entity.EmailAccountPk;
import org.mxhero.engine.plugin.adsync.entity.FeatureRule;
import org.mxhero.engine.plugin.adsync.service.DomainsSynchronizer;
import org.mxhero.engine.plugin.adsync.source.ADSource;
import org.mxhero.engine.plugin.adsync.source.ADSource.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class JpaDomainsSynchronizer implements DomainsSynchronizer{

	public static final String SYNC_TYPE="adladp";
	
	public static final String ZIMBRA_TYPE="zimbra";
	public static final String EXCHANGE_TYPE="exchange";
	public static final String ZIMBRA_FILTER="(&(|(objectclass=zimbraAccount)(objectclass=zimbraDistributionList))(mail=*))";
	public static final String EXCHANGE_FILTER="(&(objectclass=user)(mail=*))";
	public static final String SENDER_MAIL = "adsync@mxhero.com";
	public static final String OUTPUT_SERVICE = "org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService";
	
	private static Logger log = LoggerFactory.getLogger(JpaDomainsSynchronizer.class);
	
	private InputService inputService;
	
	private String zimbraFilter=ZIMBRA_FILTER;
	
	private String exchangeFilter=EXCHANGE_FILTER;
	
	private String senderMail=SENDER_MAIL;
	
	private String outputService=OUTPUT_SERVICE;
	
	private DomainAdLdapDao dao;
	
	private EmailAccountDao emailAccountDao;
	
	private DomainAliasDao domainAliasDao; 
	
	private FeatureRuleDao ruleDao;

	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW)
	public void synchronize(String domain) {
		DomainAdLdap domainAd = null;
		try{
			if(dao==null){
				log.error("dao is empty");
				return;
			}
			domainAd=dao.readByPrimaryKey(domain);
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
			
			ADSource source = new ADSource(domainAd.getAddres(), domainAd.getPort(), domainAd.getUser(), domainAd.getPassword(), domainAd.getSslFlag(), domainAd.getBase());
			List<Account> accounts = source.getAllPersonNames(filter);
			if(accounts!=null){
				for(Account account : accounts){
					if(!account.getMails().contains(account.getUid()+"@"+domain)){
						account.getMails().add(account.getUid()+"@"+domain);
					}
					syncAccount(account,domainAd.getDomain(),domainAd.getOverrideFlag());
				}
			}
			Set<EmailAccount> accountToCheck = new HashSet<EmailAccount>(domainAd.getDomain().getEmailAccounts());
			for (EmailAccount existingAccount : accountToCheck){
				boolean exists = false;
				for(Account syncAccount : accounts){
					if(syncAccount.getUid().equalsIgnoreCase(existingAccount.getId().getAccount())){
						exists=true;
						break;
					}
				}
				//if account is not in the list from the source and is managed, we need to remove it.
				if(!exists && existingAccount.getDataSource().equalsIgnoreCase(SYNC_TYPE)){
					domainAd.getDomain().getEmailAccounts().remove(existingAccount);
					
					List<FeatureRule> rules = ruleDao.findRulesByAccount(existingAccount.getId().getAccount(), existingAccount.getId().getDomainId());
					if(rules!=null){
						for(FeatureRule rule : rules){
							ruleDao.delete(rule);
						}
					}
					emailAccountDao.deleteAliasAccountByAccount(existingAccount.getId().getAccount(), existingAccount.getId().getDomainId());
					emailAccountDao.deleteAccount(existingAccount.getId().getAccount(), existingAccount.getId().getDomainId());
				}
			}
			domainAd.setLastUpdate(Calendar.getInstance());
			Calendar nextUpdate = Calendar.getInstance();
			nextUpdate.add(Calendar.HOUR, 1);
			domainAd.setNextUpdate(nextUpdate);
			domainAd.setError(null);
			dao.saveAndFlush(domainAd);
		}catch(Exception e){
			log.warn("domain "+domain+" had an error while processing",e);

			//read again
			domainAd=dao.readByPrimaryKey(domain);

			sendMail(domainAd,e.getMessage());

			Calendar nextUpdate = Calendar.getInstance();
			nextUpdate.add(Calendar.HOUR, 1);
			domainAd.setError(e.getMessage());
			domainAd.setNextUpdate(nextUpdate);
			domainAd.setLastUpdate(Calendar.getInstance());
			dao.save(domainAd);
		}
	}

	private void syncAccount(Account account, Domain domain, Boolean override){
		EmailAccountPk pk = new EmailAccountPk();
		pk.setAccount(account.getUid());
		pk.setDomainId(domain.getDomain());
		EmailAccount emailAccount = emailAccountDao.readByPrimaryKey(pk);
		//exists, override it if managed or flag true
		if(emailAccount!=null && (emailAccount.getDataSource().equalsIgnoreCase(SYNC_TYPE) || override)){
			emailAccount.setUpdatedDate(Calendar.getInstance());
			emailAccount.setDataSource(SYNC_TYPE);

			for(String mail : account.getMails()){
				boolean create=true;
				String accountName = mail.split("@")[0];
				String domainName = mail.split("@")[1];
				for(AliasAccount existingAlias : emailAccount.getAliases()){
					if(existingAlias.getId().getAccountAlias().equalsIgnoreCase(accountName)
							&& existingAlias.getId().getDomainAlias().equalsIgnoreCase(domainName)){
						create=false;
						break;
					}
				}
				if(create){
					DomainAlias domainAlias = domainAliasDao.readByPrimaryKey(domainName);
					if(domainAlias==null){
						throw new RuntimeException("domain alias does not exists, "+domainName);
					}
					AliasAccount aliasAccount = new AliasAccount();
					aliasAccount.setAccount(emailAccount);
					aliasAccount.setDomainAlias(domainAlias);
					aliasAccount.setCreated(Calendar.getInstance());
					aliasAccount.setDataSource(SYNC_TYPE);
					aliasAccount.setId(new AliasAccountPk(accountName, domainName));
					emailAccount.getAliases().add(aliasAccount);
				}
			}
		}
		//this is a new account
		else if(emailAccount==null){
			emailAccount = new EmailAccount();
			emailAccount.setId(pk);
			emailAccount.setCreatedDate(Calendar.getInstance());
			emailAccount.setUpdatedDate(Calendar.getInstance());
			emailAccount.setDataSource(SYNC_TYPE);		
			emailAccount.setDomain(domain);
			domain.getEmailAccounts().add(emailAccount);
			emailAccount.setAliases(new HashSet<AliasAccount>());
			for(String mail : account.getMails()){
				String accountName = mail.split("@")[0];
				String domainName = mail.split("@")[1];
				DomainAlias domainAlias = domainAliasDao.readByPrimaryKey(domainName);
				if(domainAlias==null){
					throw new RuntimeException("domain alias does not exists, "+domainName);
				}
				AliasAccount aliasAccount = new AliasAccount();
				aliasAccount.setAccount(emailAccount);
				aliasAccount.setDomainAlias(domainAlias);
				aliasAccount.setCreated(Calendar.getInstance());
				aliasAccount.setDataSource(SYNC_TYPE);
				aliasAccount.setId(new AliasAccountPk(accountName, domainName));
				emailAccount.getAliases().add(aliasAccount);
			}
		}
		
	}
	
	private void sendMail(DomainAdLdap domainAd,String errorMessage){
		if(inputService!=null 
				&& domainAd!=null 
				&& domainAd.getDomain()!=null
				&& domainAd.getDomain().getOwner()!=null){
			try{
				InternetAddress recipient = new InternetAddress(domainAd.getDomain().getOwner().getNotifyEmail(),false);
				InternetAddress sender = new InternetAddress(senderMail);
				
				MimeMessage newMessage = new MimeMessage(Session.getDefaultInstance(null));
				newMessage.setSender(sender);
				newMessage.setFrom(sender);
				newMessage.setReplyTo(new InternetAddress[] {sender});
				newMessage.setRecipient(RecipientType.TO, recipient);
				newMessage.setSubject("AD/LDAP SYNC ERROR");
				newMessage.setText(errorMessage);
				newMessage.setSentDate(Calendar.getInstance().getTime());
				newMessage.saveChanges();
				MimeMail newMail = new MimeMail(sender.toString(), recipient.toString(),
						newMessage, outputService);
				newMail.setPhase(RulePhase.SEND);
				inputService.addMail(newMail);
			}catch(Exception e){
				log.warn("Error while sending email",e);
			}
		}else{
			log.warn("Can not send email, inputservice is out");
		}
	}
	
	public InputService getInputService() {
		return inputService;
	}

	public void setInputService(InputService inputService) {
		this.inputService = inputService;
	}

	public String getOutputService() {
		return outputService;
	}

	public void setOutputService(String outputService) {
		this.outputService = outputService;
	}

	public DomainAdLdapDao getDao() {
		return dao;
	}

	public void setDao(DomainAdLdapDao dao) {
		this.dao = dao;
	}

	public EmailAccountDao getEmailAccountDao() {
		return emailAccountDao;
	}

	public void setEmailAccountDao(EmailAccountDao emailAccountDao) {
		this.emailAccountDao = emailAccountDao;
	}

	public DomainAliasDao getDomainAliasDao() {
		return domainAliasDao;
	}

	public void setDomainAliasDao(DomainAliasDao domainAliasDao) {
		this.domainAliasDao = domainAliasDao;
	}

	public FeatureRuleDao getRuleDao() {
		return ruleDao;
	}

	public void setRuleDao(FeatureRuleDao ruleDao) {
		this.ruleDao = ruleDao;
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

}
