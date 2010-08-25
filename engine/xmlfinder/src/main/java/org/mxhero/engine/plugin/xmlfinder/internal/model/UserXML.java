package org.mxhero.engine.plugin.xmlfinder.internal.model;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.business.UserList;

/**
 * Representes a User in a XML format.
 * @author mmarmol
 */
public class UserXML {

	private String mail;
	
	private Collection<MailListXML> mailLists;
	
	private Collection<String> aliases;

	/**
	 * @return
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return
	 */
	public Collection<MailListXML> getMailLists() {
		return mailLists;
	}

	/**
	 * @param mailLists
	 */
	public void setMailLists(Collection<MailListXML> mailLists) {
		this.mailLists = mailLists;
	}

	/**
	 * @return
	 */
	public Collection<String> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases
	 */
	public void setAliases(Collection<String> aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * @return
	 */
	public User getUser(){
		User user = new User();
		user.setMail(mail);
		user.setAliases(new ArrayList<String>());
		if(aliases!=null){
			user.getAliases().addAll(aliases);
		}
		user.setLists(new ArrayList<UserList>());
		for(MailListXML mailListXML : this.mailLists){
			user.getLists().add(mailListXML.getUserList());
		}
		return user;
	}

}
