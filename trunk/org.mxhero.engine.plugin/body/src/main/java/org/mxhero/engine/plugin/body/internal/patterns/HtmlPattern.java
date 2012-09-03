package org.mxhero.engine.plugin.body.internal.patterns;

public class HtmlPattern implements Comparable<HtmlPattern>{

	private Long priority;
	
	private String header;
	
	private String select;
	
	private String pattern;
	
	private Boolean headerOnly;
	
	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int compareTo(HtmlPattern o) {
		return this.getPriority().compareTo(o.getPriority());
	}

	public Boolean getHeaderOnly() {
		return headerOnly;
	}

	public void setHeaderOnly(Boolean headerOnly) {
		this.headerOnly = headerOnly;
	}

}
