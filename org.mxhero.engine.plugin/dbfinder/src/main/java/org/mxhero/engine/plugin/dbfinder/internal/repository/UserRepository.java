package org.mxhero.engine.plugin.dbfinder.internal.repository;

import java.util.Map;

import org.mxhero.engine.commons.mail.business.Domain;
import org.mxhero.engine.commons.mail.business.User;

public interface UserRepository {
	
	Map<String, User> getUsers();
	
	Map<String, Domain> getDomains();

}
