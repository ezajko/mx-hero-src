package org.mxhero.engine.plugin.adsync.source;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class ADSource {

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
						if(attrs.get("uid")==null ||
								attrs.get("mail")==null){
							throw new NamingException("no uid and/or mail fields found in this object");
						}
						Account account = new Account();
						account.setUid(attrs.get("uid").get().toString());
						
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
