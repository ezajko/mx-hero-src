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

package org.mxhero.engine.plugin.body.internal.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.mxhero.engine.plugin.body.internal.patterns.HtmlPattern;
import org.mxhero.engine.plugin.body.internal.patterns.Patterns;
import org.mxhero.engine.plugin.body.internal.patterns.PatternsConfig;
import org.mxhero.engine.plugin.body.internal.patterns.PlainPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternsLoader implements Runnable, PatternsConfig{

	private static Logger log = LoggerFactory.getLogger(PatternsLoader.class);
	private static final long CHECK_TIME = 1000;
	private Thread thread;
	private boolean keepWorking = false;
	private Long updateTime = 60000l;
	private String filePath;
	private ObjectMapper mapper = null;
	private Patterns patternsRaw;
	private Long fileLastModified;
	private boolean isClassPath = false;
	private List<HtmlPattern> htmlPatterns;
	private List<PlainPattern> plainPatterns;

	public PatternsLoader() {
		mapper = new ObjectMapper();
		mapper.configure( SerializationConfig.Feature.INDENT_OUTPUT, true);
	}

	public void start(){
		thread=new Thread(this);
		keepWorking=true;
		loadDefault();
		configure();
		thread.start();
	}

	public void stop(){
		keepWorking=false;	
		if(thread!=null){
			try {
				thread.join();
			} catch (InterruptedException e) {}
		}
	}

	public void run() {
		long lastUpdate = 0;
		while(keepWorking){
			try {
				Thread.sleep(CHECK_TIME);
			} catch (InterruptedException e) {}
			if(lastUpdate+getUpdateTime()-System.currentTimeMillis()<0){
				load();
				lastUpdate=System.currentTimeMillis();
			}
		}
	}

	private void load(){
		if(filePath!=null && !filePath.trim().isEmpty()){
			try{
				File file = new File(filePath);
				if(fileLastModified == null || file.lastModified()>fileLastModified){
					fileLastModified=file.lastModified();
					patternsRaw = mapper.readValue(file, Patterns.class);
					isClassPath=false;
				}
			}catch(Exception e){
				log.warn("error reading file "+filePath,e);
				loadDefault();
			}
		}else{
			loadDefault();
		}
		configure();
	}
	
	private void loadDefault(){
		if(isClassPath==false){
			try{
				patternsRaw = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("patterns.json"), Patterns.class);
				isClassPath=true;
			}catch(Exception e){
				log.warn("error reading file from class path",e);
			}
		}
	}
	
	private void configure(){
		if(patternsRaw!=null){
			if(patternsRaw.getPlainPatterns()!=null){
				Collections.sort(patternsRaw.getPlainPatterns());
				List<PlainPattern> auxPlain = new ArrayList<PlainPattern>();
				for(PlainPattern plain : patternsRaw.getPlainPatterns()){
					if(!plain.getHeaderOnly()){
						auxPlain.add(plain);
					}
				}
				plainPatterns=auxPlain;
			}
			if(patternsRaw.getHtmlPatterns()!=null){
				Collections.sort(patternsRaw.getHtmlPatterns());
				List<HtmlPattern> auxHtml = new ArrayList<HtmlPattern>();
				for(HtmlPattern html : patternsRaw.getHtmlPatterns()){
					if(!html.getHeaderOnly()){
						auxHtml.add(html);
					}
				}
				htmlPatterns=auxHtml;
			}
		}
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public List<HtmlPattern> getHtmlPatternByHeader(String header) {
		List<HtmlPattern> aux = null;
		if(patternsRaw.getHtmlPatterns()!=null && header!=null){
			aux = new ArrayList<HtmlPattern>();
			for(HtmlPattern patternInList : patternsRaw.getHtmlPatterns()){
				if(patternInList.getHeader()!=null && header.matches(patternInList.getHeader())){
					aux.add(patternInList);
				}
			}
		}
		return aux;
	}

	public List<HtmlPattern> getHtmlPatterns() {
		return htmlPatterns;
	}

	public List<PlainPattern> getPlainPatternByHeader(String header) {
		List<PlainPattern> aux = null;
		if(patternsRaw.getPlainPatterns()!=null && header!=null){
			aux = new ArrayList<PlainPattern>();
			for(PlainPattern patternInList : patternsRaw.getPlainPatterns()){
				if(patternInList.getHeader()!=null && header.matches(patternInList.getHeader())){
					aux.add(patternInList);
				}
			}
		}
		return aux;
	}

	public List<PlainPattern> getPlainPatterns() {
		return plainPatterns;
	}


}
