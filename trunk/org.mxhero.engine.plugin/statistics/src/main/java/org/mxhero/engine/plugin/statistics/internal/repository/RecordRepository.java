package org.mxhero.engine.plugin.statistics.internal.repository;

import java.util.Collection;

import org.mxhero.engine.plugin.statistics.internal.entity.Record;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;

public interface RecordRepository {

	void saveRecord(Collection<Record> records);
	
	void saveRecord(Record record);
	
	void saveStat(Collection<Stat> stats);
	
	void saveStat(Stat stat);
}
