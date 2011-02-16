package org.mxhero.engine.plugin.xmlfinder.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.business.DomainList;
import org.mxhero.engine.domain.mail.business.Group;

/**
 * Representes a domain in a XML format
 * @author mmarmol
 */
public class DomainXML {

	private String id;
	
	private Collection<String> aliases;
	
	private Collection<MailListXML> mailLists;
	
	private Collection<GroupXML> groups;
	
	private Collection<UserXML> users;
	
	private Map<String,UserXML> usersAliases;
	
	/**
	 * @return
	 */
	public Collection<UserXML> getUsers() {
		return users;
	}

	/**
	 * @param users
	 */
	public void setUsers(Collection<UserXML> users) {
		this.users = users;
	}

	/**
	 * @return
	 */
	public Map<String, UserXML> getUsersAliases() {
		return usersAliases;
	}

	/**
	 * @param usersAliases
	 */
	public void setUsersAliases(Map<String, UserXML> usersAliases) {
		this.usersAliases = usersAliases;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
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
	public Collection<GroupXML> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 */
	public void setGroups(Collection<GroupXML> groups) {
		this.groups = groups;
	}

	/**
	 * @return
	 */
	public Domain getDomain(){
		Domain domain = new Domain();
		domain.setId(id);
		domain.setAliases(new ArrayList<String>());
		domain.setManaged(true);
		if (aliases!=null){
			domain.getAliases().addAll(aliases);
		}
		domain.setLists(new ArrayList<DomainList>());
		for (MailListXML mailListXML : mailLists){
			domain.getLists().add(mailListXML.getDomainList());
		}
		domain.setGroups(new ArrayList<Group>());
		for (GroupXML groupXML : groups){
			domain.getGroups().add(groupXML.getGroup());
		}
		return domain;
	}
	
}

