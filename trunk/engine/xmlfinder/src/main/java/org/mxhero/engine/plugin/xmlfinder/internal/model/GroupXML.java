package org.mxhero.engine.plugin.xmlfinder.internal.model;

import java.util.ArrayList;
import java.util.Collection;

import org.mxhero.engine.domain.mail.business.Group;

/**
 * Representes a Group of mails in XML format.
 * @author mmarmol
 */
public class GroupXML {

	private String name;
	
	private Collection<String> mails;
	
	private Collection<String> aliases;

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
	public Group getGroup(){
		Group group = new Group();
		group.setName(name);
		group.setAliases(new ArrayList<String>());
		if(aliases!=null){
			group.getAliases().addAll(aliases);
		}
		group.setMails(new ArrayList<String>());
		if(mails!=null){
			group.getMails().addAll(mails);
		}
		return group;
	}

}
