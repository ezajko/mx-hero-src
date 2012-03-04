package org.mxhero.console.backend.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.vo.SystemPropertyVO;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class PreAuthServlet extends HttpServlet {

	private final static String LOGIN_NAME = "loginname";
	private final static String DOMAIN = "domain";
	private final static String TIMESTAMP = "timestamp";
	private final static String EXPIRES = "expires";
	private final static String PREAUTH = "preauth";
	
	private final static String PRE_AUTH_KEY = "preauth.key";
	public final static String PER_AUTH_DEFAULT = "4e2816f16c44fab20ecdee39fb850c3b0bb54d03f1d8e073aaea376a4f407f0c";
	private final static String PRE_AUTH_EXPIRES = "preauth.expires";
	public final static long PRE_AUTH_EXPIRES_DEFAULT = 300000;
	
	private SystemPropertyRepository propertyRepository;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req,resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req,resp);
	}	
	
	private void doAction(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException{
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		AuthenticationManager manager = (AuthenticationManager)context.getBean("authenticationManager");
		propertyRepository = context.getBean(SystemPropertyRepository.class);
		String principal = getPrincipal(request.getParameter(LOGIN_NAME),request.getParameter(DOMAIN),request.getParameter(TIMESTAMP),request.getParameter(EXPIRES),request.getParameter(PREAUTH));
		if(principal!=null && !principal.isEmpty()){
			Authentication auth = new PreAuthenticatedAuthenticationToken(principal,request.getParameter(PREAUTH));
			auth = manager.authenticate(auth);
			if(auth.isAuthenticated()){
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		resp.sendRedirect("/");
	}
	
	private String getPrincipal(Object loginName, Object domain, Object timestamp, Object expires, Object preauth){
		//check parameters
		if(loginName==null || loginName.toString().trim().isEmpty() || 
			domain==null || domain.toString().trim().isEmpty() ||
			timestamp==null || timestamp.toString().trim().isEmpty() ||
			preauth==null || preauth.toString().trim().isEmpty() ||
			expires==null || expires.toString().trim().isEmpty()){
			return null;
		}
		long expiresTime = Long.parseLong(expires.toString());
		long timestampTime = Long.parseLong(timestamp.toString());
		if(expiresTime<1){
			expiresTime=getExpires();
		}
		if(timestampTime+expiresTime<System.currentTimeMillis()){
			return null;
		}
		String preAuthKey = getPreauthKey();
		if(preAuthKey==null){
			return null;
		}
		if(PreAuthEncoder.encode(loginName.toString().trim(), domain.toString().trim(), timestamp.toString().trim(), expires.toString().trim(), getPreauthKey()).equals(preauth.toString().trim().isEmpty())){
			return null;
		}
		return loginName.toString().trim();
	}
	
	private String getPreauthKey(){
		SystemPropertyVO property = propertyRepository.findById(PRE_AUTH_KEY);
		if(property==null){
			return PER_AUTH_DEFAULT;
		}
		return property.getPropertyValue();
	}

	private Long getExpires(){
		SystemPropertyVO property = propertyRepository.findById(PRE_AUTH_EXPIRES);
		if(property==null){
			return PRE_AUTH_EXPIRES_DEFAULT;
		}
		return Long.parseLong(property.getPropertyValue());
	}
}
