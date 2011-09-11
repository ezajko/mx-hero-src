package org.mxhero.console.backend.vo;

import java.util.Collection;

public class LdapAccountVO {

	private String uid;
	
	private Collection<String> emails;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Collection<String> getEmails() {
		return emails;
	}

	public void setEmails(Collection<String> emails) {
		this.emails = emails;
	}

}
