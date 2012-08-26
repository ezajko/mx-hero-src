package org.mxhero.engine.plugin.boxserver.controller;

import org.mxhero.engine.plugin.boxserver.BoxCloudStorage;
import org.mxhero.engine.plugin.boxserver.request.KeyRequest;
import org.mxhero.engine.plugin.boxserver.request.TokenRequest;
import org.mxhero.engine.plugin.boxserver.response.CreateKeyResponse;
import org.mxhero.engine.plugin.boxserver.response.CreateTokenResponse;
import org.mxhero.engine.plugin.boxserver.response.ErrorResponse;
import org.mxhero.engine.plugin.boxserver.response.TokenResponse;
import org.mxhero.engine.plugin.boxserver.service.UserBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class AttachmentController.
 */
@Controller
public class AttachmentController {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(AttachmentController.class);
	
	/** The storage. */
	@Autowired
	private BoxCloudStorage storage;

	@RequestMapping(value = "/attach/application/key/create", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CreateKeyResponse createAppKey(@RequestBody final KeyRequest req) {
		logger.debug("Attachment has request new app key for app {} ",req.getName());
		String appKey = storage.createApplicationKey(req.getName());
		return new CreateKeyResponse(appKey);
	}

	/**
	 * Authorize user.
	 *
	 * @param email the email
	 * @return the string
	 */
	@RequestMapping(value = "/secure/attach/token/create", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CreateTokenResponse createToken(@RequestBody final TokenRequest req) {
		logger.debug("Attachment has request new token for user {} ",req.getEmail());
		UserBox createAccount = storage.createAccount(req.getEmail());
		return new CreateTokenResponse(createAccount);
	}

	
	@RequestMapping(value = "/secure/attach/token", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TokenResponse getToken(@RequestBody final TokenRequest req) {
		logger.debug("Attachment has request retrieve token for user {} ",req.getEmail());
		UserBox token = storage.getToken(req.getEmail());
		return new TokenResponse(token);
	}
	
	@ExceptionHandler
	public @ResponseBody ErrorResponse handleException(Exception ex){
		logger.warn("Exception {} - Message {}",ex.getClass().getName(), ex.getMessage());
	    return new ErrorResponse(ex.getMessage());
	}
	
}
