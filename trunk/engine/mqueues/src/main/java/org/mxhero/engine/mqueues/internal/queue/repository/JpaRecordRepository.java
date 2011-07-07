package org.mxhero.engine.mqueues.internal.queue.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.h2.jdbcx.JdbcConnectionPool;
import org.mxhero.engine.mqueues.internal.queue.entity.Record;
import org.mxhero.engine.mqueues.internal.queue.entity.RecordPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("personDao")
public class JpaRecordRepository implements RecordRepository {

	private EntityManager em;

	@Autowired
	private JdbcConnectionPool pool;
	
    @PersistenceContext(unitName="mqueuesPer")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
    public Record findById(RecordPk id){
    	return em.find(Record.class, id);
    }
    
    @Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
    public Collection<Record> findAll(){
    	return em.createQuery("select r from Record r order by r.id.insertDate ASC, r.id.sequence ASC ", Record.class).getResultList();
    }
    
    @Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
    public Long countEnqueued(){
    	return em.createQuery("select count(r) from Record r where r.enqueued = true ", Long.class).getSingleResult();
    }
    
    @Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
    public Long countDequeued(){
    	return em.createQuery("select count(r) from Record r where r.enqueued = false ", Long.class).getSingleResult();
    }
    
   @Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
   public Record save(Record record){
	   record = em.merge(record);
	   return record;
    }

   @Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
    public boolean delete(RecordPk pk){
	   em.createQuery("delete from RecordProperty r where r.id.recordId.insertDate = ? and r.id.recordId.sequence = ? ").setParameter(1, pk.getInsertDate()).setParameter(2, pk.getSequence()).setLockMode(LockModeType.PESSIMISTIC_WRITE).executeUpdate();
	   int count = em.createQuery("delete from Record r where r.id.insertDate = ? and r.id.sequence = ? ").setParameter(1, pk.getInsertDate()).setParameter(2, pk.getSequence()).setLockMode(LockModeType.PESSIMISTIC_WRITE).executeUpdate();
   	em.flush();
	em.close();
	   //em.remove(em.find(Record.class, pk));
    	return count > 0;
    	//return true;
    }
    
    @Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
    public Record findNext(String module, String phase){
    	Record record = null;
    	try{
    		CriteriaBuilder builder = em.getCriteriaBuilder();
    		CriteriaQuery<Record> query = em.getCriteriaBuilder().createQuery(Record.class);
    		Root<Record> root = query.from(Record.class);
    		query.select(root).where(builder.and(builder.equal(root.get("enqueued"), true),builder.equal(root.get("module"),module),builder.equal(root.get("phase"),phase))).orderBy(builder.asc(root.get("inQueueSince")));
    		record = em.createQuery(query).setMaxResults(1).getSingleResult();
    	}catch(NoResultException ex){
    		record = null;
    	}
    	if(record!=null){
    		record.setEnqueued(false);
    		record = em.merge(record);
    	}
    	return record;
    }
    
    @Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
    public void resetQueues(){
    	em.createQuery("update Record r set r.enqueued = true").executeUpdate();
    }

	@Override
	@Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
	public Long count(String module, String phase) {
		return em.createQuery("select count(r) from Record r where r.module = ? and r.phase = ?", Long.class).setParameter(1, module).setParameter(2, phase).getSingleResult();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = true, propagation=Propagation.REQUIRES_NEW)
	public Collection getCountByQueues() {
		Collection data = em.createQuery("select r.module, r.phase, 'enqueued', count(r)  from Record r where r.enqueued = true group by r.module, r.phase").getResultList();
		data.addAll(
				em.createQuery("select r.module, r.phase, 'dequeued', count(r) from Record r where r.enqueued = false group by r.module, r.phase").getResultList());
		return data;
	}

	@Override
	@Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
	public Record removeAdd(Record remove, Record add) {
		Connection con = null;
		try {
			con = pool.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			PreparedStatement statementProp = con.prepareStatement("delete from record_properties r where r.insert_date = ? and r.record_sequence = ? ");
			statementProp.setTimestamp(1, remove.getId().getInsertDate());
			statementProp.setLong(2, remove.getId().getSequence());
			statementProp.execute();
			con.commit();
			PreparedStatement statement = con.prepareStatement("delete from mail_records r where r.insert_date = ? and r.record_sequence = ? ");
			statement.setTimestamp(1, remove.getId().getInsertDate());
			statement.setLong(2, remove.getId().getSequence());
			statement.execute();
	    	add= em.merge(add);
			con.commit();
		} catch (Exception e) {
			try {
				if(con!=null && !con.isClosed()){
					con.rollback();
				}
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		} finally{
			try {
				if(con!=null && !con.isClosed()){
					con.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
		//em.createQuery("delete from RecordProperty r where r.id.recordId.insertDate = ? and r.id.recordId.sequence = ? ").setParameter(1, remove.getId().getInsertDate()).setParameter(2, remove.getId().getSequence()).setLockMode(LockModeType.PESSIMISTIC_WRITE).executeUpdate();
		//em.createQuery("delete from Record r where r.id.insertDate = ? and r.id.sequence = ? ").setParameter(1, remove.getId().getInsertDate()).setParameter(2, remove.getId().getSequence()).setLockMode(LockModeType.PESSIMISTIC_WRITE).executeUpdate();
    	//em.flush();
    	//em.close();
//    	em.remove(em.find(Record.class, remove.getId()));

    	return add;
	}
	
	@Override
	@Transactional(readOnly = false, propagation=Propagation.REQUIRES_NEW)
	public boolean reEnqueue(Record o, long time){
		Record record = em.find(Record.class,o.getId());
		if(record!=null && !record.getEnqueued()){
			record.setInQueueSince(System.currentTimeMillis()+time);
			record.setEnqueued(true);
			em.merge(record);
			return true;
		}
		return false;
	}

	public JdbcConnectionPool getPool() {
		return pool;
	}

	public void setPool(JdbcConnectionPool pool) {
		this.pool = pool;
	}
	
	
}
