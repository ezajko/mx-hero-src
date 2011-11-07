package org.mxhero.engine.plugin.adsync.internal.source;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

public class ADSource {

	private LdapTemplate tpl = null;
	private LdapContextSource source = null;
	private static Logger log = LoggerFactory.getLogger(ADSource.class);
	
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
		log.debug("Ldap search with "+this.toString()+ " and filter="+realFilter);
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ADSource [urls=").append(Arrays.deepToString(source.getUrls()))
		.append(", principal=").append(source.getAuthenticationSource().getPrincipal())
		.append(", credentials=").append(source.getAuthenticationSource().getCredentials())
		.append(", base=").append(source.getBaseLdapPathAsString())
		.append("]");
		return builder.toString();
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
