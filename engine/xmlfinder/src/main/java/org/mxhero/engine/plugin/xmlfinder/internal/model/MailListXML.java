package org.mxhero.engine.plugin.xmlfinder.internal.model;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.engine.domain.mail.business.DomainList;
import org.mxhero.engine.domain.mail.business.UserList;

/**
 * Representes a list of mails in a xml format.
 * @author mmarmol
 */
public class MailListXML{

	private String name;
	
	private Collection<String> mails;

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public Collection<String> getMails() {
		return mails;
	}

	/**
	 * @param mails
	 */
	public void setMails(Collection<String> mails) {
		this.mails = mails;
	}
	
	/**
	 * @return
	 */
	public UserList getUserList(){
		UserList mailList = new UserList();
		mailList.setName(name);
		mailList.setMails(new ArrayList<String>());
		if(mails!=null){
			mailList.getMails().addAll(mails);
		}
		return mailList;
	}
	
	/**
	 * @return
	 */
	public DomainList getDomainList(){
		DomainList mailList = new DomainList();
		mailList.setName(name);
		mailList.setMails(new ArrayList<String>());
		if(mails!=null){
			mailList.getMails().addAll(mails);
		}
		return mailList;
	}
}
