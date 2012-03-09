package org.mxhero.engine.plugin.gsync.internal.gdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthParameters.OAuthType;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;

public class GDSource {

	private static Logger log = LoggerFactory.getLogger(GDSource.class);
	
	private String consumerKey;
	private String consumerSecret;
	private String domain;
	
	public GDSource(String consumerKey, String consumerSecret, String domain) {
		if(consumerKey==null || consumerSecret==null || domain==null){
			throw new IllegalArgumentException("Null Parameter");
		}
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.domain = domain;
	}

	public List<Account> getAllPersonNames() throws Exception {
		List<Account> accounts = new ArrayList<Account>();
		Map<String,Set<String>> accountsMap = new HashMap<String, Set<String>>();
		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey(consumerKey);
		oauthParameters.setOAuthConsumerSecret(consumerSecret);
		oauthParameters.setOAuthType(OAuthType.TWO_LEGGED_OAUTH);

		AppsForYourDomainClient client = new AppsForYourDomainClient(
				oauthParameters, domain);
		
		for(UserEntry userEntry : client.retrieveAllUsers().getEntries()){
			if(!accountsMap.containsKey(userEntry.getLogin().getUserName())){
				accountsMap.put(userEntry.getLogin().getUserName(), new HashSet<String>());
			}
			accountsMap.get(userEntry.getLogin().getUserName()).add(userEntry.getLogin().getUserName()+"@"+domain);
			log.debug(" UserName: "+userEntry.getLogin().getUserName());
		}
		
		for(GenericEntry group : client.getGroupService().retrieveAllGroups().getEntries()){
			Account groupAccount = new Account();
			groupAccount.setUid(group.getProperty("groupId").split("@")[0]);
			groupAccount.setMails(new HashSet<String>());
			groupAccount.getMails().add(group.getProperty("groupId"));
			accounts.add(groupAccount);
			log.debug("Mail-List: "+group.getProperty("groupId"));
		}
		
		for(NicknameEntry nicknameEntry : client.retrieveAllNicknames().getEntries()){
			if(!accountsMap.containsKey(nicknameEntry.getLogin().getUserName())){
				accountsMap.put(nicknameEntry.getLogin().getUserName(), new HashSet<String>());
			}
			accountsMap.get(nicknameEntry.getLogin().getUserName()).add(nicknameEntry.getNickname().getName()+"@"+domain);
			log.debug("Nickname: "+nicknameEntry.getNickname().getName()+" UserName: "+nicknameEntry.getLogin().getUserName());
		}
		
		for(Entry<String,Set<String>> entry: accountsMap.entrySet()){
			Account account = new Account();
			account.setMails(entry.getValue());
			account.setUid(entry.getKey());
			accounts.add(account);
		}
		
		return accounts;
	}
	
	public class Account {
		private String uid;
		private Set<String> mails;

		public Account(){		
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public Set<String> getMails() {
			return mails;
		}

		public void setMails(Set<String> mails) {
			this.mails = mails;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Account [uid=").append(uid).append(", mails=")
					.append(mails).append("]");
			return builder.toString();
		}
		
	}
}
