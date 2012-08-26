package org.mxhero.engine.plugin.boxserver.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.mxhero.engine.plugin.boxserver.BoxCloudStorage;
import org.mxhero.engine.plugin.boxserver.exceptions.NonAuthorizeException;
import org.mxhero.engine.plugin.boxserver.util.HeaderAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * The Class SecurityInterceptor.
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter{
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	/** The storage. */
	@Autowired
	private BoxCloudStorage storage;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.debug("Performing  authentication");
		String header = request.getHeader("Authorization");
		logger.debug("Authenticate application {}", header);
		if(StringUtils.isEmpty(header)){
			logger.debug("Header Authorization: mxHeroApi app_key=APP_KEY has not been set");
			throw new NonAuthorizeException("You have to set Authorization header. See documentation");
		}
		String appKey = HeaderAuthorization.getAppKeyFromHeader(header);
		boolean application = false;
		if(appKey!=null){
			try {
				application = storage.authenticateApplication(appKey);
			} catch (Exception e) {
				logger.debug("Application is not authorize with header {}", header);
				throw new NonAuthorizeException("You are not authorize to use this application");
			}
		}else{
			logger.debug("Header Authorization: mxHeroApi app_key=APP_KEY has not been set");
			throw new NonAuthorizeException("You have to set Authorization header. See documentation");
		}
		if(!application){
			logger.debug("Application is not authorize with header {}", header);
			throw new NonAuthorizeException("You are not authorize to use this application");
		}
		return super.preHandle(request, response, handler);
	}
	
}
