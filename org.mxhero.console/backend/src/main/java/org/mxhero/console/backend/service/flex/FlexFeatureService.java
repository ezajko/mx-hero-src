package org.mxhero.console.backend.service.flex;

import java.util.Collection;

import org.mxhero.console.backend.service.FeatureService;
import org.mxhero.console.backend.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service("featureService")
@RemotingDestination(channels={"flex-amf"})
public class FlexFeatureService implements FeatureService {

	private FeatureService service;

	@Autowired(required=true)
	public FlexFeatureService(@Qualifier("jdbcFeatureService")FeatureService service) {
		this.service = service;
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Collection<CategoryVO> findFeatures() {
		return service.findFeatures();
	}

	@Override
	@Secured("ROLE_DOMAIN_ADMIN")
	public Collection<CategoryVO> findFeaturesByDomainId(String domainId) {
		return service.findFeaturesByDomainId(domainId);
	}

}
