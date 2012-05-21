package org.mxhero.webapi.security.service;

import java.util.List;
import java.util.Map;

public interface UserFinder {

	public Map<String, Object> loadUserByUsername(String username);
	
	public List<String> getAuthorities(Integer userId);
	
}
