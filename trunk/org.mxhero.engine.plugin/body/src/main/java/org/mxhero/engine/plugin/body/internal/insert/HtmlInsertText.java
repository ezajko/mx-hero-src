package org.mxhero.engine.plugin.body.internal.insert;

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

@Repository("htmlInsertText")
public class HtmlInsertText implements InsertText{

	@Autowired
	private PatternsConfig config;

	public String insert(String text, String insert, String header) {
		Document doc = Jsoup.parse(text);
		List<HtmlPattern> patternsByHeader = config.getHtmlPatternByHeader(header);
		boolean found=false;
		if(patternsByHeader!=null && patternsByHeader.size()>0){
			for(HtmlPattern htmlPattern: patternsByHeader){
				if(insert(doc, insert, htmlPattern)){
					found=true;
					break;
				}
			}
		}else{
			for(HtmlPattern htmlPattern: config.getHtmlPatterns()){
				if(insert(doc, insert, htmlPattern)){
					found=true;
					break;
				}
			}
		}
		if(!found){
			doc.body().append(insert);
		}
		return doc.toString();
	}
	
	private boolean insert(Document doc, String insert, HtmlPattern pattern){
		Elements elements = doc.select(pattern.getSelect());
		
		if(elements!=null && !elements.isEmpty()){
			Iterator<Element> iterator = elements.iterator();
			while(iterator.hasNext()){
				Element element = iterator.next();
				if(element.toString().matches(pattern.getPattern())){
					element.before(insert);
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
