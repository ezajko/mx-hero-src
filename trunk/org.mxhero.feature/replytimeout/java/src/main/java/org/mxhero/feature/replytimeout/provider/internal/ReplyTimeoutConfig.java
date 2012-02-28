package org.mxhero.feature.replytimeout.provider.internal;

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
	private String[] availableLocales = {"en_US"};
	private String errorFileStartName = "/opt/mxhero/replytimout/error/template_";
	private String errorFileEndName = ".htm";
	private Map<String, String> errorMap;
	
	public void init(){
		refreshErrorMap();
	}
	
	public void refreshErrorMap(){
		errorMap = new HashMap<String, String>();
		errorMap.put(defaultLocale,  getDefault());
		for(String locale : availableLocales){
			try {
				errorMap.put(locale,  readFileAsString(errorFileStartName+locale+errorFileEndName));
			} catch (IOException e) {
				log.error("could not read file",e);
			}
		}
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
	}
	
	public String getErrorFileEndName() {
		return errorFileEndName;
	}
	
	public void setErrorFileEndName(String errorFileEndName) {
		this.errorFileEndName = errorFileEndName;
	}
	
	
	public String getErrorTemplate(String locale){
		for(String key : errorMap.keySet()){
			if(locale.startsWith(key) || key.startsWith(locale)){
				return errorMap.get(key);
			}
		}
		return errorMap.get(defaultLocale);
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
	
	private static String getDefault(){
		InputStream in = ReplyTimeoutConfig.class.getClassLoader().getResourceAsStream("error/template_en_US.htm");
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
