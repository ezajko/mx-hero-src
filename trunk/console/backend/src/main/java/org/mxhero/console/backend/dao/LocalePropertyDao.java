package org.mxhero.console.backend.dao;

import org.mxhero.console.backend.entity.LocaleProperty;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

public interface LocalePropertyDao extends GenericDao<LocaleProperty, Integer>{

	@Query("From LocaleProperty lp WHERE lp.component = :component AND lp.locale = :locale AND lp.propertyKey = :propertyKey")
	LocaleProperty findByComponentAndLocaleAndPropertyKey(@Param("component") String component,
													@Param("locale") String locale,
													@Param("propertyKey") String propertyKey);

}
