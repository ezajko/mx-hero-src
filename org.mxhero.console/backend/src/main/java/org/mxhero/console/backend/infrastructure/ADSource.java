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

package org.mxhero.console.backend.infrastructure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class ADSource {

	public static final String SYNC_TYPE="adladp";
	public static final String ZIMBRA_TYPE="zimbra";
	public static final String EXCHANGE_TYPE="exchange";
	public static final String ZIMBRA_FILTER="(&(|(objectclass=zimbraAccount)(objectclass=zimbraDistributionList))(mail=*))";
	public static final String EXCHANGE_FILTER="(&(objectclass=user)(mail=*))";
	
	private LdapTemplate tpl = null;
	private LdapContextSource source = null;

	public ADSource(String address, String port, String user, String password, boolean ssl, String base) throws Exception {
		String connectionAddress="ldap://";
		if(ssl){
			connectionAddress="ldaps://";
		}
		connectionAddress=connectionAddress+address+":"+port;
		source = new LdapContextSource();
		source.setUrl(connectionAddress);
		source.setPassword(password);
		source.setUserDn(user);
		source.setBase(base);
		source.afterPropertiesSet();
		tpl = new LdapTemplate(source);
	}

	@SuppressWarnings("unchecked")
	public List<Account> getAllPersonNames(String filter) {
		String realFilter = "(objectClass=*)";
		if(filter!=null && filter.length()>0){
			realFilter=filter;
		}
		return tpl.search("", realFilter,
				new AttributesMapper() {
					public Account mapFromAttributes(Attributes attrs)
							throws NamingException {
						String id="uid";
						if(attrs.get(id)==null){
							if(attrs.get("sAMAccountName")==null){
								throw new NamingException("no uid or sAMAccountName fields found in this object");
							}else{
								id="sAMAccountName";
							}
						}
						if(attrs.get("mail")==null){
							throw new NamingException("no mail field found in this object");
						}
						Account account = new Account();
						account.setUid(attrs.get(id).get().toString());
						
						if(attrs.get("mail")!=null && attrs.get("mail").size()>0){
							account.setMails(new HashSet<String>());
							for(int i=0;i<attrs.get("mail").size();i++){
								account.getMails().add(attrs.get("mail").get(i).toString().toLowerCase().trim());
							}
						}
						
						return account;
					}
				});
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
