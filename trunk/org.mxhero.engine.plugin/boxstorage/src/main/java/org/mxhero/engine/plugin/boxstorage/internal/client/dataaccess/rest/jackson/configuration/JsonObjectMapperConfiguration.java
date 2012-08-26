package org.mxhero.engine.plugin.boxstorage.internal.client.dataaccess.rest.jackson.configuration;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
/**
 * The Class JsonObjectMapperConfiguration.
 */
public class JsonObjectMapperConfiguration implements BeanPostProcessor {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof MappingJacksonHttpMessageConverter) {
			MappingJacksonHttpMessageConverter jsonConverter = (MappingJacksonHttpMessageConverter) bean;
			ObjectMapper newObjectMapper = new ObjectMapper();
			newObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			jsonConverter.setObjectMapper(newObjectMapper);
		}
		return bean;
	}

}