package org.mxhero.engine.plugin.statistics.internal.dao;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.RecordPk;
import org.synyx.hades.dao.GenericDao;

/**
 * Dao used by hades to implement all the persistence needs.
 * @author mmarmol
 */
public interface RecordDao extends GenericDao<Record, RecordPk>{

}
