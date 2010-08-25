package org.mxhero.engine.plugin.statistics.internal.dao;

import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.mxhero.engine.plugin.statistics.internal.entity.StatPk;
import org.synyx.hades.dao.GenericDao;

/**
 * Dao used by hades to implement all the persistence needs.
 * @author mmarmol
 */
public interface StatDao extends GenericDao<Stat, StatPk>{

}
