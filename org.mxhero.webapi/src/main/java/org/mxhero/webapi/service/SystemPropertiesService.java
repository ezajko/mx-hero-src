package org.mxhero.webapi.service;

import java.util.List;

import org.mxhero.webapi.vo.SystemPropertyVO;

public interface SystemPropertiesService {

	public List<SystemPropertyVO> readAll();

	public SystemPropertyVO create(String key, String value);

	public SystemPropertyVO read(String key);
	
	public void update(String key, String value);
	
	public void delete(String key);
	
}
