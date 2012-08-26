package org.mxhero.engine.plugin.storageapi;


/**
 * The Class StorageResult.
 */
public class StorageResult {

	/** The success. */
	private boolean success;
	
	/** The message. */
	private String message;
	
	/** The file stored. */
	private FileStored fileStored;

	/** The already proccessed. */
	private boolean alreadyProccessed;
	
	/**
	 * Instantiates a new storage result.
	 *
	 * @param sucess the sucess
	 */
	public StorageResult(boolean sucess) {
		this.success = sucess;
	}

	/**
	 * Sets the success.
	 *
	 * @param success the new success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	/**
	 * Checks if is success.
	 *
	 * @return true, if is success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the file stored.
	 *
	 * @return the file stored
	 */
	public FileStored getFileStored() {
		return fileStored;
	}

	/**
	 * Sets the file stored.
	 *
	 * @param fileStored the new file stored
	 */
	public void setFileStored(FileStored fileStored) {
		this.fileStored = fileStored;
	}

	/**
	 * Sets the already proccessed.
	 *
	 * @param b the new already proccessed
	 */
	public void setAlreadyProccessed(boolean b) {
		this.alreadyProccessed = b;
	}
	
	/**
	 * Checks if is already proccessed.
	 *
	 * @return true, if is already proccessed
	 */
	public boolean isAlreadyProccessed() {
		return alreadyProccessed;
	}

}
