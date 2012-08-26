package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.connector.domain;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.mxhero.engine.plugin.storageapi.StorageResult;

/**
 * The Class FileUploadResponse.
 */
public class FileUploadResponse extends AbstractResponse{
	
	/** The total_count. */
	private Integer total_count;
	
	/** The entries. */
	private List<Entry> entries;

	/** The response shared link. */
	private CodeResponse responseSharedLink;
	
	/**
	 * Gets the total_count.
	 *
	 * @return the total_count
	 */
	public Integer getTotal_count() {
		return total_count;
	}
	
	/**
	 * Sets the total_count.
	 *
	 * @param total_count the new total_count
	 */
	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
	
	/**
	 * Gets the entries.
	 *
	 * @return the entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}
	
	/**
	 * Sets the entries.
	 *
	 * @param entries the new entries
	 */
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public StorageResult getResult() {
		return new StorageResult(CodeResponse.OK.equals(getResponse()));
	}
	
	/**
	 * Could create shared link.
	 *
	 * @return true, if successful
	 */
	public boolean couldCreateSharedLink(){
		return CodeResponse.OK.equals(getResponseSharedLink());
	}
	
	/**
	 * Sets the response shared link.
	 *
	 * @param response the new response shared link
	 */
	public void setResponseSharedLink(CodeResponse response){
		this.responseSharedLink = response;
	}
	
	/**
	 * Gets the response shared link.
	 *
	 * @return the response shared link
	 */
	public CodeResponse getResponseSharedLink() {
		return responseSharedLink;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}


}
