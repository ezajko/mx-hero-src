package org.mxhero.console.backend.service.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mxhero.console.backend.service.PluginReportService;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;


@Service("pluginReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaPluginReportService implements PluginReportService{

	@PersistenceContext(unitName = "statisticsPer")
	private EntityManager entityManager;
	
	@Override
	public Collection getResult(String queryString, List params) {
		Query query = entityManager.createNativeQuery(queryString);
		if(params!=null && params.size()>0){
			for(int position=0;position<params.size();position++){
				query.setParameter(position+1, params.get(position));
			}
		}
		if(queryString.toLowerCase().contains(" limit ")){
			return query.getResultList();
		}
		return query.setMaxResults(PluginReportService.MAX_RESULT).getResultList();
	}

}
