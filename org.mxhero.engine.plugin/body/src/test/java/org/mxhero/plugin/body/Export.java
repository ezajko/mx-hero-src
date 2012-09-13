/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.plugin.body;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.mxhero.engine.plugin.body.internal.patterns.HtmlPattern;
import org.mxhero.engine.plugin.body.internal.patterns.Patterns;
import org.mxhero.engine.plugin.body.internal.patterns.PlainPattern;

public class Export {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure( SerializationConfig.Feature.INDENT_OUTPUT, true);
		Patterns patterns = new Patterns();
		patterns.setHtmlPatterns(new ArrayList<HtmlPattern>());
		patterns.setPlainPatterns(new ArrayList<PlainPattern>());
		
		HtmlPattern gmailHtmlPattern = new HtmlPattern();
		gmailHtmlPattern.setPattern("*");
		gmailHtmlPattern.setPriority(0l);
		gmailHtmlPattern.setHeader(null);
		gmailHtmlPattern.setSelect("div[class=gmail_quote]");
		patterns.getHtmlPatterns().add(gmailHtmlPattern);
		
		PlainPattern gmailPlainPattern = new PlainPattern();
		gmailPlainPattern.setHeader(null);
		gmailPlainPattern.setPattern("^\\s*\\d{4}/\\d{1,2}/\\d{1,2}.*<.*@.*>\\s*$");
		gmailPlainPattern.setPriority(0l);
		patterns.getPlainPatterns().add(gmailPlainPattern);
		
		mapper.writeValue(new File("patterns.json"), patterns);

	}

}
