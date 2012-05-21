package org.mxhero.webapi.security;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class WebapiPermissionEvaluator implements PermissionEvaluator {

	private final static Logger log = Logger.getLogger(WebapiPermissionEvaluator.class);
	
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {
		log.debug(targetDomainObject.toString()+" "+permission.toString());
		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
