package org.mxhero.feature.disclaimercontract.provider.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DisclaimerContractConfig {
	
	private static Logger log = LoggerFactory.getLogger(DisclaimerContractConfig.class);
	
	private String defaultLocale = "en_US";
	private String[] availableLocales = {};
	private String errorFileStartName = "/opt/mxhero/disclaimer/error/template_";
	private String errorFileEndName = ".htm";
	private Map<String, String> errorMap;
	private String externalServiceBaseUrl="http://localhost:8080/disclaimer/approve";
	private String externalRejectServiceBaseUrl="http://localhost:8080/disclaimer/reject";
	private String password="disclaimer";
	private PBEStringEncryptor encryptor;
	public void init(){
		log.debug("init");
		StandardPBEStringEncryptor newencryptor= new StandardPBEStringEncryptor();
		newencryptor.setPassword(password);
		newencryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor = newencryptor;
		refreshErrorMap();
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

	public String getExternalServiceBaseUrl() {
		return externalServiceBaseUrl;
	}

	public void setExternalServiceBaseUrl(String externalServiceBaseUrl) {
		this.externalServiceBaseUrl = externalServiceBaseUrl;
	}
	
	public String getExternalRejectServiceBaseUrl() {
		return externalRejectServiceBaseUrl;
	}

	public void setExternalRejectServiceBaseUrl(String externalRejectServiceBaseUrl) {
		this.externalRejectServiceBaseUrl = externalRejectServiceBaseUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		StandardPBEStringEncryptor newencryptor= new StandardPBEStringEncryptor();
		newencryptor.setPassword(password);
		newencryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor = newencryptor;
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
	
	private static String getDefault(String resource){
		InputStream in = DisclaimerContractConfig.class.getClassLoader().getResourceAsStream(resource);
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

	public PBEStringEncryptor getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(PBEStringEncryptor encryptor) {
		this.encryptor = encryptor;
	}
	
}
