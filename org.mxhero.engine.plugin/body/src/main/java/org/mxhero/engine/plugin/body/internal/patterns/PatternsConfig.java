package org.mxhero.engine.plugin.body.internal.patterns;

import java.util.List;

public interface PatternsConfig {

	public static final String MAILER_HEADER = "X-Mailer";
	
	public List<HtmlPattern> getHtmlPatternByHeader(String header);

	public List<HtmlPattern> getHtmlPatterns();

	public List<PlainPattern> getPlainPatternByHeader(String header);

	public List<PlainPattern> getPlainPatterns();

}
