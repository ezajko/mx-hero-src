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

package org.mxhero.engine.plugin.body.internal.read;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mxhero.engine.plugin.body.internal.patterns.HtmlPattern;
import org.mxhero.engine.plugin.body.internal.patterns.PatternsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("htmlReadText")
public class HtmlReadText implements ReadText{

	@Autowired
	private PatternsConfig config;

	public String read(String text, String header) {
		Document doc = Jsoup.parse(text);
		List<HtmlPattern> patternsByHeader = config.getHtmlPatternByHeader(header);
		if(patternsByHeader!=null && patternsByHeader.size()>0){
			for(HtmlPattern htmlPattern: patternsByHeader){
				if(readBody(doc, htmlPattern)){
					break;
				}
			}
		}else{
			for(HtmlPattern htmlPattern: config.getHtmlPatterns()){
				if(readBody(doc, htmlPattern)){
					break;
				}
			}
		}
		return doc.toString();
	}
		
	private boolean readBody(Document doc, HtmlPattern pattern){
		Elements elements = doc.select(pattern.getSelect());
		
		if(elements!=null && !elements.isEmpty()){
			Iterator<Element> iterator = elements.iterator();
			while(iterator.hasNext()){
				Element element = iterator.next();
				if(element.toString().matches(pattern.getPattern())){
					List<Element> siblingElements = new ArrayList<Element>();
					siblingElements.add(element);
					while(element.nextElementSibling()!=null){		
						element=element.nextElementSibling();
						siblingElements.add(element);
					}
					for(Element remove : siblingElements){
						remove.remove();
					}
					return true;
				}
			}
		}

		return false;
	}

	public PatternsConfig getConfig() {
		return config;
	}

	public void setConfig(PatternsConfig config) {
		this.config = config;
	}

}
