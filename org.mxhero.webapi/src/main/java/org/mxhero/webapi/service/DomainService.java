package org.mxhero.webapi.service;

import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.PageVO;

public interface DomainService {

	public PageVO<DomainVO> readAll(Integer limit, Integer offset);
	
	public DomainVO create(DomainVO domainVO);
	
	public DomainVO read(String domain);

	public void update(DomainVO domainVO);
	
	public void delete(String domain);
	
}
