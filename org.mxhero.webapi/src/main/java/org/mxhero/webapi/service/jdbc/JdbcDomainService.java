package org.mxhero.webapi.service.jdbc;

import org.mxhero.webapi.service.DomainService;
import org.mxhero.webapi.vo.DomainVO;
import org.mxhero.webapi.vo.PageVO;
import org.springframework.stereotype.Service;

@Service("jdbcDomainService")
public class JdbcDomainService implements DomainService{

	@Override
	public PageVO<DomainVO> readAll(Integer limit, Integer offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainVO create(DomainVO domainVO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainVO read(String domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(DomainVO domainVO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String domain) {
		// TODO Auto-generated method stub
		
	}

}
