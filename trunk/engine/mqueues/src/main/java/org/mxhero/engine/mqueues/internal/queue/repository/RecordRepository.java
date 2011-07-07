package org.mxhero.engine.mqueues.internal.queue.repository;

import java.util.Collection;

import org.mxhero.engine.mqueues.internal.queue.entity.Record;
import org.mxhero.engine.mqueues.internal.queue.entity.RecordPk;

public interface RecordRepository {

    public Record findById(RecordPk id);
    
    public Collection<Record> findAll();
    
    public Long countEnqueued();
    
    public Long countDequeued();
    
    public Long count(String module, String phase);
    
	public Collection getCountByQueues();
    
    public Record save(Record record);

    public boolean delete(RecordPk pk);
    
    public Record findNext(String module, String phase);
    
    public void resetQueues();
    
    public Record removeAdd(Record remove, Record add);
    
    public boolean reEnqueue(Record o, long time);
}
