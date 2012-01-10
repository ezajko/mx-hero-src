package org.mxhero.console.backend.security;

import java.util.List;
import java.util.Map;

import org.mxhero.console.backend.vo.AuthorityVO;

public interface UserFinder {

	public Map<String, Object> loadUserByUsername(String username);
	
	public List<AuthorityVO> getAuthorities(Integer userId);
	
}
