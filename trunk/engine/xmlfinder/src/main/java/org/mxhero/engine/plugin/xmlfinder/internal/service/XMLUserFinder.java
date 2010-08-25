package org.mxhero.engine.plugin.xmlfinder.internal.service;

import java.util.Locale;

import org.mxhero.engine.domain.mail.business.User;
import org.mxhero.engine.domain.mail.finders.UserFinder;
import org.mxhero.engine.plugin.xmlfinder.internal.model.UserXML;
import org.mxhero.engine.plugin.xmlfinder.internal.xml.MapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the UserFinder interface. Search a user that is part of one domain
 * in the xml files and return it.
 * 
 * @author mmarmol
 */
public class XMLUserFinder implements UserFinder {

	private static Logger log = LoggerFactory.getLogger(XMLUserFinder.class);

	/**
	 * @see org.mxhero.engine.domain.mail.finders.UserFinder#getUser(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public User getUser(String userId, String domainId) {
		String userIdFormated = null;
		String domainIdFormated = null;
		User user = null;
		if (userId != null && domainId != null) {
			userIdFormated = userId.trim().toLowerCase(Locale.US);
			domainIdFormated = domainId.trim().toLowerCase(Locale.US);
			UserXML userXML = MapLoader.getUser(userIdFormated,
					domainIdFormated);
			if (userXML != null) {
				user = userXML.getUser();
			}
		}
		log.debug("return user " + user + " for userId " + userIdFormated
				+ " and domainId " + domainIdFormated);
		return user;
	}

}
