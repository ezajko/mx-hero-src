package org.mxhero.engine.domain.mail.command;

/**
 * Represents the result of a command and holds some properties to help describe wath happend.
 * @author mmarmol
 */
public class Result {

	private boolean result = false;
		
	private long longField;
	
	private Double doubleField;
	
	private String text;
	
	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @return the result
	 */
	public boolean isTrue() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the longField
	 */
	public long getLongField() {
		return longField;
	}

	/**
	 * @param longField the longField to set
	 */
	public void setLongField(long longField) {
		this.longField = longField;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the doubleField
	 */
	public Double getDoubleField() {
		return doubleField;
	}

	/**
	 * @param doubleField the doubleField to set
	 */
	public void setDoubleField(Double doubleField) {
		this.doubleField = doubleField;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [result=").append(result).append(", text=")
				.append(text).append(", longField=").append(longField).append(
						", doubleField=").append(doubleField).append("]");
		return builder.toString();
	}
	
}
