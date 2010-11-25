package org.mxhero.engine.domain.mail.business;

/**
 * Used to hold data of the diferent phases of a mail in the plataform.
 * @author mmarmol
 */
public final class MailState {
	
	public static final String DELIVER = "deliver";

	public static final String DROP = "drop";
	
	/**
	 * Just private, wont instantiate.
	 */
	private MailState(){
		
	}
}
