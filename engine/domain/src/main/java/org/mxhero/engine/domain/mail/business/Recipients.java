package org.mxhero.engine.domain.mail.business;

import java.util.Collection;

/**
 * Represents all the recipients that mail has so it can be used inside rules.
 * @author mmarmol
 */
public class Recipients {

	private Collection<String> allRecipients;
	
	private Collection<String> toRecipients;
	
	private Collection<String> bccRecipients;
	
	private Collection<String> ccRecipients;
	
	private Collection<String> ngRecipients;

	private String allRecipientsStr;
	
	private String toRecipientsStr;
	
	private String bccRecipientsStr;
	
	private String ccRecipientsStr;
	
	private String ngRecipientsStr;

	/**
	 * @return the allRecipients
	 */
	public Collection<String> getAllRecipients() {
		return allRecipients;
	}

	/**
	 * @param allRecipients the allRecipients to set
	 */
	public void setAllRecipients(Collection<String> allRecipients) {
		this.allRecipients = allRecipients;
	}

	/**
	 * @return the toRecipients
	 */
	public Collection<String> getToRecipients() {
		return toRecipients;
	}

	/**
	 * @param toRecipients the toRecipients to set
	 */
	public void setToRecipients(Collection<String> toRecipients) {
		this.toRecipients = toRecipients;
	}

	/**
	 * @return the bccRecipients
	 */
	public Collection<String> getBccRecipients() {
		return bccRecipients;
	}

	/**
	 * @param bccRecipients the bccRecipients to set
	 */
	public void setBccRecipients(Collection<String> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

	/**
	 * @return the ccRecipients
	 */
	public Collection<String> getCcRecipients() {
		return ccRecipients;
	}

	/**
	 * @param ccRecipients the ccRecipients to set
	 */
	public void setCcRecipients(Collection<String> ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

	/**
	 * @return the ngRecipients
	 */
	public Collection<String> getNgRecipients() {
		return ngRecipients;
	}

	/**
	 * @param ngRecipients the ngRecipients to set
	 */
	public void setNgRecipients(Collection<String> ngRecipients) {
		this.ngRecipients = ngRecipients;
	}

	/**
	 * @return the allRecipientsStr
	 */
	public String getAllRecipientsStr() {
		return allRecipientsStr;
	}

	/**
	 * @param allRecipientsStr the allRecipientsStr to set
	 */
	public void setAllRecipientsStr(String allRecipientsStr) {
		this.allRecipientsStr = allRecipientsStr;
	}

	/**
	 * @return the toRecipientsStr
	 */
	public String getToRecipientsStr() {
		return toRecipientsStr;
	}

	/**
	 * @param toRecipientsStr the toRecipientsStr to set
	 */
	public void setToRecipientsStr(String toRecipientsStr) {
		this.toRecipientsStr = toRecipientsStr;
	}

	/**
	 * @return the bccRecipientsStr
	 */
	public String getBccRecipientsStr() {
		return bccRecipientsStr;
	}

	/**
	 * @param bccRecipientsStr the bccRecipientsStr to set
	 */
	public void setBccRecipientsStr(String bccRecipientsStr) {
		this.bccRecipientsStr = bccRecipientsStr;
	}

	/**
	 * @return the ccRecipientsStr
	 */
	public String getCcRecipientsStr() {
		return ccRecipientsStr;
	}

	/**
	 * @param ccRecipientsStr the ccRecipientsStr to set
	 */
	public void setCcRecipientsStr(String ccRecipientsStr) {
		this.ccRecipientsStr = ccRecipientsStr;
	}

	/**
	 * @return the ngRecipientsStr
	 */
	public String getNgRecipientsStr() {
		return ngRecipientsStr;
	}

	/**
	 * @param ngRecipientsStr the ngRecipientsStr to set
	 */
	public void setNgRecipientsStr(String ngRecipientsStr) {
		this.ngRecipientsStr = ngRecipientsStr;
	}

}
