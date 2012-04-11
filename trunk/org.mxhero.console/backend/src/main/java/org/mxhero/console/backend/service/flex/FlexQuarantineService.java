package org.mxhero.console.backend.service.flex;

import org.mxhero.console.backend.service.QuarantineService;
import org.mxhero.console.backend.vo.QuarantineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("quarantineService")
@RemotingDestination(channels = { "flex-amf" })
public class FlexQuarantineService implements QuarantineService{

	private QuarantineService service;
	
	@Autowired(required=true)
	public FlexQuarantineService(@Qualifier("jdbcQuarantineService")QuarantineService service) {
		this.service = service;
	}

	@Override
	public void save(QuarantineVO quarantine) {
		service.save(quarantine);
	}

	@Override
	public QuarantineVO read(String domain) {
		return service.read(domain);
	}

}
