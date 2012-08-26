package org.mxhero.engine.plugin.boxserver.controller;

import org.mxhero.engine.plugin.boxserver.BoxCloudStorage;
import org.mxhero.engine.plugin.boxserver.caching.Application;
import org.mxhero.engine.plugin.boxserver.caching.TicketCaching;
import org.mxhero.engine.plugin.boxserver.exceptions.NonAuthorizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The Class AuthenticationController.
 */
@Controller
public class AuthenticationController {

	/** The storage. */
	@Autowired
	private BoxCloudStorage storage;

	@Value("${ticket.redirect.url}")
	private String urlInitTicket;
	
	@Autowired
	private TicketCaching caching;
	
	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(AuthenticationController.class);

	/**
	 * Authorize user.
	 * 
	 * @param ticket
	 *            the ticket
	 * @param token
	 *            the token
	 * @return the string
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public RedirectView authorizeUser(
			@RequestParam(value = "ticket") final String ticket,
			@RequestParam(value = "auth_token") final String token) {
		logger.debug(
				"User Box has been authenticated with ticket {} and token {}",
				ticket, token);
		Application app = caching.getApp(ticket);
		if(app==null){
			logger.error("URL to notify client has not been set.");
			throw new RuntimeException("URL to notify client has not been set.");
		}
		String email = storage.registerToken(ticket, token, app.getAppKey());
		logger.debug("Notify client Url {} with token {} for email {}", new Object[]{app.getUrl(), token, email});
		StringBuilder urlToRedirect = new StringBuilder();
		urlToRedirect.append(app.getUrl());
		urlToRedirect.append("?token=");
		urlToRedirect.append(token);
		urlToRedirect.append("&email=");
		urlToRedirect.append(email);
		return new RedirectView(urlToRedirect.toString());
	}

	/**
	 * Inits the authorization.
	 * 
	 * @param email
	 *            the email
	 * @return the view
	 */
	@RequestMapping(value = "/init/auth", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public View initAuthorization(
			@RequestParam(value = "resp_url") final String responseUrl, @RequestParam(value="auth_key") final String authorizationKey) {
		logger.debug("Init Authorization process for app {} with appKey {}", responseUrl, authorizationKey);
		String ticket = storage.getTicket();
		caching.setUrl(ticket, responseUrl, authorizationKey);
		String redirectUrl = urlInitTicket + ticket;
		logger.debug("Redirect to Box to authenticate {}", redirectUrl);
		return new RedirectView(redirectUrl);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public String handleException(Exception ex) {
		logger.warn("Exception {} - Message {}", ex.getClass().getName(),
				ex.getMessage());
		return "error";
	}

	@ExceptionHandler(NonAuthorizeException.class)
	@ResponseStatus(HttpStatus.OK)
	public String handleNonAuthorize(NonAuthorizeException ex) {
		logger.warn("Exception {} - Message {}", ex.getClass().getName(),
				ex.getMessage());
		return "nonauth";
	}
}
