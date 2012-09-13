/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	private List<String> aliases;
	
	public GDSource(String consumerKey, String consumerSecret, String domain, List<String> aliases) {
		if(consumerKey==null || consumerSecret==null || domain==null){
			throw new IllegalArgumentException("Null Parameter");
		}
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.domain = domain;
		this.aliases = aliases;
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
			if(aliases!=null && aliases.size()>0){
				for(String domainAlias : aliases){
					accountsMap.get(userEntry.getLogin().getUserName()).add(userEntry.getLogin().getUserName()+"@"+domainAlias);
				}
			}else{
				accountsMap.get(userEntry.getLogin().getUserName()).add(userEntry.getLogin().getUserName()+"@"+domain);
			}
			log.debug(" UserName: "+userEntry.getLogin().getUserName());
		}
		
		for(GenericEntry group : client.getGroupService().retrieveAllGroups().getEntries()){
			Account groupAccount = new Account();
			groupAccount.setUid(group.getProperty("groupId").split("@")[0]);
			groupAccount.setMails(new HashSet<String>());
			if(aliases!=null && aliases.size()>0){
				for(String domainAlias : aliases){
					groupAccount.getMails().add(group.getProperty("groupId").split("@")[0]+"@"+domainAlias);
				}
			}else{
				groupAccount.getMails().add(group.getProperty("groupId"));
			}
			accounts.add(groupAccount);
			log.debug("Mail-List: "+group.getProperty("groupId"));
		}
		
		for(NicknameEntry nicknameEntry : client.retrieveAllNicknames().getEntries()){
			if(!accountsMap.containsKey(nicknameEntry.getLogin().getUserName())){
				accountsMap.put(nicknameEntry.getLogin().getUserName(), new HashSet<String>());
			}
			if(aliases!=null && aliases.size()>0){
				for(String domainAlias : aliases){
					accountsMap.get(nicknameEntry.getLogin().getUserName()).add(nicknameEntry.getNickname().getName()+"@"+domainAlias);
				}
			}else{
				accountsMap.get(nicknameEntry.getLogin().getUserName()).add(nicknameEntry.getNickname().getName()+"@"+domain);
			}
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
