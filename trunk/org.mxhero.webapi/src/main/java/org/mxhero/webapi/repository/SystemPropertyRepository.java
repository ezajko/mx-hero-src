package org.mxhero.webapi.repository;

import java.util.Collection;
import java.util.List;

import org.mxhero.webapi.vo.SystemPropertyVO;

public interface SystemPropertyRepository {

	public List<SystemPropertyVO> findAll();
	
	public SystemPropertyVO findById(String propertyKey);
	
	public void save(SystemPropertyVO property);
	
	public void save(Collection<SystemPropertyVO> properties);
}
