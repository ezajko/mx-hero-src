package org.mxhero.engine.plugin.statistics.internal.dao;

import java.sql.Timestamp;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.RecordPk;
import org.synyx.hades.dao.GenericDao;
import org.synyx.hades.dao.Param;
import org.synyx.hades.dao.Query;

/**
 * Dao used by hades to implement all the persistence needs.
 * @author mmarmol
 */
public interface RecordDao extends GenericDao<Record, RecordPk>{

	@Query("select count(rec.id.insertDate) from Record rec where rec.id.insertDate > :sinceDate and rec.userId = :userId and rec.phase = :phase" )
	Long amountOfUserEmailsSince(@Param("sinceDate") Timestamp sinceDate,
								@Param("userId") String userId,
								@Param("phase") String phase);

	@Query("select sum(rec.bytesSize) from Record rec where rec.id.insertDate > :sinceDate and rec.userId = :userId and rec.phase = :phase" )
	Long bytesOfUserEmailsSince(@Param("sinceDate") Timestamp sinceDate,
								@Param("userId") String userId,
								@Param("phase") String phase);
}
