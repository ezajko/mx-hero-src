package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.Map;

import org.mxhero.engine.commons.domain.Domain;
import org.mxhero.engine.commons.domain.User;

public interface UserRepository {
	
	Map<String, User> getUsers();
	
	Map<String, Domain> getDomains();

}
