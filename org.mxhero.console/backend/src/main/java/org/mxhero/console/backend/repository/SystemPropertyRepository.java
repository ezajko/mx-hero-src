package org.mxhero.console.backend.repository;

import java.util.Collection;
import java.util.List;

import org.mxhero.console.backend.vo.SystemPropertyVO;


public interface SystemPropertyRepository {

	public List<SystemPropertyVO> findAll();
	
	public SystemPropertyVO findById(String propertyKey);
	
	public void save(SystemPropertyVO property);
	
	public void save(Collection<SystemPropertyVO> properties);
}
