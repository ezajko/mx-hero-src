package org.mxhero.engine.plugin.xmlfinder.internal.service;

import java.util.Locale;

import org.mxhero.engine.domain.mail.business.Domain;
import org.mxhero.engine.domain.mail.finders.DomainFinder;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainXML;
import org.mxhero.engine.plugin.xmlfinder.internal.xml.MapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the DomainFinder interface. Looks in the data collected from the
 * xml files to load the domain and data related and return it.
 * 
 * @author mmarmol
 */
public class XMLDomainFinder implements DomainFinder {

	private static Logger log = LoggerFactory.getLogger(XMLDomainFinder.class);

	/**
	 * @see org.mxhero.engine.domain.mail.finders.DomainFinder#getDomain(java.lang.String)
	 */
	@Override
	public Domain getDomain(String domainId) {
		String domainIdFormated = null;
		Domain domain = null;
		if (domainId != null) {
			domainIdFormated = domainId.trim().toLowerCase(Locale.US);
			DomainXML domainXML = MapLoader.getDomain(domainIdFormated);
			if (domainXML != null) {
				domain = domainXML.getDomain();
			}
		}
		log.debug("return domain " + domain + " for id " + domainIdFormated);
		return domain;
	}

}
