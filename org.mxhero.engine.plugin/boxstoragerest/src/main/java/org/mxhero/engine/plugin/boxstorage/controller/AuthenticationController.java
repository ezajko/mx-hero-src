package org.mxhero.engine.plugin.boxstorage.controller;

import org.mxhero.engine.plugin.boxstorage.internal.client.BoxCloudStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class AuthenticationController.
 */
@Controller
public class AuthenticationController {

	/** The storage. */
	@Autowired
	private BoxCloudStorageClient storage;

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
	@RequestMapping(value = "/register/token", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String registerToken(
			@RequestParam(value = "token") final String token,
			@RequestParam(value = "email") final String email) {
		logger.debug(
				"User Box {} has been authenticated with token {}",
				email, token);
		storage.registerToken(email, token);
		return "auth";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public String handleException(Exception ex) {
		logger.warn("Exception {} - Message {}", ex.getClass().getName(),
				ex.getMessage());
		return "error";
	}

}
