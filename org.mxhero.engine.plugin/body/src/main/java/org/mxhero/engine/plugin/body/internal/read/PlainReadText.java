package org.mxhero.engine.plugin.body.internal.read;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mxhero.engine.plugin.body.internal.patterns.PatternsConfig;
import org.mxhero.engine.plugin.body.internal.patterns.PlainPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("plainReadText")
public class PlainReadText implements ReadText{

	@Autowired
	private PatternsConfig config;

	public String read(String text, String header) {
		StringBuilder sb = new StringBuilder(text);
		List<PlainPattern> patternsByHeader = config.getPlainPatternByHeader(header);
		if(patternsByHeader!=null && patternsByHeader.size()>0){
			for(PlainPattern plainPattern: patternsByHeader){
				if(readBody(sb, plainPattern)){
					break;
				}
			}
		}else{
			for(PlainPattern plainPattern: config.getPlainPatterns()){
				if(readBody(sb, plainPattern)){
					break;
				}
			}
		}
		return sb.toString();
	}
		
	private boolean readBody(StringBuilder sb, PlainPattern pattern){
		Pattern p = Pattern.compile(pattern.getPattern());
		Matcher m = p.matcher(sb);
		if(m.find()){
			sb.replace((m.start()>0)?m.start()-1:0, sb.length(), "");
			return true;
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
