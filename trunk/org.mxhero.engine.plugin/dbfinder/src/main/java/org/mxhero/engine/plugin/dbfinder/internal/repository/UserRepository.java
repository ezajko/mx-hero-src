package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.Map;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;

/**
 * @author mmarmol
 *
 */
public interface UserRepository {
	
	/**
	 * @return
	 */
	Map<String, User> getUsers();
	
	/**
	 * @return
	 */
	Map<String, Domain> getDomains();

}
