package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.Group;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface GroupDao extends GenericDao<Group, Integer>{

	@Query("Select g From Group g WHERE g.domain.id = :domainId")
	List<Group> findByDomainId(@Param("domainId") Integer domainId);
}
