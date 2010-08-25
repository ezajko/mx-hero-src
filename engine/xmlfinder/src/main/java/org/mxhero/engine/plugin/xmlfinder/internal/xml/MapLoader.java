package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainXML;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;
import org.mxhero.engine.plugin.xmlfinder.internal.model.GroupXML;
import org.mxhero.engine.plugin.xmlfinder.internal.model.UserXML;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates maps for user and domain using their alias to facilitate faster searches.
 * @author mmarmol
 */
public final class MapLoader {

	private static Logger log = LoggerFactory.getLogger(MapLoader.class);
	
	private static Map<String,DomainXML> domains = new HashMap<String, DomainXML>();
	
	/**
	 * Empty constructor.
	 */
	private MapLoader(){
		
	}
	
	/**
	 * Creates the map from a DomainListXML.
	 * @param list loaded from the xml files, contains all the domain and user data.
	 */
	public static void createMap(DomainListXML list){
		Map<String,DomainXML> newDomains = new HashMap<String, DomainXML>();
		Map<String,DomainXML> oldDomains = domains;
		log.debug("domains cleared");
		if(list!=null){
			for (DomainXML domain : list.getDomains()){
				log.debug("loading domain "+domain);
				for(String domainAlias : domain.getAliases()){
					log.debug("adding domain "+domain.getId());
					newDomains.put(domainAlias.trim().toLowerCase(Locale.US), domain);
				}
				
				domain.setUsersAliases(new HashMap<String, UserXML>());
				for (UserXML user : domain.getUsers()){
					log.debug("adding user "+user.getMail());
					for (String alias : user.getAliases()){
						domain.getUsersAliases().put(alias.trim().toLowerCase(Locale.US), user);
						log.debug("adding user alias "+user.getMail()+" alias "+alias.trim().toLowerCase(Locale.US));
					}
				}
				
				for (GroupXML group : domain.getGroups()){
					group.setAliases(new ArrayList<String>());
					log.debug("adding aliases for group "+group.getName());
					for (String userMail : group.getMails()){
						UserXML user = domain.getUsersAliases().get(userMail.trim().toLowerCase(Locale.US));
						if (user!=null){
							log.debug("user found adding aliases "+ Arrays.toString(user.getAliases().toArray()) +" for "+user.getMail());
							group.getAliases().addAll(user.getAliases());
						}
					}
				}
			}
		}
		oldDomains.clear();
		domains=newDomains;
	}

	/**
	 * Return a domain from the alias or name string.
	 * @param domainKey
	 * @return
	 */
	public static DomainXML getDomain(String domainKey) {
		return domains.get(domainKey);
	}

	/**
	 * Returns a user using his alias or email for a particular domain.
	 * @param userKey
	 * @param domainKey
	 * @return
	 */
	public static UserXML getUser(String userKey, String domainKey) {
		DomainXML domainXML = getDomain(domainKey);
		if (domainXML!=null){
			log.debug("domaind for user found "+domainXML.getId());
			return domainXML.getUsersAliases().get(userKey);
		}
		return null;
	}

}
