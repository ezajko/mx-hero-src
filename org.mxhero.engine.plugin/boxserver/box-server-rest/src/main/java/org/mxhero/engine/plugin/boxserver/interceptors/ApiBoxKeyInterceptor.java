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
public class ApiBoxKeyInterceptor extends HandlerInterceptorAdapter{
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ApiBoxKeyInterceptor.class);
	
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
		logger.debug("Authenticate api box mxhero module {}", header);
		if(StringUtils.isEmpty(header)){
			logger.debug("Header Authorization: mxHeroApi app_key=APP_KEY&module_key=MODULE_KEY has not been set");
			throw new NonAuthorizeException("You have to set ApiBoxAuth header. See documentation");
		}
		String[] splitHeader = header.split("&");
		if(splitHeader.length!=2){
			logger.debug("Header Authorization: mxHeroApi app_key=APP_KEY&module_key=MODULE_KEY has not been set");
			throw new NonAuthorizeException("You have to set ApiBoxAuth header. See documentation");
		}
		String apiBoxKey = HeaderAuthorization.getBoxKeyFromHeader(splitHeader[1]);
		String appKey = HeaderAuthorization.getAppKeyFromHeader(splitHeader[0]);
		boolean application = false;
		if(apiBoxKey!=null && appKey!=null){
			try {
				application = storage.authenticateApplication(appKey) && storage.authenticateModule(apiBoxKey);
			} catch (Exception e) {
				logger.debug("Module is not authorize to get Api Box key with header {}", header);
				throw new NonAuthorizeException("You are not authorize to use this application");
			}
		}else{
			logger.debug("Header ApiBoxAuth:  mxHeroApi app_key=APP_KEY&module_key=MODULE_KEY has not been set");
			throw new NonAuthorizeException("You have to set ApiBoxAuth header. See documentation");
		}
		if(!application){
			logger.debug("Module is not authorize to get Api Box key with header {}", header);
			throw new NonAuthorizeException("You are not authorize to use this application");
		}
		return super.preHandle(request, response, handler);
	}
	
}
