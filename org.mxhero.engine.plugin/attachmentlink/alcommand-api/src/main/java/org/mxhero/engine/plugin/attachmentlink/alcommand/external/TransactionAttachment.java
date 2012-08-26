package org.mxhero.engine.plugin.attachmentlink.alcommand.external;

/**
 * The Class TransactionAttachment.
 */
public class TransactionAttachment {
	
	/** The id message attach. */
	private Long idMessageAttach;
	
	/** The email. */
	private String email;
	
	/** The file path. */
	private String filePath;
	
	/** The public url. */
	private String publicUrl;

	
	/**
	 * Sets the public url.
	 *
	 * @param publicUrl the new public url
	 */
	public void setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
	}
	
	/**
	 * Gets the public url.
	 *
	 * @return the public url
	 */
	public String getPublicUrl() {
		return publicUrl;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the id message attach.
	 *
	 * @return the id message attach
	 */
	public Long getIdMessageAttach() {
		return idMessageAttach;
	}

	/**
	 * Sets the id message attach.
	 *
	 * @param idMessageAttach the new id message attach
	 */
	public void setIdMessageAttach(Long idMessageAttach) {
		this.idMessageAttach = idMessageAttach;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[idMessageAttach=");
		builder.append(getIdMessageAttach());
		builder.append("-filePath=");
		builder.append(getFilePath());
		builder.append("-email=");
		builder.append(getEmail());
		builder.append("]");
		return builder.toString();
	}

}
