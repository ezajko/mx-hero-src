package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

/**
 * The Enum CodeResponse.
 */
public enum CodeResponse {

	/** The OK. */
	OK("Ok"), 
	
	/** The USE r_ alread y_ exist. */
	USER_ALREADY_EXIST("User already exist with this email"), 
	
	/** The BA d_ authentication. */
	BAD_AUTHENTICATION("Error authentication mxHero with Box"), 
	
	/** The BA d_ parameters. */
	BAD_PARAMETERS("Could not obtain token with this email"), 
	
	/** The UNKNOWN. */
	UNKNOWN("Unknown error"), 
	
	/** The FILE s_ couldn t_ b e_ uploaded. */
	FILES_COULDNT_BE_UPLOADED("Files could not be uploaded"),
	
	/** The FILE s_ couldn t_ b e_ uploaded. */
	SHARED_LINK_COULD_NOT_BE_CREATED("Shared link could not be created");

	/** The message. */
	private String message;

	/**
	 * Instantiates a new code response.
	 *
	 * @param message the message
	 */
	private CodeResponse(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}
	
}
