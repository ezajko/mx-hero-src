package org.mxhero.console.backend.service.jpa;

import org.mxhero.console.backend.dao.LocalePropertyDao;
import org.mxhero.console.backend.entity.LocaleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocalePropertyHelper {

	@Autowired
	private LocalePropertyDao dao;

	public LocalePropertyDao getDao() {
		return dao;
	}

	public void setDao(LocalePropertyDao dao) {
		this.dao = dao;
	}

	public String getValue(String component, String key, String userLocale, String defaultLocale ){
		LocaleProperty localeProperty = null;
		
		localeProperty = dao.findByComponentAndLocaleAndPropertyKey(component, userLocale, key);
		if(localeProperty!=null){
			return localeProperty.getPropertyValue();
		}
		
		localeProperty = dao.findByComponentAndLocaleAndPropertyKey(component, defaultLocale, key);
		if(localeProperty!=null){
			return localeProperty.getPropertyValue();
		}
		
		return key;
	}
}
