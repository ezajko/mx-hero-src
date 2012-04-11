package org.mxhero.engine.plugin.emailquarantine.repository;

public interface QuarantineRepository {

	String findEmail(String domain);
	
}
