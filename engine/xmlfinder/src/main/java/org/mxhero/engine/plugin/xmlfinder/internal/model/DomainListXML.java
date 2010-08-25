package org.mxhero.engine.plugin.xmlfinder.internal.model;

import java.util.Collection;

/**
 * Represents a list of Domains in a XML format
 * @author mmarmol
 */
public class DomainListXML {

	private Collection<DomainXML> domains;
	
	/**
	 * Empty constructor
	 */
	public DomainListXML(){
		
	}

	/**
	 * @return
	 */
	public Collection<DomainXML> getDomains() {
		return domains;
	}

	/**
	 * @param domains
	 */
	public void setDomains(Collection<DomainXML> domains) {
		this.domains = domains;
	}
	
}
