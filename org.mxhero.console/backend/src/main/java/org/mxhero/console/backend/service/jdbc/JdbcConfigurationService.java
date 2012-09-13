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

package org.mxhero.console.backend.service.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.util.encoders.Base64;
import org.mxhero.console.backend.infrastructure.MailSender;
import org.mxhero.console.backend.repository.SystemPropertyRepository;
import org.mxhero.console.backend.repository.UserRepository;
import org.mxhero.console.backend.service.ConfigurationService;
import org.mxhero.console.backend.vo.ApplicationUserVO;
import org.mxhero.console.backend.vo.ConfigurationVO;
import org.mxhero.console.backend.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Repository("jdbcConfigurationService")
public class JdbcConfigurationService implements ConfigurationService {

	private static final String MAIL_TEST_SUBJECT = "mail.test.subject";
	private static final String MAIL_TEST_BODY = "mail.test.body";

	private SystemPropertyRepository systemPropertiesRepository;

	private AbstractMessageSource ms;

	private UserRepository userRepository;

	@Autowired
	public JdbcConfigurationService(
			SystemPropertyRepository systemPropertiesRepository,
			AbstractMessageSource ms, UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.ms = ms;
		this.systemPropertiesRepository = systemPropertiesRepository;
		Security.addProvider(new BouncyCastleProvider());
	}

	@Override
	public void edit(ConfigurationVO configurationVO) {
		SystemPropertyVO property = null;
		Collection<SystemPropertyVO> properties = new ArrayList<SystemPropertyVO>();

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_AUTH);
		property.setPropertyValue(configurationVO.getAuth().toString());
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_HOST);
		property.setPropertyValue(configurationVO.getHost());
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_PASSWORD);
		if (configurationVO.getAuth()) {
			property.setPropertyValue(configurationVO.getPassword());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_PORT);
		property.setPropertyValue(configurationVO.getPort().toString());
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE);
		property.setPropertyValue(configurationVO.getSsl().toString());
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_SMTP_USER);
		if (configurationVO.getAuth()) {
			property.setPropertyValue(configurationVO.getUser());
		} else {
			property.setPropertyValue(null);
		}
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.MAIL_ADMIN);
		property.setPropertyValue(configurationVO.getAdminMail());
		properties.add(property);

		property = new SystemPropertyVO();
		property.setPropertyKey(SystemPropertyVO.LICENSE);
		property.setPropertyValue(configurationVO.getLicense());
		properties.add(property);

		systemPropertiesRepository.save(properties);
	}

	@Override
	public ConfigurationVO find() {
		ConfigurationVO configuration = new ConfigurationVO();

		configuration.setAuth(Boolean.parseBoolean(systemPropertiesRepository
				.findById(SystemPropertyVO.MAIL_SMTP_AUTH).getPropertyValue()));
		configuration.setDefaultLanguage(systemPropertiesRepository.findById(
				SystemPropertyVO.DEFAULT_USER_LANGUAGE).getPropertyValue());
		configuration.setHost(systemPropertiesRepository.findById(
				SystemPropertyVO.MAIL_SMTP_HOST).getPropertyValue());
		configuration.setPassword(systemPropertiesRepository.findById(
				SystemPropertyVO.MAIL_SMTP_PASSWORD).getPropertyValue());
		configuration.setPort(Integer.parseInt(systemPropertiesRepository
				.findById(SystemPropertyVO.MAIL_SMTP_PORT).getPropertyValue()));
		configuration.setSsl(Boolean.parseBoolean(systemPropertiesRepository
				.findById(SystemPropertyVO.MAIL_SMTP_SSL_ENABLE)
				.getPropertyValue()));
		configuration.setUser(systemPropertiesRepository.findById(
				SystemPropertyVO.MAIL_SMTP_USER).getPropertyValue());
		configuration.setAdminMail(systemPropertiesRepository.findById(
				SystemPropertyVO.MAIL_ADMIN).getPropertyValue());
		configuration.setDocumentationUrl(systemPropertiesRepository.findById(
				SystemPropertyVO.DOCUMENTATION_URL).getPropertyValue());
		SystemPropertyVO property = systemPropertiesRepository
				.findById(SystemPropertyVO.EXTERNAL_LOGO_PATH);
		if (property != null) {
			configuration.setLogoPath(property.getPropertyValue());
		}
		property = systemPropertiesRepository
				.findById(SystemPropertyVO.NEWS_FEED_ENABLED);
		if (property != null) {
			configuration.setNewsFeedEnabled(property.getPropertyValue()
					.toLowerCase());
		}
		property = systemPropertiesRepository
				.findById(SystemPropertyVO.LICENSE);
		if (property != null) {
			configuration.setLicense(property.getPropertyValue());
		}
		return configuration;
	}

	@Override
	public void testMail(ConfigurationVO configurationVO) {
		ApplicationUserVO user = userRepository
				.finbByUserName(SecurityContextHolder.getContext()
						.getAuthentication().getName());
		MailSender.send(ms.getMessage(MAIL_TEST_SUBJECT, null, new Locale(user
				.getLocale().split("_")[0], user.getLocale().split("_")[1])),
				ms.getMessage(MAIL_TEST_BODY, null, new Locale(user.getLocale()
						.split("_")[0], user.getLocale().split("_")[1])),
				configurationVO.getAdminMail(), configurationVO);

	}

	private PublicKey readPublicKey() {
	    InputStream is = this.getClass().getClassLoader().getResourceAsStream("pub.key");
	    PEMReader pemReader = new PEMReader(new InputStreamReader(is));
	    try {
			Object obj = pemReader.readObject();
			if(obj instanceof RSAPublicKey){
				return (RSAPublicKey)obj;
			}
			 pemReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}

	public boolean testLicense(String license){
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(license)));
			Map<String, String> treeMap = new TreeMap<String, String>();
			NodeList items = doc.getDocumentElement().getElementsByTagName("item");
			if(items!=null){
				for(int i=0;i<items.getLength();i++){
					items.item(i).getAttributes().getNamedItem("name").getNodeValue();
					treeMap.put(items.item(i).getAttributes().getNamedItem("name").getNodeValue(), items.item(i).getAttributes().getNamedItem("value").getNodeValue());
				}
			}
			
			String verifySignature = doc.getDocumentElement().getElementsByTagName("signature").item(0).getAttributes().getNamedItem("value").getNodeValue();
			
			String keyToSign="";
			for(String key : treeMap.keySet()){
				keyToSign=keyToSign+key+treeMap.get(key);
			}
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(readPublicKey());
			signature.update(keyToSign.getBytes());
			return signature.verify(Base64.decode(verifySignature));

		} catch (Exception e) {
			return false;
		} 
	}
}
