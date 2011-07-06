package org.mxhero.console.backend.dao;

import java.util.List;

import org.mxhero.console.backend.entity.Group;
import org.mxhero.console.backend.entity.GroupPk;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface GroupDao extends GenericDao<Group, GroupPk>{

	@Query("Select g From Group g WHERE g.id.domainId = :domainId order by g.id.name")
	List<Group> findByDomainId(@Param("domainId") String domainId);
}
