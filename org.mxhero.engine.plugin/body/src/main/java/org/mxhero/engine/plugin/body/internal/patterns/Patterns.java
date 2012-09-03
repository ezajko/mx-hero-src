package org.mxhero.engine.plugin.body.internal.patterns;

import java.util.List;

public class Patterns {

	private List<HtmlPattern> htmlPatterns;
	
	private List<PlainPattern> plainPatterns;

	public List<HtmlPattern> getHtmlPatterns() {
		return htmlPatterns;
	}

	public void setHtmlPatterns(List<HtmlPattern> htmlPatterns) {
		this.htmlPatterns = htmlPatterns;
	}

	public List<PlainPattern> getPlainPatterns() {
		return plainPatterns;
	}

	public void setPlainPatterns(List<PlainPattern> plainPatterns) {
		this.plainPatterns = plainPatterns;
	}
	
}
