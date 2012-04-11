package org.mxhero.console.backend.service.jdbc;

import org.mxhero.console.backend.repository.QuarantineRepository;
import org.mxhero.console.backend.service.QuarantineService;
import org.mxhero.console.backend.vo.QuarantineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcQuarantineService")
public class JdbcQuarantineService implements QuarantineService{
	
	private QuarantineRepository repository;

	@Autowired
	public JdbcQuarantineService(QuarantineRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void save(QuarantineVO quarantine) {
		if(quarantine!=null){
			if(quarantine.getEmail()==null || quarantine.getEmail().trim().isEmpty()){
				repository.delete(quarantine.getDomain());
			}else{
				repository.save(quarantine);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public QuarantineVO read(String domain) {
		return repository.read(domain);
	}
	
	

}
