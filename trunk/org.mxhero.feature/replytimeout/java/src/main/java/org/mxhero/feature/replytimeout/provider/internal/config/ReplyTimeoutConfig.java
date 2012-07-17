package org.mxhero.feature.replytimeout.provider.internal.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReplyTimeoutConfig {
	
	private static Logger log = LoggerFactory.getLogger(ReplyTimeoutConfig.class);
	
	private String defaultLocale = "en_US";
	private String[] availableLocales = {};
	private String errorFileStartName = "/opt/mxhero/replytimeout/error/template_";
	private String errorFileEndName = ".htm";
	private String noreplyFileEndName = ".htm";
	private String noreplyFileStartName = "/opt/mxhero/replytimeout/noreply/template_";
	private Integer checkTimeInMinutes = 5;
	private Map<String, String> errorMap;
	private Map<String, String> noreplyMap;
	private String outputService = "org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService";
	
	public void init(){
		refreshErrorMap();
		refreshNoReplyMap();
	}
	
	public void refreshErrorMap(){
		errorMap = new HashMap<String, String>();
		errorMap.put(defaultLocale,  getDefault("error/template_en_US.htm"));
		for(String locale : availableLocales){
			String file = errorFileStartName+locale+errorFileEndName;
			try {
				errorMap.put(locale,  readFileAsString(file));
			} catch (IOException e) {
				log.error("could not read file "+file);
			}
		}
	}
	
	public void refreshNoReplyMap(){
		noreplyMap = new HashMap<String, String>();
		noreplyMap.put(defaultLocale,  getDefault("noreply/template_en_US.htm"));
		for(String locale : availableLocales){
			String file = noreplyFileStartName+locale+noreplyFileEndName;
			try {
				noreplyMap.put(locale,  readFileAsString(file));
			} catch (IOException e) {
				log.error("could not read file "+file);
			}
		}
	}

	public String getNoreplyFileEndName() {
		return noreplyFileEndName;
	}

	public void setNoreplyFileEndName(String noreplyFileEndName) {
		this.noreplyFileEndName = noreplyFileEndName;
		refreshNoReplyMap();
	}

	public String getNoreplyFileStartName() {
		return noreplyFileStartName;
	}

	public void setNoreplyFileStartName(String noreplyFileStartName) {
		this.noreplyFileStartName = noreplyFileStartName;
		refreshNoReplyMap();
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}
	
	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	
	public String getAvailableLocales() {
		return Arrays.deepToString(availableLocales).replace("[", "").replace("]", "");
	}
	
	public void setAvailableLocales(String availableLocales) {
		this.availableLocales = availableLocales.split(";");
	}
	
	public String getErrorFileStartName() {
		return errorFileStartName;
	}
	
	public void setErrorFileStartName(String errorFileStartName) {
		this.errorFileStartName = errorFileStartName;
		refreshErrorMap();
	}
	
	public String getErrorFileEndName() {
		return errorFileEndName;
	}
	
	public void setErrorFileEndName(String errorFileEndName) {
		this.errorFileEndName = errorFileEndName;
		refreshErrorMap();
	}
	
	public Integer getCheckTimeInMinutes() {
		return checkTimeInMinutes;
	}

	public void setCheckTimeInMinutes(Integer checkTimeInMinutes) {
		this.checkTimeInMinutes = checkTimeInMinutes;
	}
	
	public String getOutputService() {
		return outputService;
	}

	public void setOutputService(String outputService) {
		this.outputService = outputService;
	}

	public String getErrorTemplate(String locale){
		for(String key : errorMap.keySet()){
			if(locale.startsWith(key) || key.startsWith(locale)){
				return errorMap.get(key);
			}
		}
		return errorMap.get(defaultLocale);
	}
	
	public String getNoReplyTemplate(String locale){
		for(String key : noreplyMap.keySet()){
			if(locale.startsWith(key) || key.startsWith(locale)){
				return noreplyMap.get(key);
			}
		}
		return noreplyMap.get(defaultLocale);
	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}
	
	private static String getDefault(String resource){
		InputStream in = ReplyTimeoutConfig.class.getClassLoader().getResourceAsStream(resource);
		StringBuffer out = new StringBuffer();
		try {
	    byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
			    out.append(new String(b, 0, n));
			}
		} catch (IOException e) {
			log.error("could not read default file",e);
		}
	    return out.toString();
	}
}
