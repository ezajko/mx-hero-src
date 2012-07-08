package org.mxhero.engine.plugin.disclaimercontract.internal.loader;

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

public class DisclaimerContractTemplate {
	
	private static Logger log = LoggerFactory.getLogger(DisclaimerContractTemplate.class);
	
	private String defaultLocale = "en_US";
	private String[] availableLocales = {};
	private String notificationFileStartName = "/opt/mxhero/disclaimer/old/template_";
	private String notificationFileEndName = ".htm";
	private Map<String, String> errorMap;

	public void init(){
		log.debug("init");
		refreshNotificationMap();
	}
	
	public void refreshNotificationMap(){
		errorMap = new HashMap<String, String>();
		errorMap.put(defaultLocale,  getDefault("old/template_en_US.htm"));
		for(String locale : availableLocales){
			String file = notificationFileStartName+locale+notificationFileEndName;
			try {
				errorMap.put(locale,  readFileAsString(file));
			} catch (IOException e) {
				log.error("could not read file "+file);
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

	public String getNotificationFileStartName() {
		return notificationFileStartName;
	}

	public void setNotificationFileStartName(String notificationFileStartName) {
		this.notificationFileStartName = notificationFileStartName;
	}

	public String getNotificationFileEndName() {
		return notificationFileEndName;
	}

	public void setNotificationFileEndName(String notificationFileEndName) {
		this.notificationFileEndName = notificationFileEndName;
	}

	public String getTemplate(String locale){
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
	
	private static String getDefault(String resource){
		InputStream in = DisclaimerContractTemplate.class.getClassLoader().getResourceAsStream(resource);
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
