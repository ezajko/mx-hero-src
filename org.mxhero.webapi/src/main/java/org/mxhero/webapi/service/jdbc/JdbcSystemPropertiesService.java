package org.mxhero.webapi.service.jdbc;

import java.util.List;

import org.mxhero.webapi.repository.SystemPropertyRepository;
import org.mxhero.webapi.service.SystemPropertiesService;
import org.mxhero.webapi.service.exception.ConflictResourceException;
import org.mxhero.webapi.service.exception.UnknownResourceException;
import org.mxhero.webapi.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jdbcSystemPropertiesService")
public class JdbcSystemPropertiesService implements SystemPropertiesService{

	private SystemPropertyRepository repository;
	
	@Autowired(required=true)
	public JdbcSystemPropertiesService(SystemPropertyRepository repository){
		this.repository = repository;
	}

	@Override
	public List<SystemPropertyVO> readAll() {
		return repository.findAll();
	}

	@Override
	public SystemPropertyVO create(String key, String value) {
		if(repository.findById(key)!=null){
			throw new ConflictResourceException("system.property.already.exists");
		}
		SystemPropertyVO newProperty = new SystemPropertyVO();
		newProperty.setPropertyKey(key);
		newProperty.setPropertyValue(value);
		repository.save(newProperty);
		return newProperty;
	}

	@Override
	public SystemPropertyVO read(String key) {
		SystemPropertyVO property =  repository.findById(key);
		if(property == null){
			throw new UnknownResourceException("system.property.not.found");
		}
		return property;
	}

	@Override
	public void update(String key, String value) {
		SystemPropertyVO property =  repository.findById(key);
		if(property == null){
			throw new UnknownResourceException("system.property.not.found");
		}
		property.setPropertyValue(value);
		repository.save(property);
	}

	@Override
	public void delete(String key) {
		SystemPropertyVO property =  repository.findById(key);
		if(property == null){
			throw new UnknownResourceException("system.property.not.found");
		}
		repository.delete(key);
	}
		
}
